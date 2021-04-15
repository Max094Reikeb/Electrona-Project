package net.reikeb.electrona.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.*;
import net.minecraftforge.energy.*;
import net.minecraftforge.items.*;

import net.reikeb.electrona.containers.ConverterContainer;
import net.reikeb.electrona.init.*;
import net.reikeb.electrona.misc.vm.EnergyFunction;
import net.reikeb.electrona.utils.ItemHandler;

import static net.reikeb.electrona.init.TileEntityInit.*;

public class TileConverter extends LockableLootTileEntity implements ITickableTileEntity {

    private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
    private final ItemHandler inventory;

    public double electronicPower;
    private int maxStorage;
    private boolean toVP;
    private boolean toOthers;
    private double vp;
    private int wait;

    public TileConverter() {
        super(TILE_CONTERTER.get());

        this.inventory = new ItemHandler(1);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gui.electrona.el_converter.name");
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new StringTextComponent("el_converter");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.stacks;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> stacks) {
        this.stacks = stacks;
    }

    @Override
    public Container createMenu(final int windowID, final PlayerInventory playerInv, final PlayerEntity playerIn) {
        return new ConverterContainer(windowID, playerInv, this);
    }

    @Override
    public Container createMenu(int id, PlayerInventory player) {
        return new ConverterContainer(ContainerInit.CONVERTER_CONTAINER.get(), id);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public void tick() {
        // We get the variables
        World world = this.level;
        BlockPos blockPos = this.getBlockPos();

        // We get the NBT Tags
        this.getTileData().putInt("MaxStorage", 10000);
        double electronicPower = this.getTileData().getDouble("ElectronicPower");
        boolean toVP = this.getTileData().getBoolean("toVP");
        boolean toOthers = this.getTileData().getBoolean("toOthers");

        if (world != null) { // Avoid NullPointerExceptions

            wait += 1;
            if (wait >= 15) {
                if (((!toVP) && (!toOthers)) || (toVP && toOthers)) {
                    this.getTileData().putBoolean("toVP", true);
                    this.getTileData().putBoolean("toOthers", false);
                }

                if (toVP) {
                    if (electronicPower >= 1) {
                        this.getTileData().putDouble("vp", (this.getTileData().getDouble("vp") + 3));
                        this.getTileData().putDouble("ElectronicPower", electronicPower - 1);
                    }
                } else if (toOthers) {
                    if (electronicPower >= 1) {
                        {
                            this.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(cap -> cap.receiveEnergy(3, false));
                        }
                        this.getTileData().putDouble("ElectronicPower", electronicPower - 1);
                    }
                }
                wait = 0;
            }

            // Output slot - Handling slots
            EnergyFunction.transferEnergyWithItemSlot(this.getTileData(), ItemInit.PORTABLE_BATTERY.get().asItem(), inventory, false, electronicPower, 0, 4);

            this.setChanged();
            world.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(),
                    Constants.BlockFlags.NOTIFY_NEIGHBORS);
        }
    }

    public final IItemHandlerModifiable getInventory() {
        return this.inventory;
    }

    @Override
    public void load(BlockState blockState, CompoundNBT compound) {
        super.load(blockState, compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        this.toVP = compound.getBoolean("toVP");
        this.toOthers = compound.getBoolean("toOthers");
        this.vp = compound.getDouble("vp");
        this.wait = compound.getInt("wait");
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundNBT) compound.get("Inventory"));
        }
        if (compound.contains("energyStorage")) {
            CapabilityEnergy.ENERGY.readNBT(energyStorage, null, compound.get("energyStorage"));
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.putBoolean("toVP", this.toVP);
        compound.putBoolean("toOthers", this.toOthers);
        compound.putDouble("vp", this.vp);
        compound.putInt("wait", this.wait);
        compound.put("Inventory", inventory.serializeNBT());
        compound.put("energyStorage", CapabilityEnergy.ENERGY.writeNBT(energyStorage, null));
        return compound;
    }

    private final EnergyStorage energyStorage = new EnergyStorage(10000, 40, 40, 0) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int retval = super.receiveEnergy(maxReceive, simulate);
            if (!simulate) {
                setChanged();
                level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 2);
            }
            return retval;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            int retval = super.extractEnergy(maxExtract, simulate);
            if (!simulate) {
                setChanged();
                level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 2);
            }
            return retval;
        }
    };

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == CapabilityEnergy.ENERGY)
            return LazyOptional.of(() -> energyStorage).cast();
        if (!this.remove && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> this.inventory).cast();
        return super.getCapability(cap, side);
    }

    public void dropItems(World world, BlockPos pos) {
        for (int i = 0; i < 1; i++)
            if (!inventory.getStackInSlot(i).isEmpty()) {
                InventoryHelper.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(i));
            }
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 0, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.load(this.getBlockState(), pkt.getTag());
    }

    @Override
    public int getContainerSize() {
        return 1;
    }
}