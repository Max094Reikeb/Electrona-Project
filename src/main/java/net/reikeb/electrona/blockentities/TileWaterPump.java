package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import net.reikeb.electrona.blocks.WaterPump;
import net.reikeb.electrona.containers.WaterPumpContainer;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.init.SoundsInit;
import net.reikeb.electrona.misc.vm.EnergyFunction;
import net.reikeb.electrona.misc.vm.FluidFunction;
import net.reikeb.electrona.utils.FluidTankHandler;

import java.util.concurrent.atomic.AtomicInteger;

import static net.reikeb.electrona.init.TileEntityInit.TILE_WATER_PUMP;

public class TileWaterPump extends AbstractTileEntity {

    public static final BlockEntityTicker<TileWaterPump> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    private final FluidTankHandler fluidTank = new FluidTankHandler(10000, fs -> {
        return fs.getFluid() == Fluids.WATER;
    }) {
        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            setChanged();
            level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 2);
        }
    };
    public double electronicPower;
    private int maxStorage;
    private boolean isOn;
    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int p_39284_) {
            switch (p_39284_) {
                case 0:
                    return (int) TileWaterPump.this.electronicPower;
                case 1:
                    AtomicInteger waterLevel = new AtomicInteger();
                    TileWaterPump.this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                            .ifPresent(cap -> waterLevel.set(cap.getFluidInTank(1).getAmount()));
                    return waterLevel.get();
                case 2:
                    return (TileWaterPump.this.isOn ? 1 : 0);
                default:
                    return 0;
            }
        }

        @Override
        public void set(int p_39285_, int p_39286_) {
            switch (p_39285_) {
                case 0:
                    TileWaterPump.this.electronicPower = p_39286_;
                case 1:
                    TileWaterPump.this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                            .ifPresent(cap -> cap.fill(new FluidStack(Fluids.WATER, p_39286_), IFluidHandler.FluidAction.EXECUTE));
                case 2:
                    TileWaterPump.this.isOn = (p_39286_ == 1);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };
    private int wait;

    public TileWaterPump(BlockPos pos, BlockState state) {
        super(TILE_WATER_PUMP.get(), pos, state, 2);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.electrona.water_pump.name");
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("water_pump");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new WaterPumpContainer(id, player, this, dataAccess);
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
        // We get the NBT Tags
        this.getTileData().putInt("MaxStorage", 1000);
        double electronicPower = this.getTileData().getDouble("ElectronicPower");
        boolean isOn = this.getTileData().getBoolean("isOn");

        AtomicInteger waterLevel = new AtomicInteger();
        this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).ifPresent(cap -> waterLevel.set(cap.getFluidInTank(1).getAmount()));
        AtomicInteger tankCapacity = new AtomicInteger();
        this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).ifPresent(cap -> tankCapacity.set(cap.getTankCapacity(1)));

        // Input slot - Handling slots
        if ((this.inventory.getStackInSlot(0).getItem() == Items.BUCKET)
                && (waterLevel.get() >= 1000)) {
            this.inventory.decrStackSize(0, 1);
            this.inventory.insertItem(0, new ItemStack(Items.WATER_BUCKET, 1), false);
            FluidFunction.drainWater(this, 1000);
        }

        // Output slot - Handling slots
        EnergyFunction.transferEnergyWithItemSlot(this.getTileData(), ItemInit.PORTABLE_BATTERY.get().asItem(), inventory, false, electronicPower, 1, 4);

        world.setBlockAndUpdate(blockPos, this.getBlockState().setValue(WaterPump.PUMPING, isOn));

        if (world.isClientSide) return;

        // Pump water
        if (isOn) {
            wait += 1;
            if (wait >= 15) {
                if (electronicPower >= 20
                        && Blocks.WATER == world.getBlockState(blockPos.below()).getBlock()) {
                    if (tankCapacity.get() >= (waterLevel.get() + 100)) {
                        FluidFunction.fillWater(this, 100);
                        electronicPower -= 20;
                        world.setBlockAndUpdate(blockPos.below(), Blocks.AIR.defaultBlockState());
                        world.playSound(null, this.getBlockPos(), SoundsInit.WATER_PUMPING.get(), SoundSource.BLOCKS, 0.6F, 1.0F);
                    } else if (tankCapacity.get() >= (waterLevel.get() + 50)) {
                        FluidFunction.fillWater(this, 50);
                        electronicPower -= 10;
                        world.setBlockAndUpdate(blockPos.below(), Blocks.AIR.defaultBlockState());
                        world.playSound(null, this.getBlockPos(), SoundsInit.WATER_PUMPING.get(), SoundSource.BLOCKS, 0.6F, 1.0F);
                    } else if (tankCapacity.get() >= (waterLevel.get() + 10)) {
                        FluidFunction.fillWater(this, 10);
                        electronicPower -= 2;
                        world.setBlockAndUpdate(blockPos.below(), Blocks.AIR.defaultBlockState());
                        world.playSound(null, this.getBlockPos(), SoundsInit.WATER_PUMPING.get(), SoundSource.BLOCKS, 0.6F, 1.0F);
                    } else if (tankCapacity.get() > waterLevel.get()) {
                        FluidFunction.fillWater(this, 1);
                        electronicPower -= 0.2;
                        world.setBlockAndUpdate(blockPos.below(), Blocks.AIR.defaultBlockState());
                        world.playSound(null, this.getBlockPos(), SoundsInit.WATER_PUMPING.get(), SoundSource.BLOCKS, 0.6F, 1.0F);
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

        // We pass water to blocks around
        FluidFunction.generatorTransferFluid(world, blockPos, Direction.values(), this, waterLevel.get(), 100);

        this.setChanged();
        world.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(), 3);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        this.isOn = compound.getBoolean("isOn");
        this.wait = compound.getInt("wait");
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundTag) compound.get("Inventory"));
        }
        if (compound.get("fluidTank") != null) {
            fluidTank.readFromNBT((CompoundTag) compound.get("fluidTank"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.putBoolean("isOn", this.isOn);
        compound.putInt("wait", this.wait);
        compound.put("Inventory", inventory.serializeNBT());
        compound.put("fluidTank", fluidTank.serializeNBT());
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> fluidTank).cast();
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this.inventory));
    }
}
