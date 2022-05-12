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
import net.reikeb.maxilib.intface.IEnergy;
import net.reikeb.maxilib.intface.IFluid;

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
        double cablePower = cable.getElectronicPower();

        TagKey<Block> machineTag = isBlue ? Tags.BLUE_MACHINES : Tags.MACHINES;
        TagKey<Block> cableTag = isBlue ? Tags.BLUE_CABLE : Tags.CABLE;

        for (Direction dir : Direction.values()) {
            if (cablePower > 0) {
                BlockEntity blockEntity = level.getBlockEntity(pos.relative(dir));
                BlockState offsetBlockState = level.getBlockState(pos.relative(dir));
                if (blockEntity == null) continue;
                if (!(offsetBlockState.is(machineTag) || offsetBlockState.is(cableTag))) continue;
                if (!(blockEntity instanceof EnergyInterface energyBlockEntity)) continue;

                double machinePower = energyBlockEntity.getElectronicPower();
                int machineMax = energyBlockEntity.getMaxStorage();
                boolean machineLogic = energyBlockEntity.getLogic();

                if (offsetBlockState.is(machineTag)) {
                    if (machinePower < (machineMax - cablePower)) {
                        IEnergy.fillEnergy(energyBlockEntity, transferPerTick);
                        IEnergy.drainEnergy(cable, transferPerTick);
                    } else {
                        IEnergy.setEnergy(energyBlockEntity, machineMax);
                    }
                } else if (offsetBlockState.is(cableTag)) {
                    if ((!machineLogic) && ((transferPerTick + machinePower) < machineMax)) {
                        IEnergy.fillEnergy(energyBlockEntity, transferPerTick);
                        IEnergy.drainEnergy(cable, transferPerTick);
                        cable.setLogic(!(cable.getElectronicPower() > 0));
                        energyBlockEntity.setLogic(false);
                    }
                }
            } else {
                cable.setLogic(false);
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

                int machineFluid = IFluid.getFluidAmount(fluidBlockEntity);
                int machineMax = IFluid.getTankCapacity(fluidBlockEntity);
                boolean machineLogic = fluidBlockEntity.getLogic();

                if (offsetBlockState.is(Tags.WATER_TANK)) {
                    if (machineFluid < (machineMax - cableFluid)) {
                        IFluid.fillWater(fluidBlockEntity, (int) transferPerTick);
                        IFluid.drainWater(fluidCable, (int) transferPerTick);
                    } else {
                        IFluid.fillWater(fluidBlockEntity, (machineMax - machineFluid));
                    }
                } else if (offsetBlockState.is(Tags.WATER_CABLE)) {
                    if ((!machineLogic) && ((transferPerTick + machineFluid) < machineMax)) {
                        IFluid.fillWater(fluidBlockEntity, (int) transferPerTick);
                        IFluid.drainWater(fluidCable, (int) transferPerTick);
                        fluidCable.setLogic(!(fluidCable.getWaterLevel() > 0));
                        fluidBlockEntity.setLogic(false);
                    }
                }
            } else {
                fluidCable.setLogic(false);
                return; // we have no more fluid
            }
        }
    }
}
