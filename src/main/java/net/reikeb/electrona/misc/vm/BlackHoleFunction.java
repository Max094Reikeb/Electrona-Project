package net.reikeb.electrona.misc.vm;

import net.minecraft.block.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import net.reikeb.electrona.init.*;
import net.reikeb.electrona.utils.GemPower;
import net.reikeb.electrona.world.Gamerules;

public class BlackHoleFunction {

    /**
     * Method that handles the Black Hole expansion
     *
     * @param world      The world
     * @param pos        The position of the Hole
     * @param directions The directions the Hole expands
     */
    public static void HoleProcedure(World world, BlockPos pos, Direction[] directions) {
        if (world.isClientSide) return;
        ITagCollection<Block> tagCollection = BlockTags.getAllTags();
        ITag<Block> stopsHoleTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", "electrona/stops_black_hole"));

        if (world.getLevelData().getGameRules().getBoolean(Gamerules.DO_BLACK_HOLES_EXIST)) {
            for (Direction dir : directions) {
                Block offsetBlock = world.getBlockState(pos.relative(dir)).getBlock();
                BlockPos belowPos = new BlockPos(pos.getX(), (pos.getY() - 1), pos.getZ());
                BlockPos abovePos = new BlockPos(pos.getX(), (pos.getY() + 1), pos.getZ());

                if ((Math.random() >= 0.5) && (!stopsHoleTag.contains(offsetBlock))) {
                    world.setBlock(pos.relative(dir), BlockInit.HOLE.get().defaultBlockState(), 3);
                }
                if (!stopsHoleTag.contains(world.getBlockState(belowPos).getBlock())) {
                    world.setBlock(belowPos, BlockInit.HOLE.get().defaultBlockState(), 3);
                }
                if (!stopsHoleTag.contains(world.getBlockState(abovePos).getBlock())) {
                    world.setBlock(abovePos, BlockInit.HOLE.get().defaultBlockState(), 3);
                }
            }
        }
        if (BlockInit.SINGULARITY.get() != world.getBlockState(pos).getBlock()) {
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }
    }

    /**
     * Method that handles Singularity's particles
     *
     * @param world The world of the Singularity
     * @param pos   The position of the Singularity
     */
    public static void singularityParticles(World world, BlockPos pos) {
        if (world.isClientSide) return;
        double xRadius = 0.1;
        double loop = 0;
        double zRadius = 0.1;
        double particleAmount = 20;
        while (xRadius < 5) {
            particleAmount = xRadius * 20;
            for (int i = 0; i < (int) particleAmount; i++) {
                double tempX = Math.cos((((Math.PI * 2) / (particleAmount)) * (loop)));
                double tempZ = Math.sin((((Math.PI * 2) / (particleAmount)) * (loop)));
                ((ServerWorld) world).sendParticles(ParticleInit.DARK_MATTER.get(), ((pos.getX() + 0.5) + ((tempX) * (xRadius))), pos.getY(),
                        ((pos.getZ() + 0.5) + ((tempZ) * (zRadius))), 1, 0, 0, 0, 0.05);
                loop = loop + 1;
            }
            xRadius = xRadius + 0.1;
            zRadius = zRadius + 0.1;
        }
    }

    /**
     * Method that handles Singularity's delay
     *
     * @param world The world of the Singularity
     * @param pos   The position of the Singularity
     */
    public static void singularityDelay(World world, BlockPos pos) {
        if (world.isClientSide) return;
        ItemStack stack = new ItemStack(ItemInit.COSMIC_GEM.get(), 1);
        stack.getOrCreateTag().putString("power", GemPower.randomPowerId());
        ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        itemEntity.setPickUpDelay(10);
        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        world.addFreshEntity(itemEntity);
    }
}
