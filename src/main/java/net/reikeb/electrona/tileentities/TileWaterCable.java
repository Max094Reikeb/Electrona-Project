package net.reikeb.electrona.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import net.reikeb.electrona.misc.vm.CableFunction;
import net.reikeb.electrona.utils.FluidTankHandler;

import static net.reikeb.electrona.init.TileEntityInit.TILE_WATER_CABLE;

public class TileWaterCable extends BlockEntity {

    public static final BlockEntityTicker<TileWaterCable> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    private boolean cableLogic;

    public TileWaterCable(BlockPos pos, BlockState state) {
        super(TILE_WATER_CABLE.get(), pos, state);
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
        // We pass energy to blocks around (this part is common to all cables)
        CableFunction.cableTransferFluid(this.level, this.getBlockPos(), Direction.values(), this, this.fluidTank.getFluidAmount(), 100);

        this.setChanged();
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.cableLogic = compound.getBoolean("logic");
        if (compound.get("fluidTank") != null) {
            fluidTank.readFromNBT((CompoundTag) compound.get("fluidTank"));
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound = super.save(compound);
        compound.putBoolean("logic", this.cableLogic);
        compound.put("fluidTank", fluidTank.serializeNBT());
        return compound;
    }

    private final FluidTankHandler fluidTank = new FluidTankHandler(1000, fs -> {
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
        this.load(pkt.getTag());
    }
}
