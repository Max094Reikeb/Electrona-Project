package net.reikeb.electrona.misc.vm;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollection;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.reikeb.electrona.init.EnchantmentInit;

public class EnchantmentFunction {

    /**
     * Main Lumberjack enchantment function
     *
     * @param player     The player who cuts the tree
     * @param world      The world
     * @param pos        The position of the broken block
     * @param directions The directions to get neighbour blocks
     */
    public static void lumberjackMain(ServerPlayerEntity player, World world, BlockPos pos, Direction[] directions) {
        ITagCollection<Block> tagCollection = BlockTags.getAllTags();
        ITag<Block> logTag;
        logTag = tagCollection.getTagOrEmpty(new ResourceLocation("minecraft:logs"));
        if ((EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.LUMBERJACK.get(), player.getMainHandItem()) > 0)
                && (logTag.contains(world.getBlockState(pos).getBlock()))) {
            for (Direction dir : directions) {
                BlockPos otherPos = pos.relative(dir);
                if (logTag.contains(world.getBlockState(otherPos).getBlock())) {
                    Block.dropResources(world.getBlockState(otherPos), world, otherPos);
                    world.destroyBlock(otherPos, false);
                    lumberjackDef(world, otherPos, directions);
                }
            }
        }
    }

    /**
     * Second Lumberjack enchantment function
     *
     * @param world      The world
     * @param pos        The position of the neighbour blocks
     * @param directions The directions to get the neighbour blocks
     */
    public static void lumberjackDef(World world, BlockPos pos, Direction[] directions) {
        ITagCollection<Block> tagCollection = BlockTags.getAllTags();
        ITag<Block> logTag;
        logTag = tagCollection.getTagOrEmpty(new ResourceLocation("minecraft:logs"));

        for (Direction dir : directions) {
            BlockPos otherPos = pos.relative(dir);
            if (logTag.contains(world.getBlockState(otherPos).getBlock())) {
                Block.dropResources(world.getBlockState(otherPos), world, otherPos);
                world.destroyBlock(otherPos, false);
                lumberjackDef(world, otherPos, directions);
            }
        }
    }
}
