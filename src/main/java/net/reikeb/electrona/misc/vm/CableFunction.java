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

import net.reikeb.electrona.blockentities.WaterCableBlockEntity;
import net.reikeb.electrona.misc.Keys;

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
                BlockEntity blockEntity = world.getBlockEntity(pos.relative(dir));
                Block offsetBlock = world.getBlockState(pos.relative(dir)).getBlock();
                if (blockEntity == null) continue;
                if (!(machineTag.contains(offsetBlock) || cableTag.contains(offsetBlock))) continue;

                double machinePower = blockEntity.getTileData().getDouble("ElectronicPower");
                int machineMax = blockEntity.getTileData().getInt("MaxStorage");
                boolean machineLogic = blockEntity.getTileData().getBoolean("logic");

                if (machineTag.contains(offsetBlock)) {
                    if (machinePower < (machineMax - cablePower)) {
                        blockEntity.getTileData().putDouble("ElectronicPower", (machinePower + transferPerTick));
                        cableNBT.putDouble("ElectronicPower", (cablePower - transferPerTick));
                    } else {
                        blockEntity.getTileData().putDouble("ElectronicPower", machineMax);
                    }
                } else if (cableTag.contains(offsetBlock)) {
                    if ((!machineLogic) && ((transferPerTick + machinePower) < machineMax)) {
                        blockEntity.getTileData().putDouble("ElectronicPower", (machinePower + transferPerTick));
                        cableNBT.putDouble("ElectronicPower", (cablePower - transferPerTick));
                        cableNBT.putBoolean("logic", !(cableNBT.getDouble("ElectronicPower") > 0));
                        blockEntity.getTileData().putBoolean("logic", false);
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
     * @param world                 The world the blocks are in
     * @param pos                   The blockpos of the Cable
     * @param directions            The directions of the Cable
     * @param waterCableBlockEntity The Block Entity of the Cable
     * @param cableFLuid            The amount of fluid in the Cable
     * @param transferPerSecond     The amount of fluid transfered each second by the Cable
     */
    public static void cableTransferFluid(Level world, BlockPos pos, Direction[] directions, WaterCableBlockEntity waterCableBlockEntity, double cableFLuid, int transferPerSecond) {
        double transferPerTick = transferPerSecond * 0.05;

        TagCollection<Block> tagCollection = BlockTags.getAllTags();
        Tag<Block> machineTag, cableTag;
        machineTag = tagCollection.getTagOrEmpty(Keys.HAS_WATER_TANK_TAG);
        cableTag = tagCollection.getTagOrEmpty(Keys.WATER_CABLE_TAG);

        for (Direction dir : directions) {
            if (cableFLuid > 0) {
                BlockEntity blockEntity = world.getBlockEntity(pos.relative(dir));
                Block offsetBlock = world.getBlockState(pos.relative(dir)).getBlock();
                if (blockEntity == null) continue;
                if (!(machineTag.contains(offsetBlock) || cableTag.contains(offsetBlock))) continue;

                AtomicInteger machineFluid = FluidFunction.getFluidAmount(blockEntity);
                AtomicInteger machineMax = FluidFunction.getTankCapacity(blockEntity);
                boolean machineLogic = blockEntity.getTileData().getBoolean("logic");

                if (machineTag.contains(offsetBlock)) {
                    if (machineFluid.get() < (machineMax.get() - cableFLuid)) {
                        FluidFunction.fillWater(blockEntity, (int) transferPerTick);
                        FluidFunction.drainWater(waterCableBlockEntity, (int) transferPerTick);
                    } else {
                        FluidFunction.fillWater(blockEntity, (machineMax.get() - machineFluid.get()));
                    }
                } else if (cableTag.contains(offsetBlock)) {
                    if ((!machineLogic) && ((transferPerTick + machineFluid.get()) < machineMax.get())) {
                        FluidFunction.fillWater(blockEntity, (int) transferPerTick);
                        FluidFunction.drainWater(waterCableBlockEntity, (int) transferPerTick);
                        waterCableBlockEntity.getTileData().putBoolean("logic", !(waterCableBlockEntity.getTileData().getDouble("ElectronicPower") > 0));
                        blockEntity.getTileData().putBoolean("logic", false);
                    }
                }
            } else {
                waterCableBlockEntity.getTileData().putBoolean("logic", false);
                return; // we have no more fluid
            }
        }
    }
}
