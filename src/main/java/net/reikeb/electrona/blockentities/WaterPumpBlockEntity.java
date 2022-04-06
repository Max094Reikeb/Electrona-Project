package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
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
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import net.reikeb.electrona.blocks.WaterPump;
import net.reikeb.electrona.containers.WaterPumpContainer;
import net.reikeb.electrona.init.SoundsInit;
import net.reikeb.electrona.misc.vm.EnergyFunction;
import net.reikeb.electrona.misc.vm.FluidFunction;
import net.reikeb.electrona.utils.FluidTankHandler;
import net.reikeb.electrona.utils.ItemHandler;

import java.util.concurrent.atomic.AtomicInteger;

import static net.reikeb.electrona.init.BlockEntityInit.WATER_PUMP_BLOCK_ENTITY;

public class WaterPumpBlockEntity extends AbstractBlockEntity implements AbstractEnergyBlockEntity {

    public static final BlockEntityTicker<WaterPumpBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
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
    public int maxStorage;
    private boolean isOn;
    private int wait;

    public WaterPumpBlockEntity(BlockPos pos, BlockState state) {
        super(WATER_PUMP_BLOCK_ENTITY.get(), pos, state, 2);
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
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new WaterPumpContainer(id, this.getBlockPos(), playerInventory, player);
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
        this.setMaxStorage(1000);

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
        EnergyFunction.transferEnergyWithItemSlot(this, false, 1, 4);

        world.setBlockAndUpdate(blockPos, this.getBlockState().setValue(WaterPump.PUMPING, isOn));

        if (world.isClientSide) return;

        // Pump water
        if (this.isOn) {
            wait += 1;
            if (wait >= 15) {
                if (this.electronicPower >= 20
                        && Blocks.WATER == world.getBlockState(blockPos.below()).getBlock()) {
                    if (tankCapacity.get() >= (waterLevel.get() + 100)) {
                        FluidFunction.fillWater(this, 100);
                        this.electronicPower -= 20;
                        world.setBlockAndUpdate(blockPos.below(), Blocks.AIR.defaultBlockState());
                        world.playSound(null, this.getBlockPos(), SoundsInit.WATER_PUMPING.get(), SoundSource.BLOCKS, 0.6F, 1.0F);
                    } else if (tankCapacity.get() >= (waterLevel.get() + 50)) {
                        FluidFunction.fillWater(this, 50);
                        this.electronicPower -= 10;
                        world.setBlockAndUpdate(blockPos.below(), Blocks.AIR.defaultBlockState());
                        world.playSound(null, this.getBlockPos(), SoundsInit.WATER_PUMPING.get(), SoundSource.BLOCKS, 0.6F, 1.0F);
                    } else if (tankCapacity.get() >= (waterLevel.get() + 10)) {
                        FluidFunction.fillWater(this, 10);
                        this.electronicPower -= 2;
                        world.setBlockAndUpdate(blockPos.below(), Blocks.AIR.defaultBlockState());
                        world.playSound(null, this.getBlockPos(), SoundsInit.WATER_PUMPING.get(), SoundSource.BLOCKS, 0.6F, 1.0F);
                    } else if (tankCapacity.get() > waterLevel.get()) {
                        FluidFunction.fillWater(this, 1);
                        this.electronicPower -= 0.2;
                        world.setBlockAndUpdate(blockPos.below(), Blocks.AIR.defaultBlockState());
                        world.playSound(null, this.getBlockPos(), SoundsInit.WATER_PUMPING.get(), SoundSource.BLOCKS, 0.6F, 1.0F);
                    } else {
                        this.isOn = false;
                    }
                } else if (this.electronicPower < 20) {
                    this.isOn = false;
                }
                wait = 0;
            }
        } else {
            wait = 0;
        }

        // We pass water to blocks around
        FluidFunction.generatorTransferFluid(world, blockPos, Direction.values(), this, waterLevel.get(), 100);

        this.setChanged();
        world.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(), 3);
    }

    public ItemHandler getItemInventory() {
        return this.inventory;
    }

    public int getElectronicPowerTimesHundred() {
        return (int) (this.electronicPower * 100);
    }

    public void setElectronicPowerTimesHundred(int electronicPowerTimesHundred) {
        this.electronicPower = electronicPowerTimesHundred / 100.0;
    }

    public double getElectronicPower() {
        return this.electronicPower;
    }

    public void setElectronicPower(double electronicPower) {
        this.electronicPower = electronicPower;
    }

    public int getMaxStorage() {
        return this.maxStorage;
    }

    public void setMaxStorage(int maxStorage) {
        this.maxStorage = maxStorage;
    }

    public int getWaterLevel() {
        return FluidFunction.getFluidAmount(this).get();
    }

    public void setWaterLevel(int amount) {
        AtomicInteger waterLevel = FluidFunction.getFluidAmount(this);
        FluidFunction.drainWater(this, waterLevel.get());
        FluidFunction.fillWater(this, amount);
    }

    public int isOn() {
        return this.isOn ? 1 : 0;
    }

    public void setOn(int isOn) {
        this.isOn = (isOn == 1);
    }

    public boolean getLogic() {
        return false;
    }

    public void setLogic(boolean logic) {
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        this.isOn = compound.getBoolean("isOn");
        this.wait = compound.getInt("wait");
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
        compound.put("fluidTank", fluidTank.serializeNBT());
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> fluidTank).cast();
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this.inventory));
    }
}
