package net.reikeb.electrona.tileentities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.entity.player.*;
import net.minecraft.world.Containers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.level.Level;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.*;
import net.minecraftforge.energy.*;
import net.minecraftforge.items.*;

import net.reikeb.electrona.containers.ConverterContainer;
import net.reikeb.electrona.init.*;
import net.reikeb.electrona.misc.vm.EnergyFunction;
import net.reikeb.electrona.utils.ItemHandler;

import static net.reikeb.electrona.init.TileEntityInit.*;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;

public class TileConverter extends RandomizableContainerBlockEntity implements TickableBlockEntity {

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
    public Component getDisplayName() {
        return new TranslatableComponent("gui.electrona.el_converter.name");
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("el_converter");
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
    public AbstractContainerMenu createMenu(final int windowID, final Inventory playerInv, final Player playerIn) {
        return new ConverterContainer(windowID, playerInv, this);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new ConverterContainer(ContainerInit.CONVERTER_CONTAINER.get(), id);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public void tick() {
        // We get the variables
        Level world = this.level;
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
    public void load(BlockState blockState, CompoundTag compound) {
        super.load(blockState, compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        this.toVP = compound.getBoolean("toVP");
        this.toOthers = compound.getBoolean("toOthers");
        this.vp = compound.getDouble("vp");
        this.wait = compound.getInt("wait");
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundTag) compound.get("Inventory"));
        }
        if (compound.contains("energyStorage")) {
            CapabilityEnergy.ENERGY.readNBT(energyStorage, null, compound.get("energyStorage"));
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
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

    public void dropItems(Level world, BlockPos pos) {
        for (int i = 0; i < 1; i++)
            if (!inventory.getStackInSlot(i).isEmpty()) {
                Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(i));
            }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 0, this.getUpdateTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(this.getBlockState(), pkt.getTag());
    }

    @Override
    public int getContainerSize() {
        return 1;
    }
}