package net.reikeb.electrona.misc.vm;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.reikeb.electrona.blockentities.WaterCableBlockEntity;
import net.reikeb.electrona.misc.Tags;
import net.reikeb.maxilib.abs.AbstractEnergyBlockEntity;

import java.util.concurrent.atomic.AtomicInteger;

public class CableFunction {

    /**
     * This method is used by Cables to transfer energy to Machines or other cables
     *
     * @param level             The level the blocks are in
     * @param pos               The blockpos of the Cable
     * @param directions        The directions of the Cable
     * @param cableBlockEntity  The BlockEntity of the Cable
     * @param transferPerSecond The amount of energy transfered each second
     * @param isBlue            Defines if the Cable is a normal Cable or a Blue Cable
     */
    public static <T extends AbstractEnergyBlockEntity> void cableTransferEnergy(Level level, BlockPos pos, Direction[] directions, T cableBlockEntity, int transferPerSecond, Boolean isBlue) {
        double transferPerTick = transferPerSecond * 0.05;
        double cablePower = cableBlockEntity.getElectronicPower();

        TagKey<Block> machineTag = isBlue ? Tags.BLUE_MACHINES : Tags.MACHINES;
        TagKey<Block> cableTag = isBlue ? Tags.BLUE_CABLE : Tags.CABLE;

        for (Direction dir : directions) {
            if (cablePower > 0) {
                BlockEntity blockEntity = level.getBlockEntity(pos.relative(dir));
                BlockState offsetBlockState = level.getBlockState(pos.relative(dir));
                if (blockEntity == null) continue;
                if (!(offsetBlockState.is(machineTag) || offsetBlockState.is(cableTag))) continue;
                if (!(blockEntity instanceof AbstractEnergyBlockEntity energyBlockEntity)) continue;

                double machinePower = energyBlockEntity.getElectronicPower();
                int machineMax = energyBlockEntity.getMaxStorage();
                boolean machineLogic = energyBlockEntity.getLogic();

                if (offsetBlockState.is(machineTag)) {
                    if (machinePower < (machineMax - cablePower)) {
                        EnergyFunction.fillEnergy(energyBlockEntity, transferPerTick);
                        EnergyFunction.drainEnergy(cableBlockEntity, transferPerTick);
                    } else {
                        EnergyFunction.setEnergy(energyBlockEntity, machineMax);
                    }
                } else if (offsetBlockState.is(cableTag)) {
                    if ((!machineLogic) && ((transferPerTick + machinePower) < machineMax)) {
                        EnergyFunction.fillEnergy(energyBlockEntity, transferPerTick);
                        EnergyFunction.drainEnergy(cableBlockEntity, transferPerTick);
                        cableBlockEntity.setLogic(!(cableBlockEntity.getElectronicPower() > 0));
                        energyBlockEntity.setLogic(false);
                    }
                }
            } else {
                cableBlockEntity.setLogic(false);
                return; // we have no more power
            }
        }
    }

    /**
     * This method is used by Cables to transfer fluid to Machines or other cables
     *
     * @param level                 The level the blocks are in
     * @param pos                   The blockpos of the Cable
     * @param directions            The directions of the Cable
     * @param waterCableBlockEntity The Block Entity of the Cable
     * @param cableFLuid            The amount of fluid in the Cable
     * @param transferPerSecond     The amount of fluid transfered each second by the Cable
     */
    public static void cableTransferFluid(Level level, BlockPos pos, Direction[] directions, WaterCableBlockEntity waterCableBlockEntity, double cableFLuid, int transferPerSecond) {
        double transferPerTick = transferPerSecond * 0.05;

        for (Direction dir : directions) {
            if (cableFLuid > 0) {
                BlockEntity blockEntity = level.getBlockEntity(pos.relative(dir));
                BlockState offsetBlockState = level.getBlockState(pos.relative(dir));
                if (blockEntity == null) continue;
                if (!(offsetBlockState.is(Tags.WATER_TANK) || offsetBlockState.is(Tags.WATER_CABLE))) continue;

                AtomicInteger machineFluid = FluidFunction.getFluidAmount(blockEntity);
                AtomicInteger machineMax = FluidFunction.getTankCapacity(blockEntity);
                boolean machineLogic = (blockEntity instanceof WaterCableBlockEntity waterCableBlock) && waterCableBlock.getLogic();

                if (offsetBlockState.is(Tags.WATER_TANK)) {
                    if (machineFluid.get() < (machineMax.get() - cableFLuid)) {
                        FluidFunction.fillWater(blockEntity, (int) transferPerTick);
                        FluidFunction.drainWater(waterCableBlockEntity, (int) transferPerTick);
                    } else {
                        FluidFunction.fillWater(blockEntity, (machineMax.get() - machineFluid.get()));
                    }
                } else if (offsetBlockState.is(Tags.WATER_CABLE)) {
                    if ((!machineLogic) && ((transferPerTick + machineFluid.get()) < machineMax.get())) {
                        FluidFunction.fillWater(blockEntity, (int) transferPerTick);
                        FluidFunction.drainWater(waterCableBlockEntity, (int) transferPerTick);
                        waterCableBlockEntity.setLogic(!(FluidFunction.getFluidAmount(waterCableBlockEntity).get() > 0));
                        if (blockEntity instanceof WaterCableBlockEntity waterCableBlock)
                            waterCableBlock.setLogic(false);
                    }
                }
            } else {
                waterCableBlockEntity.setLogic(false);
                return; // we have no more fluid
            }
        }
    }
}
