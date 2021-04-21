package net.reikeb.electrona.tileentities;

import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.*;
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
import net.minecraftforge.fluids.capability.*;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.*;

import net.reikeb.electrona.blocks.WaterPump;
import net.reikeb.electrona.containers.WaterPumpContainer;
import net.reikeb.electrona.init.*;
import net.reikeb.electrona.misc.vm.*;
import net.reikeb.electrona.utils.ItemHandler;

import static net.reikeb.electrona.init.TileEntityInit.*;

import java.util.concurrent.atomic.AtomicInteger;

public class TileWaterPump extends LockableLootTileEntity implements ITickableTileEntity {

    private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY);
    private final ItemHandler inventory;

    public double electronicPower;
    private int maxStorage;
    private boolean isOn;
    private int wait;

    public TileWaterPump() {
        super(TILE_WATER_PUMP.get());

        this.inventory = new ItemHandler(2);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gui.electrona.water_pump.name");
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new StringTextComponent("water_pump");
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
        return new WaterPumpContainer(windowID, playerInv, this);
    }

    @Override
    public Container createMenu(int id, PlayerInventory player) {
        return new WaterPumpContainer(ContainerInit.WATER_PUMP_CONTAINER.get(), id);
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
        BlockPos posUnder = new BlockPos(blockPos.getX(), (blockPos.getY() - 1), blockPos.getZ());

        // We get the NBT Tags
        this.getTileData().putInt("MaxStorage", 1000);
        double electronicPower = this.getTileData().getDouble("ElectronicPower");
        boolean isOn = this.getTileData().getBoolean("isOn");

        AtomicInteger waterLevel = new AtomicInteger();
        this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).ifPresent(cap -> waterLevel.set(cap.getFluidInTank(1).getAmount()));
        AtomicInteger tankCapacity = new AtomicInteger();
        this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).ifPresent(cap -> tankCapacity.set(cap.getTankCapacity(1)));

        if (world != null) { // Avoid NullPointerExceptions

            world.setBlockAndUpdate(blockPos, this.getBlockState().setValue(WaterPump.PUMPING, isOn));

            // Pump water
            if (isOn) {
                wait += 1;
                if (wait >= 15) {
                    if (electronicPower >= 20
                            && Blocks.WATER == world.getBlockState(posUnder).getBlock()) {
                        if (tankCapacity.get() >= (waterLevel.get() + 100)) {
                            FluidFunction.fillWater(this, 100);
                            electronicPower -= 20;
                            world.setBlockAndUpdate(posUnder, Blocks.AIR.defaultBlockState());
                            world.playSound(null, this.getBlockPos(), SoundsInit.WATER_PUMPING.get(), SoundCategory.BLOCKS, 0.6F, 1.0F);
                        } else if (tankCapacity.get() >= (waterLevel.get() + 50)) {
                            FluidFunction.fillWater(this, 50);
                            electronicPower -= 10;
                            world.setBlockAndUpdate(posUnder, Blocks.AIR.defaultBlockState());
                            world.playSound(null, this.getBlockPos(), SoundsInit.WATER_PUMPING.get(), SoundCategory.BLOCKS, 0.6F, 1.0F);
                        } else if (tankCapacity.get() >= (waterLevel.get() + 10)) {
                            FluidFunction.fillWater(this, 10);
                            electronicPower -= 2;
                            world.setBlockAndUpdate(posUnder, Blocks.AIR.defaultBlockState());
                            world.playSound(null, this.getBlockPos(), SoundsInit.WATER_PUMPING.get(), SoundCategory.BLOCKS, 0.6F, 1.0F);
                        } else if (tankCapacity.get() > waterLevel.get()) {
                            FluidFunction.fillWater(this, 1);
                            electronicPower -= 0.2;
                            world.setBlockAndUpdate(posUnder, Blocks.AIR.defaultBlockState());
                            world.playSound(null, this.getBlockPos(), SoundsInit.WATER_PUMPING.get(), SoundCategory.BLOCKS, 0.6F, 1.0F);
                        } else {
                            isOn = false;
                        }
                        this.getTileData().putDouble("ElectronicPower", electronicPower);
                    } else if (electronicPower < 20) {
                        isOn = false;
                    }
                    wait = 0;
                }
            } else {
                wait = 0;
            }
            this.getTileData().putBoolean("isOn", isOn);

            // Input slot - Handling slots
            if ((this.inventory.getStackInSlot(0).getItem() == Items.BUCKET)
                    && (waterLevel.get() >= 1000)) {
                this.inventory.decrStackSize(0, 1);
                this.inventory.insertItem(0, new ItemStack(Items.WATER_BUCKET, 1), false);
                FluidFunction.drainWater(this, 1000);
            }

            // Output slot - Handling slots
            EnergyFunction.transferEnergyWithItemSlot(this.getTileData(), ItemInit.PORTABLE_BATTERY.get().asItem(), inventory, false, electronicPower, 1, 4);

            // We pass water to blocks around
            FluidFunction.generatorTransferFluid(world, blockPos, Direction.values(), this, waterLevel.get(), 100);

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
        this.isOn = compound.getBoolean("isOn");
        this.wait = compound.getInt("wait");
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundNBT) compound.get("Inventory"));
        }
        if (compound.get("fluidTank") != null)
            CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(fluidTank, null, compound.get("fluidTank"));
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.putBoolean("isOn", this.isOn);
        compound.putInt("wait", this.wait);
        compound.put("Inventory", inventory.serializeNBT());
        compound.put("fluidTank", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(fluidTank, null));
        return compound;
    }

    private final FluidTank fluidTank = new FluidTank(10000, fs -> {
        if (fs.getFluid() == Fluids.WATER)
            return true;
        return false;
    }) {
        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            setChanged();
            level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 2);
        }
    };

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> fluidTank).cast();
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this.inventory));
    }

    public void dropItems(World world, BlockPos pos) {
        for (int i = 0; i < 2; i++)
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
        return 2;
    }
}
