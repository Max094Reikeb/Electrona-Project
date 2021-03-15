package net.reikeb.electrona.utils;

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

public class ElectronaUtils {

    public static void generatorTransferEnergy(World world, BlockPos pos, Direction[] directions, CompoundNBT generatorNBT, int transferPerSecond, double generatorPower, Boolean isGenerator) {
        double transferPerTick = transferPerSecond * 0.05;

        ITagCollection<Block> tagCollection = BlockTags.getAllTags();
        ITag<Block> machineTag, cableTag;
        if (isGenerator) {
            machineTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", "electrona/machines_all"));
            cableTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", "electrona/cable"));
        } else {
            machineTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", "electrona/machines"));
            cableTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", "electrona/blue_cable"));
        }

        for (Direction dir : directions) {
            if (generatorPower <= 0) return; // we have no more power

            TileEntity tileEntity = world.getBlockEntity(pos.relative(dir));
            if (tileEntity == null) continue;
            Block offsetBlock = world.getBlockState(pos.relative(dir)).getBlock();
            if (!(machineTag.contains(offsetBlock) || cableTag.contains(offsetBlock))) continue;

            double machinePower = tileEntity.getTileData().getDouble("ElectronicPower");
            double machineMax = tileEntity.getTileData().getDouble("MaxStorage");
            double headroom = machineMax - machinePower;
            double actualTransfer = Math.min(Math.min(transferPerTick, generatorPower), headroom);

            generatorNBT.putDouble("ElectronicPower", (generatorPower -= actualTransfer));
            tileEntity.getTileData().putDouble("ElectronicPower", (machinePower + actualTransfer));
        }
    }
}
