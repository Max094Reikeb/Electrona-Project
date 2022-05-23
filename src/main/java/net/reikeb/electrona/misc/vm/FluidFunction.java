package net.reikeb.electrona.misc.vm;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.reikeb.electrona.misc.Tags;
import net.reikeb.maxilib.intface.FluidInterface;

public class FluidFunction {

    /**
     * This method is used by fluid generators to transfer fluids to Machines.
     *
     * @param level                The level of the blocks
     * @param pos                  The blockpos of the generator
     * @param generatorBlockEntity The BlockEntity of the generator
     * @param transferPerSecond    The amount of fluid transfered per second
     */
    public static <T extends FluidInterface> void generatorTransferFluid(Level level, BlockPos pos, T generatorBlockEntity, int transferPerSecond) {
        double transferPerTick = transferPerSecond * 0.05;
        int generatorLevel = generatorBlockEntity.getWaterLevel();

        for (Direction dir : Direction.values()) {
            if (generatorLevel <= 0) return; // we have no more fluid

            BlockEntity blockEntity = level.getBlockEntity(pos.relative(dir));
            BlockState offsetBlockState = level.getBlockState(pos.relative(dir));
            if (blockEntity == null) continue;
            if (!(blockEntity instanceof FluidInterface fluidBlockEntity)) continue;
            if (!(offsetBlockState.is(Tags.WATER_TANK) || offsetBlockState.is(Tags.WATER_CABLE))) continue;

            int machineLevel = fluidBlockEntity.getWaterLevel();
            int machineCapacity = fluidBlockEntity.getTankCapacity();
            double headroom = machineCapacity - machineLevel;
            double actualTransfer = Math.min(Math.min(transferPerTick, generatorLevel), headroom);

            FluidInterface.drainWater(generatorBlockEntity, (int) actualTransfer);
            FluidInterface.fillWater(fluidBlockEntity, (int) actualTransfer);
        }
    }
}
