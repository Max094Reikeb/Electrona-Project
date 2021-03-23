package net.reikeb.electrona.misc.vm;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollection;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
    public static void cableTransferEnergy(World world, BlockPos pos, Direction[] directions, CompoundNBT cableNBT, double cablePower, int transferPerSecond, Boolean isBlue) {
        double transferPerTick = transferPerSecond * 0.05;

        ITagCollection<Block> tagCollection = BlockTags.getAllTags();
        ITag<Block> machineTag, cableTag;
        machineTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", (isBlue ? "electrona/machines" : "electrona/machines_all")));
        cableTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", (isBlue ? "electrona/blue_cable" : "electrona/cable")));

        for (Direction dir : directions) {
            if (cablePower > 0) {
                TileEntity tileEntity = world.getBlockEntity(pos.relative(dir));
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
                        if (cableNBT.getDouble("ElectronicPower") > 0) {
                            cableNBT.putBoolean("logic", false);
                        } else {
                            cableNBT.putBoolean("logic", true);
                        }
                        tileEntity.getTileData().putBoolean("logic", false);
                    }
                }
            } else {
                cableNBT.putBoolean("logic", false);
                return; // we have no more power
            }
        }
    }
}
