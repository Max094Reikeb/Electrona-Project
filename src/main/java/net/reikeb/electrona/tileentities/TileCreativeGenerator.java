package net.reikeb.electrona.tileentities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tileentity.*;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.*;

import net.reikeb.electrona.misc.vm.EnergyFunction;

import static net.reikeb.electrona.init.TileEntityInit.*;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;

public class TileCreativeGenerator extends BlockEntity implements TickableBlockEntity {

    private double electronicPower;
    private int maxStorage;

    public TileCreativeGenerator() {
        super(TILE_CREATIVE_GENERATOR.get());
    }

    @Override
    public void tick() {
        // We get the variables
        Level world = this.level;
        BlockPos blockPos = this.getBlockPos();

        // We get the NBT Tags
        this.getTileData().putInt("MaxStorage", 999999999);
        this.getTileData().putDouble("ElectronicPower", 999999999);
        double electronicPower = this.getTileData().getDouble("ElectronicPower");

        if (world != null) { // Avoid NullPointerExceptions

            this.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(cap ->
                    cap.receiveEnergy(999999999, false));

            // We pass energy to blocks around (this part is common to all generators)
            EnergyFunction.generatorTransferEnergy(world, blockPos, Direction.values(), this.getTileData(), 20, electronicPower, true);
        }
    }

    @Override
    public void load(BlockState blockState, CompoundTag compound) {
        super.load(blockState, compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        if (compound.contains("energyStorage")) {
            CapabilityEnergy.ENERGY.readNBT(energyStorage, null, compound.get("energyStorage"));
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound = super.save(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.put("energyStorage", CapabilityEnergy.ENERGY.writeNBT(energyStorage, null));
        return compound;
    }

    private final EnergyStorage energyStorage = new EnergyStorage(999999999, 999999999, 999999999, 999999999) {
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
        return super.getCapability(cap, side);
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
