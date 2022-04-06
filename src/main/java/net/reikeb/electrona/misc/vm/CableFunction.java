package net.reikeb.electrona.misc.vm;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.reikeb.electrona.blockentities.AbstractEnergyBlockEntity;
import net.reikeb.electrona.blockentities.WaterCableBlockEntity;
import net.reikeb.electrona.misc.Tags;

import java.util.concurrent.atomic.AtomicInteger;

public class CableFunction {

    /**
     * This method is used by Cables to transfer energy to Machines or other cables
     *
     * @param world             The world the blocks are in
     * @param pos               The blockpos of the Cable
     * @param directions        The directions of the Cable
     * @param cableBlockEntity  The BlockEntity of the Cable
     * @param transferPerSecond The amount of energy transfered each second
     * @param isBlue            Defines if the Cable is a normal Cable or a Blue Cable
     */
    public static <T extends AbstractEnergyBlockEntity> void cableTransferEnergy(Level world, BlockPos pos, Direction[] directions, T cableBlockEntity, int transferPerSecond, Boolean isBlue) {
        double transferPerTick = transferPerSecond * 0.05;
        double cablePower = cableBlockEntity.getElectronicPower();

        Tag<Block> machineTag = isBlue ? Tags.BLUE_MACHINES : Tags.MACHINES;
        Tag<Block> cableTag = isBlue ? Tags.BLUE_CABLE : Tags.CABLE;

        for (Direction dir : directions) {
            if (cablePower > 0) {
                BlockEntity blockEntity = world.getBlockEntity(pos.relative(dir));
                Block offsetBlock = world.getBlockState(pos.relative(dir)).getBlock();
                if (blockEntity == null) continue;
                if (!(machineTag.contains(offsetBlock) || cableTag.contains(offsetBlock))) continue;
                if (!(blockEntity instanceof AbstractEnergyBlockEntity energyBlockEntity)) continue;

                double machinePower = energyBlockEntity.getElectronicPower();
                int machineMax = energyBlockEntity.getMaxStorage();
                boolean machineLogic = energyBlockEntity.getLogic();

                if (machineTag.contains(offsetBlock)) {
                    if (machinePower < (machineMax - cablePower)) {
                        energyBlockEntity.setElectronicPower(machinePower + transferPerTick);
                        cableBlockEntity.setElectronicPower(cablePower - transferPerTick);
                    } else {
                        energyBlockEntity.setElectronicPower(machineMax);
                    }
                } else if (cableTag.contains(offsetBlock)) {
                    if ((!machineLogic) && ((transferPerTick + machinePower) < machineMax)) {
                        energyBlockEntity.setElectronicPower(machinePower + transferPerTick);
                        cableBlockEntity.setElectronicPower(cablePower -= transferPerTick);
                        cableBlockEntity.setLogic(!(cablePower > 0));
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
     * @param world                 The world the blocks are in
     * @param pos                   The blockpos of the Cable
     * @param directions            The directions of the Cable
     * @param waterCableBlockEntity The Block Entity of the Cable
     * @param cableFLuid            The amount of fluid in the Cable
     * @param transferPerSecond     The amount of fluid transfered each second by the Cable
     */
    public static void cableTransferFluid(Level world, BlockPos pos, Direction[] directions, WaterCableBlockEntity waterCableBlockEntity, double cableFLuid, int transferPerSecond) {
        double transferPerTick = transferPerSecond * 0.05;

        for (Direction dir : directions) {
            if (cableFLuid > 0) {
                BlockEntity blockEntity = world.getBlockEntity(pos.relative(dir));
                Block offsetBlock = world.getBlockState(pos.relative(dir)).getBlock();
                if (blockEntity == null) continue;
                if (!(Tags.WATER_TANK.contains(offsetBlock) || Tags.WATER_CABLE.contains(offsetBlock))) continue;

                AtomicInteger machineFluid = FluidFunction.getFluidAmount(blockEntity);
                AtomicInteger machineMax = FluidFunction.getTankCapacity(blockEntity);
                boolean machineLogic = (blockEntity instanceof WaterCableBlockEntity waterCableBlock) && waterCableBlock.getLogic();

                if (Tags.WATER_TANK.contains(offsetBlock)) {
                    if (machineFluid.get() < (machineMax.get() - cableFLuid)) {
                        FluidFunction.fillWater(blockEntity, (int) transferPerTick);
                        FluidFunction.drainWater(waterCableBlockEntity, (int) transferPerTick);
                    } else {
                        FluidFunction.fillWater(blockEntity, (machineMax.get() - machineFluid.get()));
                    }
                } else if (Tags.WATER_CABLE.contains(offsetBlock)) {
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
