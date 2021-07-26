package net.reikeb.electrona.tileentities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tileentity.*;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import net.reikeb.electrona.misc.vm.EnergyFunction;

import static net.reikeb.electrona.init.TileEntityInit.*;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;

public class TileEnergeticLightningRod extends BlockEntity implements TickableBlockEntity {

    private double electronicPower;
    private int maxStorage;

    public TileEnergeticLightningRod() {
        super(TILE_ENERGETIC_LIGHTNING_ROD.get());
    }

    @Override
    public void tick() {
        // We get the variables
        Level world = this.level;
        BlockPos blockPos = this.getBlockPos();

        // We get the NBT Tags
        this.getTileData().putInt("MaxStorage", 3000);
        double electronicPower = this.getTileData().getDouble("ElectronicPower");

        if (world != null) { // Avoid NullPointerExceptions

            // We pass energy to blocks around (this part is common to all generators)
            EnergyFunction.generatorTransferEnergy(world, blockPos, Direction.values(), this.getTileData(), 50, electronicPower, true);
        }
    }

    public void struckByLightning() {
        double power = 0;
        double electronicPower = this.getTileData().getDouble("ElectronicPower");
        if (electronicPower <= 2000) {
            power = electronicPower + 1000;
        } else {
            power = 3000;
        }
        this.getTileData().putDouble("ElectronicPower", power);
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
