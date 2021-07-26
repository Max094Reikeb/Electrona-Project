package net.reikeb.electrona.tileentities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tileentity.*;
import net.minecraft.core.Direction;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import net.reikeb.electrona.misc.vm.CableFunction;

import static net.reikeb.electrona.init.TileEntityInit.*;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;

public class TileWaterCable extends BlockEntity implements TickableBlockEntity {

    private boolean cableLogic;

    public TileWaterCable() {
        super(TILE_WATER_CABLE.get());
    }

    @Override
    public void tick() {

        // We pass energy to blocks around (this part is common to all cables)
        CableFunction.cableTransferFluid(this.level, this.getBlockPos(), Direction.values(), this, this.fluidTank.getFluidAmount(), 100);

        this.setChanged();
    }

    @Override
    public void load(BlockState blockState, CompoundTag compound) {
        super.load(blockState, compound);
        this.cableLogic = compound.getBoolean("logic");
        if (compound.get("fluidTank") != null)
            CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(fluidTank, null, compound.get("fluidTank"));
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound = super.save(compound);
        compound.putBoolean("logic", this.cableLogic);
        compound.put("fluidTank", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(fluidTank, null));
        return compound;
    }

    private final FluidTank fluidTank = new FluidTank(1000, fs -> {
        return fs.getFluid() == Fluids.WATER;
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
        return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> fluidTank).cast());
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
