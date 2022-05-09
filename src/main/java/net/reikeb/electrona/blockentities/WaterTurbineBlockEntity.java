package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.reikeb.electrona.blocks.WaterTurbine;
import net.reikeb.electrona.misc.vm.EnergyFunction;
import net.reikeb.maxilib.abs.AbstractEnergyBlockEntity;
import net.reikeb.maxilib.inventory.ItemHandler;

import javax.annotation.Nullable;
import java.util.Objects;

import static net.reikeb.electrona.init.BlockEntityInit.WATER_TURBINE_BLOCK_ENTITY;

public class WaterTurbineBlockEntity extends BlockEntity implements AbstractEnergyBlockEntity {

    public static final BlockEntityTicker<WaterTurbineBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    private double electronicPower;
    private int maxStorage;

    public WaterTurbineBlockEntity(BlockPos pos, BlockState state) {
        super(WATER_TURBINE_BLOCK_ENTITY.get(), pos, state);
    }

    public Direction getDirection() {
        return this.getBlockState().getValue(WaterTurbine.FACING);
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
        // We get the variables
        BlockPos frontPos = blockPos.relative(this.getDirection().getOpposite());
        BlockPos backPos = blockPos.relative(this.getDirection());

        this.setMaxStorage(1000);
        if (world == null) return;

        // We generate the energy (this part is uncommon for all generators)
        if ((Blocks.AIR == world.getBlockState(backPos).getBlock())
                && (Material.WATER == world.getBlockState(frontPos).getMaterial())) {
            world.setBlockAndUpdate(blockPos, this.getBlockState().setValue(WaterTurbine.WATERLOGGED, true));
        } else if (Material.WATER != world.getBlockState(frontPos).getMaterial()) {
            world.setBlockAndUpdate(blockPos, this.getBlockState().setValue(WaterTurbine.WATERLOGGED, false));
        }

        if ((Material.WATER == world.getBlockState(backPos).getMaterial())
                && (Material.WATER == world.getBlockState(frontPos).getMaterial())) {
            if ((this.electronicPower < 996)) {
                EnergyFunction.fillEnergy(this, 0.2);
            } else if (((this.electronicPower >= 996) && (this.electronicPower <= 999.95))) {
                EnergyFunction.fillEnergy(this, 0.05);
            }
        } else {
            if ((this.electronicPower > 0.2)) {
                EnergyFunction.drainEnergy(this, 0.2);
            } else if (((this.electronicPower <= 0.2) && (this.electronicPower >= 0.05))) {
                EnergyFunction.drainEnergy(this, 0.05);
            }
        }

        // We pass energy to blocks around (this part is common to all generators)
        EnergyFunction.generatorTransferEnergy(world, blockPos, Direction.values(), this, 4, true);
    }

    public ItemHandler getItemInventory() {
        return null;
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
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        CompoundTag nbt = new CompoundTag();
        saveAdditional(nbt);
        return ClientboundBlockEntityDataPacket.create(this, blockEntity -> nbt);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(Objects.requireNonNull(pkt.getTag()));
    }
}
