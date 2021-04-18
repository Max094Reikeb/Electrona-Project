package net.reikeb.electrona.misc.vm;

import net.minecraft.block.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import net.reikeb.electrona.init.*;
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
     * Method that handles Singularity's spawn
     *
     * @param world The world of the Singularity
     * @param pos   The position of the Singularity
     */
    public static void SingularitySpawn(World world, BlockPos pos) {
        if (world.isClientSide) return;
        ITagCollection<Block> tagCollection = BlockTags.getAllTags();
        ITag<Block> stopsHoleTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", "electrona/stops_black_hole"));

        if (world.getLevelData().getGameRules().getBoolean(Gamerules.DO_BLACK_HOLES_EXIST)) {
            BlockPos closestPos = null;
            BlockPos checkPos;
            for (int n = 0; n < 4; n++) {
                for (int x = pos.getX() - 100; x < pos.getX() + 100; x++) {
                    for (int y = pos.getY() - 100; y < pos.getY() + 100; y++) {
                        for (int z = pos.getZ() - 100; z < pos.getZ() + 100; z++) {
                            checkPos = new BlockPos(x, y, z);
                            if (!stopsHoleTag.contains(world.getBlockState(checkPos).getBlock())) {
                                if (closestPos == null
                                        || ((pos.getX() - checkPos.getX() < pos.getX() - closestPos.getX())
                                        && (pos.getY() - checkPos.getY() < pos.getY() - closestPos.getY())
                                        && (pos.getZ() - checkPos.getZ() < pos.getZ() - closestPos.getZ()))) {
                                    closestPos = checkPos;
                                }
                            }
                        }
                    }
                }
                if (closestPos != null) world.setBlockAndUpdate(closestPos, BlockInit.HOLE.get().defaultBlockState());
            }
        }
    }

    /**
     * Method that handles Singularity's particles
     *
     * @param world The world of the Singularity
     * @param pos   The position of the Singularity
     */
    public static void SingularityParticles(World world, BlockPos pos) {
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
}
