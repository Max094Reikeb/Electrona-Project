package net.reikeb.electrona.utils;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.*;
import net.minecraft.world.World;
import net.minecraft.world.biome.*;
import net.minecraft.world.chunk.IChunk;

import net.reikeb.electrona.Electrona;

import java.util.*;

public class ElectronaUtils {

    /**
     * Method to rotate a VoxelShape around an axis
     *
     * @param from  Origin Direction
     * @param to    Destination Direction
     * @param shape The VoxelShape to rotate
     * @return The rotated VoxelShape
     */
    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, VoxelShapes.empty()};

        int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.or(buffer[1], VoxelShapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = VoxelShapes.empty();
        }

        return buffer[0];
    }

    /**
     * Method that gets the RayTraceResult of where an entity looks at
     *
     * @param entity        The entity
     * @param range         The range
     * @param height        The speed
     * @param includeFluids Defines if fluids count
     * @return The RayTraceResult
     */
    public static RayTraceResult lookAt(Entity entity, double range, float height, boolean includeFluids) {
        Vector3d vector3d = entity.getEyePosition(height);
        Vector3d vector3d1 = entity.getViewVector(height);
        Vector3d vector3d2 = vector3d.add(vector3d1.x * range, vector3d1.y * range, vector3d1.z * range);
        return entity.level.clip(new RayTraceContext(vector3d, vector3d2, RayTraceContext.BlockMode.OUTLINE, includeFluids ? RayTraceContext.FluidMode.ANY : RayTraceContext.FluidMode.NONE, entity));
    }

    public static class Gravity {

        public static void applyGravity(World world, BlockPos pos) {
            if (isGravityAffected(world, pos)) {
                FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, world.getBlockState(pos));
                fallingBlockEntity.time = 1;
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                world.addFreshEntity(fallingBlockEntity);
            }
        }

        public static boolean isGravityAffected(World world, BlockPos pos) {
            if (world == null) return false;
            Block block = world.getBlockState(pos).getBlock();
            boolean flag1 = isAir(world, pos);
            boolean flag2 = (block instanceof LeavesBlock) || (block instanceof TorchBlock) || (block instanceof LanternBlock) || (block instanceof LeverBlock) || (block == Blocks.BEDROCK) || (block instanceof FlowingFluidBlock);
            boolean flag3 = world.isEmptyBlock(pos.below()) || FallingBlock.isFree(world.getBlockState(pos.below()));
            boolean flag4 = isSupport(world, pos);
            boolean flag5 = staysAttached(world, pos);
            return ((!flag1) && (!flag2) && (pos.getY() > 0) && flag3 && (!flag4) && (!flag5));
        }

        private static boolean isSupport(World world, BlockPos pos) {
            if (world == null) return false;
            Block block = world.getBlockState(pos).getBlock();
            if ((block instanceof StairsBlock) || (block instanceof SlabBlock)) {
                return !isAir(world, pos.north()) || !isAir(world, pos.south())
                        || !isAir(world, pos.east()) || !isAir(world, pos.west());
            }
            return false;
        }

        private static boolean staysAttached(World world, BlockPos pos) {
            Block block = world.getBlockState(pos).getBlock();
            if ((block instanceof FenceBlock) || (block instanceof FenceGateBlock)
                    || (block instanceof ChainBlock) || (block instanceof PaneBlock)) {
                return !isAir(world, pos.north()) || !isAir(world, pos.south())
                        || !isAir(world, pos.east()) || !isAir(world, pos.west());
            }
            return false;
        }

        public static boolean isAir(World world, BlockPos pos) {
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

    public static class Biome {

        private static final int WIDTH_BITS = (int) Math.round(Math.log(16.0D) / Math.log(2.0D)) - 2;

        public static void setBiomeAtPos(World world, BlockPos pos, ResourceLocation biome) {
            RegistryKey<net.minecraft.world.biome.Biome> biomeKey = RegistryKey.create(Registry.BIOME_REGISTRY, biome);
            setBiomeAtPos(world, pos, biomeKey);
        }

        public static void setBiomeAtPos(World world, BlockPos pos, RegistryKey<net.minecraft.world.biome.Biome> biomeKey) {
            Optional<MutableRegistry<net.minecraft.world.biome.Biome>> biomeRegistry = world.registryAccess().registry(Registry.BIOME_REGISTRY);
            if (!biomeRegistry.isPresent()) return;
            net.minecraft.world.biome.Biome biome = biomeRegistry.get().get(biomeKey);
            if (biome == null) return;
            BiomeContainer bc = world.getChunk(pos).getBiomes();
            IChunk chunk = world.getChunk(pos);
            if (bc != null && bc.biomes != null) {
                net.minecraft.world.biome.Biome[] biomeArray = bc.biomes;
                int biomeIndex = getBiomeIndex(pos.getX(), pos.getY(), pos.getZ(), 0L);
                if (biomeIndex < biomeArray.length) {
                    biomeArray[biomeIndex] = biome;
                } else {
                    Electrona.LOGGER.error(String.format("Failed to set biome at pos: %s; to biome: %s", pos, biome));
                }
            }
            chunk.setUnsaved(true);
        }

        private static int getBiomeIndex(int x, int y, int z, long seed) {
            int i = x - 2;
            int j = y - 2;
            int k = z - 2;
            int l = i >> 2;
            int i1 = j >> 2;
            int j1 = k >> 2;
            double d0 = (double) (i & 3) / 4.0D;
            double d1 = (double) (j & 3) / 4.0D;
            double d2 = (double) (k & 3) / 4.0D;
            double[] adouble = new double[8];

            for (int k1 = 0; k1 < 8; ++k1) {
                boolean flag = (k1 & 4) == 0;
                boolean flag1 = (k1 & 2) == 0;
                boolean flag2 = (k1 & 1) == 0;
                int l1 = flag ? l : l + 1;
                int i2 = flag1 ? i1 : i1 + 1;
                int j2 = flag2 ? j1 : j1 + 1;
                double d3 = flag ? d0 : d0 - 1.0D;
                double d4 = flag1 ? d1 : d1 - 1.0D;
                double d5 = flag2 ? d2 : d2 - 1.0D;
                adouble[k1] = func_226845_a_(seed, l1, i2, j2, d3, d4, d5);
            }

            int k2 = 0;
            double d6 = adouble[0];

            for (int l2 = 1; l2 < 8; ++l2) {
                if (d6 > adouble[l2]) {
                    k2 = l2;
                    d6 = adouble[l2];
                }
            }

            int i3 = (k2 & 4) == 0 ? l : l + 1;
            int j3 = (k2 & 2) == 0 ? i1 : i1 + 1;
            int k3 = (k2 & 1) == 0 ? j1 : j1 + 1;

            int arrayIndex = i3 & BiomeContainer.HORIZONTAL_MASK;
            arrayIndex |= (k3 & BiomeContainer.HORIZONTAL_MASK) << WIDTH_BITS;
            return arrayIndex | MathHelper.clamp(j3, 0, BiomeContainer.VERTICAL_MASK) << WIDTH_BITS + WIDTH_BITS;
        }

        private static double func_226845_a_(long p_226845_0_, int p_226845_2_, int p_226845_3_, int p_226845_4_, double p_226845_5_, double p_226845_7_, double p_226845_9_) {
            long lvt_11_1_ = FastRandom.next(p_226845_0_, p_226845_2_);
            lvt_11_1_ = FastRandom.next(lvt_11_1_, p_226845_3_);
            lvt_11_1_ = FastRandom.next(lvt_11_1_, p_226845_4_);
            lvt_11_1_ = FastRandom.next(lvt_11_1_, p_226845_2_);
            lvt_11_1_ = FastRandom.next(lvt_11_1_, p_226845_3_);
            lvt_11_1_ = FastRandom.next(lvt_11_1_, p_226845_4_);
            double d0 = func_226844_a_(lvt_11_1_);
            lvt_11_1_ = FastRandom.next(lvt_11_1_, p_226845_0_);
            double d1 = func_226844_a_(lvt_11_1_);
            lvt_11_1_ = FastRandom.next(lvt_11_1_, p_226845_0_);
            double d2 = func_226844_a_(lvt_11_1_);
            return func_226843_a_(p_226845_9_ + d2) + func_226843_a_(p_226845_7_ + d1) + func_226843_a_(p_226845_5_ + d0);
        }

        private static double func_226844_a_(long p_226844_0_) {
            double d0 = (double) ((int) Math.floorMod(p_226844_0_ >> 24, 1024L)) / 1024.0D;
            return (d0 - 0.5D) * 0.9D;
        }

        private static double func_226843_a_(double p_226843_0_) {
            return p_226843_0_ * p_226843_0_;
        }
    }
}
