package net.reikeb.electrona.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;

public class Gravity {

    /**
     * Applies gravity to a block
     *
     * @param world The world of the block
     * @param pos   The position of the block
     */
    public static void applyGravity(Level world, BlockPos pos) {
        if (isGravityAffected(world, pos)) {
            FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, world.getBlockState(pos));
            fallingBlockEntity.time = 1;
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            world.addFreshEntity(fallingBlockEntity);
        }
    }

    /**
     * Checks if a block can be moved by gravity
     *
     * @param world The world of the block
     * @param pos   The position of the block
     * @return true if the block can be moved by gravity
     */
    public static boolean isGravityAffected(Level world, BlockPos pos) {
        if (world == null) return false;
        Block block = world.getBlockState(pos).getBlock();
        boolean flag1 = isAir(world, pos);
        boolean flag2 = (block instanceof LeavesBlock) || (block instanceof TorchBlock) || (block instanceof LeverBlock) || (block == Blocks.BEDROCK) || (block instanceof LiquidBlock);
        boolean flag3 = world.isEmptyBlock(pos.below()) || FallingBlock.isFree(world.getBlockState(pos.below()));
        boolean flag4 = isSupport(world, pos);
        boolean flag5 = staysAttached(world, pos);
        return ((!flag1) && (!flag2) && (pos.getY() > 0) && flag3 && (!flag4) && (!flag5));
    }

    /**
     * Checks if a block is a support (Stairs or Slabs)
     *
     * @param world The world of the block
     * @param pos   The position of the block
     * @return true if the block is considered as a support
     */
    private static boolean isSupport(Level world, BlockPos pos) {
        if (world == null) return false;
        Block block = world.getBlockState(pos).getBlock();
        if ((block instanceof StairBlock) || (block instanceof SlabBlock)) {
            return !isAir(world, pos.north()) || !isAir(world, pos.south())
                    || !isAir(world, pos.east()) || !isAir(world, pos.west());
        }
        return false;
    }

    /**
     * Checks if a block can stay attached to another block
     *
     * @param world The world of the block
     * @param pos   The position of the block
     * @return true if the block can stay attached
     */
    private static boolean staysAttached(Level world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        if ((block instanceof FenceBlock) || (block instanceof FenceGateBlock)
                || (block instanceof ChainBlock) || (block instanceof IronBarsBlock)) {
            return !isAir(world, pos.north()) || !isAir(world, pos.south())
                    || !isAir(world, pos.east()) || !isAir(world, pos.west());
        }
        return false;
    }

    /**
     * Checks if a block is air
     *
     * @param world The world of the block
     * @param pos   The position of the block
     * @return true if the block is air
     */
    public static boolean isAir(Level world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        return (block == Blocks.AIR) || (block == Blocks.VOID_AIR) || (block == Blocks.CAVE_AIR);
    }

    /*
    private static boolean isAttachedToNormalBlock(World world, BlockPos pos, boolean checkNextBlock) {
        for (Direction dir : Direction.values()) {
            BlockPos otherPos = pos.relative(dir);
            if ((!isSupport(world, otherPos)) && (!staysAttached(world, otherPos))) {
                return true;
            } else {
                if (checkNextBlock) {
                    if (!isAir(world, otherPos)) {
                        if (isAttachedToNormalBlock(world, otherPos, false)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }
    */
}
