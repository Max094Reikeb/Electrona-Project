package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.blocks.WaterPump;
import net.reikeb.electrona.containers.WaterPumpContainer;
import net.reikeb.electrona.init.SoundsInit;
import net.reikeb.electrona.misc.vm.EnergyFunction;
import net.reikeb.electrona.misc.vm.FluidFunction;
import net.reikeb.maxilib.abs.AbstractFluidBlockEntity;
import net.reikeb.maxilib.intface.EnergyInterface;
import net.reikeb.maxilib.intface.FluidInterface;
import net.reikeb.maxilib.inventory.ItemHandler;

import static net.reikeb.electrona.init.BlockEntityInit.WATER_PUMP_BLOCK_ENTITY;

public class WaterPumpBlockEntity extends AbstractFluidBlockEntity implements EnergyInterface {

    public static final BlockEntityTicker<WaterPumpBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    private double electronicPower;
    private int maxStorage;
    private boolean isOn;
    private int wait;

    public WaterPumpBlockEntity(BlockPos pos, BlockState state) {
        super(WATER_PUMP_BLOCK_ENTITY.get(), pos, state, "water_pump", Electrona.MODID, 2, 10000);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new WaterPumpContainer(id, this.getBlockPos(), playerInventory, player);
    }

    public <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState state, T t) {
        this.setMaxEnergy(1000);

        // Input slot - Handling slots
        if ((this.inventory.getStackInSlot(0).getItem() == Items.BUCKET)
                && (this.getWaterLevel() >= 1000)) {
            this.inventory.decrStackSize(0, 1);
            this.inventory.insertItem(0, new ItemStack(Items.WATER_BUCKET, 1), false);
            FluidInterface.drainWater(this, 1000);
        }

        // Output slot - Handling slots
        EnergyFunction.transferEnergyWithItemSlot(this, false, 1, 4);

        level.setBlockAndUpdate(blockPos, this.getBlockState().setValue(WaterPump.PUMPING, isOn));

        if (level.isClientSide) return;

        // Pump water
        if (this.isOn) {
            wait += 1;
            if (wait >= 15) {
                if (this.electronicPower >= 20
                        && Blocks.WATER == level.getBlockState(blockPos.below()).getBlock()) {
                    if (this.getTankCapacity() >= (this.getWaterLevel() + 100)) {
                        FluidInterface.fillWater(this, 100);
                        EnergyInterface.drainEnergy(this, 20);
                        level.setBlockAndUpdate(blockPos.below(), Blocks.AIR.defaultBlockState());
                        level.playSound(null, this.getBlockPos(), SoundsInit.WATER_PUMPING.get(), SoundSource.BLOCKS, 0.6F, 1.0F);
                    } else if (this.getTankCapacity() >= (this.getWaterLevel() + 50)) {
                        FluidInterface.fillWater(this, 50);
                        EnergyInterface.drainEnergy(this, 10);
                        level.setBlockAndUpdate(blockPos.below(), Blocks.AIR.defaultBlockState());
                        level.playSound(null, this.getBlockPos(), SoundsInit.WATER_PUMPING.get(), SoundSource.BLOCKS, 0.6F, 1.0F);
                    } else if (this.getTankCapacity() >= (this.getWaterLevel() + 10)) {
                        FluidInterface.fillWater(this, 10);
                        EnergyInterface.drainEnergy(this, 2);
                        level.setBlockAndUpdate(blockPos.below(), Blocks.AIR.defaultBlockState());
                        level.playSound(null, this.getBlockPos(), SoundsInit.WATER_PUMPING.get(), SoundSource.BLOCKS, 0.6F, 1.0F);
                    } else if (this.getTankCapacity() > this.getWaterLevel()) {
                        FluidInterface.fillWater(this, 1);
                        EnergyInterface.drainEnergy(this, 0.2);
                        level.setBlockAndUpdate(blockPos.below(), Blocks.AIR.defaultBlockState());
                        level.playSound(null, this.getBlockPos(), SoundsInit.WATER_PUMPING.get(), SoundSource.BLOCKS, 0.6F, 1.0F);
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
        FluidFunction.generatorTransferFluid(level, blockPos, this, 100);

        this.setChanged();
        level.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(), 3);
    }

    public ItemHandler getItemInventory() {
        return this.inventory;
    }

    public void setHundredEnergy(int hundredEnergy) {
        this.electronicPower = hundredEnergy / 100.0;
    }

    public double getEnergy() {
        return this.electronicPower;
    }

    public void setEnergy(double energy) {
        this.electronicPower = energy;
    }

    public int getMaxEnergy() {
        return this.maxStorage;
    }

    public void setMaxEnergy(int maxEnergy) {
        this.maxStorage = maxEnergy;
    }

    public int isOn() {
        return this.isOn ? 1 : 0;
    }

    public void setOn(int isOn) {
        this.isOn = (isOn == 1);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        this.isOn = compound.getBoolean("isOn");
        this.wait = compound.getInt("wait");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.putBoolean("isOn", this.isOn);
        compound.putInt("wait", this.wait);
    }
}
