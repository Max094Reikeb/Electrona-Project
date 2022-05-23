package net.reikeb.electrona.misc.vm;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.reikeb.electrona.misc.Tags;
import net.reikeb.maxilib.intface.EnergyInterface;
import net.reikeb.maxilib.intface.FluidInterface;

public class CableFunction {

    /**
     * This method is used by Cables to transfer energy to Machines or other cables
     *
     * @param level             The level the blocks are in
     * @param pos               The blockpos of the Cable
     * @param cable             The BlockEntity of the Cable
     * @param transferPerSecond The amount of energy transferred each second
     * @param isBlue            Defines if the Cable is a normal Cable or a Blue Cable
     */
    public static <T extends EnergyInterface> void cableTransferEnergy(Level level, BlockPos pos, T cable, int transferPerSecond, Boolean isBlue) {
        double transferPerTick = transferPerSecond * 0.05;
        double cablePower = cable.getEnergy();

        TagKey<Block> machineTag = isBlue ? Tags.BLUE_MACHINES : Tags.MACHINES;
        TagKey<Block> cableTag = isBlue ? Tags.BLUE_CABLE : Tags.CABLE;

        for (Direction dir : Direction.values()) {
            if (cablePower > 0) {
                BlockEntity blockEntity = level.getBlockEntity(pos.relative(dir));
                BlockState offsetBlockState = level.getBlockState(pos.relative(dir));
                if (blockEntity == null) continue;
                if (!(offsetBlockState.is(machineTag) || offsetBlockState.is(cableTag))) continue;
                if (!(blockEntity instanceof EnergyInterface energyBlockEntity)) continue;

                double machinePower = energyBlockEntity.getEnergy();
                int machineMax = energyBlockEntity.getMaxEnergy();
                boolean machineLogic = energyBlockEntity.getEnergyLogic();

                if (offsetBlockState.is(machineTag)) {
                    if (machinePower < (machineMax - cablePower)) {
                        EnergyInterface.fillEnergy(energyBlockEntity, transferPerTick);
                        EnergyInterface.drainEnergy(cable, transferPerTick);
                    } else {
                        energyBlockEntity.setEnergy(machineMax);
                    }
                } else if (offsetBlockState.is(cableTag)) {
                    if ((!machineLogic) && ((transferPerTick + machinePower) < machineMax)) {
                        EnergyInterface.fillEnergy(energyBlockEntity, transferPerTick);
                        EnergyInterface.drainEnergy(cable, transferPerTick);
                        cable.setEnergyLogic(!(cable.getEnergy() > 0));
                        energyBlockEntity.setEnergyLogic(false);
                    }
                }
            } else {
                cable.setEnergyLogic(false);
                return; // we have no more power
            }
        }
    }

    /**
     * This method is used by Cables to transfer fluid to Machines or other cables
     *
     * @param level             The level the blocks are in
     * @param pos               The blockpos of the Cable
     * @param fluidCable        The Block Entity of the Cable
     * @param transferPerSecond The amount of fluid transferred each second by the Cable
     */
    public static <T extends FluidInterface> void cableTransferFluid(Level level, BlockPos pos, T fluidCable, int transferPerSecond) {
        double transferPerTick = transferPerSecond * 0.05;
        int cableFluid = fluidCable.getWaterLevel();

        for (Direction dir : Direction.values()) {
            if (cableFluid > 0) {
                BlockEntity blockEntity = level.getBlockEntity(pos.relative(dir));
                BlockState offsetBlockState = level.getBlockState(pos.relative(dir));
                if (blockEntity == null) continue;
                if (!(offsetBlockState.is(Tags.WATER_TANK) || offsetBlockState.is(Tags.WATER_CABLE))) continue;
                if (!(blockEntity instanceof FluidInterface fluidBlockEntity)) continue;

                int machineFluid = fluidBlockEntity.getWaterLevel();
                int machineMax = fluidBlockEntity.getTankCapacity();
                boolean machineLogic = fluidBlockEntity.getFluidLogic();

                if (offsetBlockState.is(Tags.WATER_TANK)) {
                    if (machineFluid < (machineMax - cableFluid)) {
                        FluidInterface.fillWater(fluidBlockEntity, (int) transferPerTick);
                        FluidInterface.drainWater(fluidCable, (int) transferPerTick);
                    } else {
                        FluidInterface.fillWater(fluidBlockEntity, (machineMax - machineFluid));
                    }
                } else if (offsetBlockState.is(Tags.WATER_CABLE)) {
                    if ((!machineLogic) && ((transferPerTick + machineFluid) < machineMax)) {
                        FluidInterface.fillWater(fluidBlockEntity, (int) transferPerTick);
                        FluidInterface.drainWater(fluidCable, (int) transferPerTick);
                        fluidCable.setFluidLogic(!(fluidCable.getWaterLevel() > 0));
                        fluidBlockEntity.setFluidLogic(false);
                    }
                }
            } else {
                fluidCable.setFluidLogic(false);
                return; // we have no more fluid
            }
        }
    }
}
