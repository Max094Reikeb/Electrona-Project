package net.reikeb.electrona.blockentities;

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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.reikeb.electrona.misc.vm.CableFunction;
import net.reikeb.maxilib.abs.AbstractFluidBlockEntity;
import net.reikeb.maxilib.intface.FluidInterface;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static net.reikeb.electrona.init.BlockEntityInit.WATER_CABLE_BLOCK_ENTITY;

public class WaterCableBlockEntity extends BlockEntity implements FluidInterface {

    public static final BlockEntityTicker<WaterCableBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    private final AbstractFluidBlockEntity.FluidTankHandler fluidTank = new AbstractFluidBlockEntity.FluidTankHandler(1000, fs -> {
        return fs.getFluid() == Fluids.WATER;
    }) {
        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            setChanged();
            level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 2);
        }
    };
    private boolean cableLogic;

    public WaterCableBlockEntity(BlockPos pos, BlockState state) {
        super(WATER_CABLE_BLOCK_ENTITY.get(), pos, state);
    }

    public <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState state, T t) {
        // We pass energy to blocks around (this part is common to all cables)
        CableFunction.cableTransferFluid(this.level, this.getBlockPos(), this, 100);

        this.setChanged();
    }

    public int getWaterLevel() {
        AtomicInteger amount = new AtomicInteger();
        this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                .ifPresent(cap -> amount.set(cap.getFluidInTank(1).getAmount()));
        return amount.get();
    }

    public void setWaterLevel(int amount) {
        this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                .ifPresent(cap -> cap.drain(getWaterLevel(), IFluidHandler.FluidAction.EXECUTE));
        this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                .ifPresent(cap -> cap.fill(new FluidStack(Fluids.WATER, amount), IFluidHandler.FluidAction.EXECUTE));
    }

    public int getMaxCapacity() {
        AtomicInteger capacity = new AtomicInteger();
        this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                .ifPresent(cap -> capacity.set(cap.getTankCapacity(1)));
        return capacity.get();
    }

    public boolean getLogic() {
        return this.cableLogic;
    }

    public void setLogic(boolean logic) {
        this.cableLogic = logic;
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
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putBoolean("logic", this.cableLogic);
        compound.put("fluidTank", fluidTank.serializeNBT());
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> fluidTank).cast());
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
