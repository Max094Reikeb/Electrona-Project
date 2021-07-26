package net.reikeb.electrona.tileentities;

import net.minecraft.block.*;
import net.minecraft.world.level.material.Material;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tileentity.*;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import net.reikeb.electrona.blocks.WaterTurbine;
import net.reikeb.electrona.misc.vm.EnergyFunction;

import static net.reikeb.electrona.init.TileEntityInit.*;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileWaterTurbine extends BlockEntity implements TickableBlockEntity {

    private double electronicPower;
    private int maxStorage;

    public TileWaterTurbine() {
        super(TILE_WATER_TURBINE.get());
    }

    public Direction getDirection() {
        return this.getBlockState().getValue(WaterTurbine.FACING);
    }

    @Override
    public void tick() {
        // We get the variables
        Level world = this.level;
        BlockPos blockPos = this.getBlockPos();
        BlockPos frontPos = blockPos.relative(this.getDirection().getOpposite());
        BlockPos backPos = blockPos.relative(this.getDirection());

        // We get the NBT Tags
        this.getTileData().putInt("MaxStorage", 1000);
        double electronicPower = this.getTileData().getDouble("ElectronicPower");

        if (world != null) { // Avoid NullPointerExceptions

            // We generate the energy (this part is uncommon for all generators)
            if ((Blocks.AIR == world.getBlockState(backPos).getBlock())
                    && (Material.WATER == world.getBlockState(frontPos).getMaterial())) {
                world.setBlockAndUpdate(blockPos, this.getBlockState().setValue(WaterTurbine.WATERLOGGED, true));
            } else if (Material.WATER != world.getBlockState(frontPos).getMaterial()) {
                world.setBlockAndUpdate(blockPos, this.getBlockState().setValue(WaterTurbine.WATERLOGGED, false));
            }

            if ((Material.WATER == world.getBlockState(backPos).getMaterial())
                    && (Material.WATER == world.getBlockState(frontPos).getMaterial())) {
                if ((electronicPower < 996)) {
                    this.getTileData().putDouble("ElectronicPower", (electronicPower + 0.2));
                } else if (((electronicPower >= 996) && (electronicPower <= 999.95))) {
                    this.getTileData().putDouble("ElectronicPower", (electronicPower + 0.05));
                }
            } else {
                if ((electronicPower > 0.2)) {
                    this.getTileData().putDouble("ElectronicPower", (electronicPower - 0.2));
                } else if (((electronicPower <= 0.2) && (electronicPower >= 0.05))) {
                    this.getTileData().putDouble("ElectronicPower", (electronicPower - 0.05));
                }
            }

            // We pass energy to blocks around (this part is common to all generators)
            EnergyFunction.generatorTransferEnergy(world, blockPos, Direction.values(), this.getTileData(), 4, electronicPower, true);
        }
    }

    @Override
    public void load(BlockState blockState, CompoundTag compound) {
        super.load(blockState, compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound = super.save(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        return compound;
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
}
