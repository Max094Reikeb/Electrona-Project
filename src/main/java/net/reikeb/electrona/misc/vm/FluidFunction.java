package net.reikeb.electrona.misc.vm;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.reikeb.electrona.misc.Tags;

import java.util.concurrent.atomic.AtomicInteger;

public class FluidFunction {

    /**
     * This method is used by fluid generators to transfer fluids to Machines.
     *
     * @param world             The world of the blocks
     * @param pos               The blockpos of the generator
     * @param directions        The directions of the generator
     * @param generatorBE       The BlockEntity of the generator
     * @param generatorLevel    The current fluid amount of the generator
     * @param transferPerSecond The amount of fluid transfered per second
     */
    public static void generatorTransferFluid(Level world, BlockPos pos, Direction[] directions, BlockEntity generatorBE, int generatorLevel, int transferPerSecond) {
        double transferPerTick = transferPerSecond * 0.05;

        for (Direction dir : directions) {
            if (generatorLevel <= 0) return; // we have no more fluid

            BlockEntity blockEntity = world.getBlockEntity(pos.relative(dir));
            BlockState offsetBlockState = world.getBlockState(pos.relative(dir));
            if (blockEntity == null) continue;
            if (!(offsetBlockState.is(Tags.WATER_TANK) || offsetBlockState.is(Tags.WATER_CABLE))) continue;

            AtomicInteger machineLevel = getFluidAmount(blockEntity);
            AtomicInteger machineCapacity = getTankCapacity(blockEntity);
            double headroom = machineCapacity.get() - machineLevel.get();
            double actualTransfer = Math.min(Math.min(transferPerTick, generatorLevel), headroom);

            drainWater(generatorBE, (int) actualTransfer);
            fillWater(blockEntity, (int) actualTransfer);
        }
    }

    /**
     * Small method to drain water from a BlockEntity
     *
     * @param be     The BlockEntity we drain water from
     * @param amount The amount of water drained
     */
    public static void drainWater(BlockEntity be, int amount) {
        be.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                .ifPresent(cap -> cap.drain(amount, IFluidHandler.FluidAction.EXECUTE));
    }

    /**
     * Small method to fill water into a BlockEntity
     *
     * @param be     The BlockEntity we give water to
     * @param amount The amount of water given
     */
    public static void fillWater(BlockEntity be, int amount) {
        be.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                .ifPresent(cap -> cap.fill(new FluidStack(Fluids.WATER, amount), IFluidHandler.FluidAction.EXECUTE));
    }

    /**
     * Small method to get the amount of fluid in a BlockEntity
     *
     * @param be The BlockEntity to check
     * @return The amount of fluid
     */
    public static AtomicInteger getFluidAmount(BlockEntity be) {
        AtomicInteger amount = new AtomicInteger();
        be.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                .ifPresent(cap -> amount.set(cap.getFluidInTank(1).getAmount()));
        return amount;
    }

    /**
     * Small method to get the capacity of a tank in a BlockEntity
     *
     * @param be The BlockEntity to check
     * @return The tank capacity
     */
    public static AtomicInteger getTankCapacity(BlockEntity be) {
        AtomicInteger capacity = new AtomicInteger();
        be.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                .ifPresent(cap -> capacity.set(cap.getTankCapacity(1)));
        return capacity;
    }
}
