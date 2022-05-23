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
import net.reikeb.maxilib.intface.EnergyInterface;
import net.reikeb.maxilib.inventory.ItemHandler;

import javax.annotation.Nullable;
import java.util.Objects;

import static net.reikeb.electrona.init.BlockEntityInit.WATER_TURBINE_BLOCK_ENTITY;

public class WaterTurbineBlockEntity extends BlockEntity implements EnergyInterface {

    public static final BlockEntityTicker<WaterTurbineBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    private double electronicPower;
    private int maxStorage;

    public WaterTurbineBlockEntity(BlockPos pos, BlockState state) {
        super(WATER_TURBINE_BLOCK_ENTITY.get(), pos, state);
    }

    public Direction getDirection() {
        return this.getBlockState().getValue(WaterTurbine.FACING);
    }

    public <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState state, T t) {
        // We get the variables
        BlockPos frontPos = blockPos.relative(this.getDirection().getOpposite());
        BlockPos backPos = blockPos.relative(this.getDirection());

        this.setMaxEnergy(1000);
        if (level == null) return;

        // We generate the energy (this part is uncommon for all generators)
        if ((Blocks.AIR == level.getBlockState(backPos).getBlock())
                && (Material.WATER == level.getBlockState(frontPos).getMaterial())) {
            level.setBlockAndUpdate(blockPos, this.getBlockState().setValue(WaterTurbine.WATERLOGGED, true));
        } else if (Material.WATER != level.getBlockState(frontPos).getMaterial()) {
            level.setBlockAndUpdate(blockPos, this.getBlockState().setValue(WaterTurbine.WATERLOGGED, false));
        }

        if ((Material.WATER == level.getBlockState(backPos).getMaterial())
                && (Material.WATER == level.getBlockState(frontPos).getMaterial())) {
            if ((this.electronicPower < 996)) {
                EnergyInterface.fillEnergy(this, 0.2);
            } else if (((this.electronicPower >= 996) && (this.electronicPower <= 999.95))) {
                EnergyInterface.fillEnergy(this, 0.05);
            }
        } else {
            if ((this.electronicPower > 0.2)) {
                EnergyInterface.drainEnergy(this, 0.2);
            } else if (((this.electronicPower <= 0.2) && (this.electronicPower >= 0.05))) {
                EnergyInterface.drainEnergy(this, 0.05);
            }
        }

        // We pass energy to blocks around (this part is common to all generators)
        EnergyFunction.generatorTransferEnergy(level, blockPos, this, 4, true);
    }

    public ItemHandler getItemInventory() {
        return null;
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
