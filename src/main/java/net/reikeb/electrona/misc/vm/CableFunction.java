package net.reikeb.electrona.misc.vm;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollection;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

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
        machineTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", (isBlue ? "electrona/machines" : "electrona/machines_all")));
        cableTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", (isBlue ? "electrona/blue_cable" : "electrona/cable")));

        for (Direction dir : directions) {
            if (cablePower > 0) {
                BlockEntity tileEntity = world.getBlockEntity(pos.relative(dir));
                if (tileEntity == null) continue;
                Block offsetBlock = world.getBlockState(pos.relative(dir)).getBlock();
                if (!(machineTag.contains(offsetBlock) || cableTag.contains(offsetBlock))) continue;

                double machinePower = tileEntity.getTileData().getDouble("ElectronicPower");
                int machineMax = tileEntity.getTileData().getInt("MaxStorage");
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
        machineTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", "electrona/has_water_tank"));
        cableTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", "electrona/water_cable"));

        for (Direction dir : directions) {
            if (cableFLuid > 0) {
                BlockEntity tileEntity = world.getBlockEntity(pos.relative(dir));
                if (tileEntity == null) continue;
                Block offsetBlock = world.getBlockState(pos.relative(dir)).getBlock();
                if (!(machineTag.contains(offsetBlock) || cableTag.contains(offsetBlock))) continue;

                AtomicInteger machineFluid = new AtomicInteger();
                tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                        .ifPresent(cap -> machineFluid.set(cap.getFluidInTank(1).getAmount()));
                AtomicInteger machineMax = new AtomicInteger();
                tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                        .ifPresent(cap -> machineMax.set(cap.getTankCapacity(1)));
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
