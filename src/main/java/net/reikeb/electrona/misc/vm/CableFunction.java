package net.reikeb.electrona.misc.vm;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollection;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.reikeb.electrona.misc.Keys;
import net.reikeb.electrona.tileentities.TileWaterCable;

import java.util.concurrent.atomic.AtomicInteger;

public class CableFunction {

    /**
     * This method is used by Cables to transfer energy to Machines or other cables
     *
     * @param world             The world the blocks are in
     * @param pos               The blockpos of the Cable
     * @param directions        The directions of the Cable
     * @param cableNBT          The NBT Tag of the Cable
     * @param transferPerSecond The amount of energy transfered each second
     * @param isBlue            Defines if the Cable is a normal Cable or a Blue Cable
     */
    public static void cableTransferEnergy(Level world, BlockPos pos, Direction[] directions, CompoundTag cableNBT, double cablePower, int transferPerSecond, Boolean isBlue) {
        double transferPerTick = transferPerSecond * 0.05;

        TagCollection<Block> tagCollection = BlockTags.getAllTags();
        Tag<Block> machineTag, cableTag;
        machineTag = tagCollection.getTagOrEmpty((isBlue ? Keys.BLUE_CABLE_MACHINE_TAG : Keys.CABLE_MACHINE_TAG));
        cableTag = tagCollection.getTagOrEmpty((isBlue ? Keys.BLUE_CABLE_TAG : Keys.CABLE_TAG));

        for (Direction dir : directions) {
            if (cablePower > 0) {
                BlockEntity tileEntity = EnergyFunction.getUtilBlockEntity(world, pos, dir);
                Block offsetBlock = EnergyFunction.getUtilOffsetBlock(world, pos, dir);
                if (tileEntity == null) continue;
                if (!(machineTag.contains(offsetBlock) || cableTag.contains(offsetBlock))) continue;

                double machinePower = EnergyFunction.getUtilMachinePower(world, pos, dir);
                int machineMax = EnergyFunction.getUtilMachineMax(world, pos, dir);
                boolean machineLogic = tileEntity.getTileData().getBoolean("logic");

                if (machineTag.contains(offsetBlock)) {
                    if (machinePower < (machineMax - cablePower)) {
                        tileEntity.getTileData().putDouble("ElectronicPower", (machinePower + transferPerTick));
                        cableNBT.putDouble("ElectronicPower", (cablePower - transferPerTick));
                    } else {
                        tileEntity.getTileData().putDouble("ElectronicPower", machineMax);
                    }
                } else if (cableTag.contains(offsetBlock)) {
                    if ((!machineLogic) && ((transferPerTick + machinePower) < machineMax)) {
                        tileEntity.getTileData().putDouble("ElectronicPower", (machinePower + transferPerTick));
                        cableNBT.putDouble("ElectronicPower", (cablePower - transferPerTick));
                        cableNBT.putBoolean("logic", !(cableNBT.getDouble("ElectronicPower") > 0));
                        tileEntity.getTileData().putBoolean("logic", false);
                    }
                }
            } else {
                cableNBT.putBoolean("logic", false);
                return; // we have no more power
            }
        }
    }

    /**
     * This method is used by Cables to transfer fluid to Machines or other cables
     *
     * @param world             The world the blocks are in
     * @param pos               The blockpos of the Cable
     * @param directions        The directions of the Cable
     * @param cable             The Tile Entity of the Cable
     * @param cableFLuid        The amount of fluid in the Cable
     * @param transferPerSecond The amount of fluid transfered each second by the Cable
     */
    public static void cableTransferFluid(Level world, BlockPos pos, Direction[] directions, TileWaterCable cable, double cableFLuid, int transferPerSecond) {
        double transferPerTick = transferPerSecond * 0.05;

        TagCollection<Block> tagCollection = BlockTags.getAllTags();
        Tag<Block> machineTag, cableTag;
        machineTag = tagCollection.getTagOrEmpty(Keys.HAS_WATER_TANK_TAG);
        cableTag = tagCollection.getTagOrEmpty(Keys.WATER_CABLE_TAG);

        for (Direction dir : directions) {
            if (cableFLuid > 0) {
                BlockEntity tileEntity = FluidFunction.getUtilBlockEntity(world, pos, dir);
                Block offsetBlock = FluidFunction.getUtilOffsetBlock(world, pos, dir);
                if (tileEntity == null) continue;
                if (!(machineTag.contains(offsetBlock) || cableTag.contains(offsetBlock))) continue;

                AtomicInteger machineFluid = FluidFunction.getUtilMachineLevel(world, pos, dir);
                AtomicInteger machineMax = FluidFunction.getUtilMachineCapacity(world, pos, dir);
                boolean machineLogic = tileEntity.getTileData().getBoolean("logic");

                if (machineTag.contains(offsetBlock)) {
                    if (machineFluid.get() < (machineMax.get() - cableFLuid)) {
                        FluidFunction.fillWater(tileEntity, (int) transferPerTick);
                        FluidFunction.drainWater(cable, (int) transferPerTick);
                    } else {
                        FluidFunction.fillWater(tileEntity, (machineMax.get() - machineFluid.get()));
                    }
                } else if (cableTag.contains(offsetBlock)) {
                    if ((!machineLogic) && ((transferPerTick + machineFluid.get()) < machineMax.get())) {
                        FluidFunction.fillWater(tileEntity, (int) transferPerTick);
                        FluidFunction.drainWater(cable, (int) transferPerTick);
                        cable.getTileData().putBoolean("logic", !(cable.getTileData().getDouble("ElectronicPower") > 0));
                        tileEntity.getTileData().putBoolean("logic", false);
                    }
                }
            } else {
                cable.getTileData().putBoolean("logic", false);
                return; // we have no more fluid
            }
        }
    }
}
