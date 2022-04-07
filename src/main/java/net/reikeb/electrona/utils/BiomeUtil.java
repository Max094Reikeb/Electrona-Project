package net.reikeb.electrona.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.LinearCongruentialGenerator;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraftforge.network.PacketDistributor;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.BiomeSingleUpdatePacket;

public class BiomeUtil {

    private static final int WIDTH_BITS = (int) Math.round(Math.log(16.0D) / Math.log(2.0D)) - 2;
    private static final int HEIGHT_BITS = (int) Math.round(Math.log(256.0D) / Math.log(2.0D)) - 2;
    private static final int VERTICAL_MASK = (1 << HEIGHT_BITS) - 1;
    private static final int HORIZONTAL_MASK = (1 << WIDTH_BITS) - 1;

    /**
     * Modifies the biome at a location by a biome's ResourceLocation
     *
     * @param world The world of the biome
     * @param pos   The location of the biome
     * @param biome The biome's ResourceLocation to replace with
     */
    public static void setBiomeAtPos(Level world, BlockPos pos, ResourceLocation biome) {
        if (world.isClientSide) return;
        ResourceKey<Biome> biomeKey = ResourceKey.create(Registry.BIOME_REGISTRY, biome);
        setBiomeKeyAtPos(world, pos, biomeKey);
        NetworkManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new BiomeSingleUpdatePacket(pos, biome));
    }

    /**
     * Modifies the biome at a location by a biome's ResourceKey
     *
     * @param world    The world of the biome
     * @param pos      The location of the biome
     * @param biomeKey The biome's ResourceKey to replace with
     */
    public static void setBiomeKeyAtPos(Level world, BlockPos pos, ResourceKey<Biome> biomeKey) {
        Biome biome = world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(biomeKey);
        if (biome == null) return;
        ChunkAccess chunkAccess = world.getChunk(pos);
        LevelChunkSection[] bc = chunkAccess.getSections();
        int biomeIndex = getBiomeIndex(pos.getX(), pos.getY(), pos.getZ(), 0L);
        Biome[] biomeArray = new Biome[100];
        if (biomeIndex < bc.length) {
            biomeArray[biomeIndex] = biome;
        } else {
            Electrona.LOGGER.error(String.format("Failed to set biome at pos: %s; to biome: %s", pos, biome));
        }
        chunkAccess.setUnsaved(true);
        /*
        Biome biome = world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(biomeKey); // get the biome from the biomeKey
        if (biome == null) return; // check the biome isn't null
        ChunkAccess chunkAccess = world.getChunk(pos); // get access to the chunk at location
        ChunkBiomeContainer bc = chunkAccess.getBiomes(); // get a container of all biomes in the chunk at the location
        if (bc != null) {
            Biome[] biomeArray = bc.biomes; // set all biomes in an array
            int biomeIndex = getBiomeIndex(pos.getX(), pos.getY(), pos.getZ(), 0L); // get the index of a biome at x y z
            if (biomeIndex < biomeArray.length) {
                biomeArray[biomeIndex] = biome; // change the biome in the array at index by our wanted biome
            } else {
                Electrona.LOGGER.error(String.format("Failed to set biome at pos: %s; to biome: %s", pos, biome));
            }
        }
        chunkAccess.setUnsaved(true); // set unsave so it saves again
         */
    }

    /**
     * Gets the number index of a biome
     *
     * @param x    X position of the biome
     * @param y    Y position of the biome
     * @param z    Z position of the biome
     * @param seed Seed of the map
     * @return biome index
     */
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

        int arrayIndex = i3 & HORIZONTAL_MASK;
        arrayIndex |= (k3 & HORIZONTAL_MASK) << WIDTH_BITS;
        return arrayIndex | Mth.clamp(j3, 0, VERTICAL_MASK) << WIDTH_BITS + WIDTH_BITS;
    }

    private static double func_226845_a_(long p_226845_0_, int p_226845_2_, int p_226845_3_, int p_226845_4_, double p_226845_5_, double p_226845_7_, double p_226845_9_) {
        long linCongGen = LinearCongruentialGenerator.next(p_226845_0_, p_226845_2_);
        linCongGen = LinearCongruentialGenerator.next(linCongGen, p_226845_3_);
        linCongGen = LinearCongruentialGenerator.next(linCongGen, p_226845_4_);
        linCongGen = LinearCongruentialGenerator.next(linCongGen, p_226845_2_);
        linCongGen = LinearCongruentialGenerator.next(linCongGen, p_226845_3_);
        linCongGen = LinearCongruentialGenerator.next(linCongGen, p_226845_4_);
        double d0 = func_226844_a_(linCongGen);
        linCongGen = LinearCongruentialGenerator.next(linCongGen, p_226845_0_);
        double d1 = func_226844_a_(linCongGen);
        linCongGen = LinearCongruentialGenerator.next(linCongGen, p_226845_0_);
        double d2 = func_226844_a_(linCongGen);
        return square(p_226845_9_ + d2) + square(p_226845_7_ + d1) + square(p_226845_5_ + d0);
    }

    private static double func_226844_a_(long p_226844_0_) {
        double d0 = (double) ((int) Math.floorMod(p_226844_0_ >> 24, 1024L)) / 1024.0D;
        return (d0 - 0.5D) * 0.9D;
    }

    /**
     * Calculate the square of a number (n*n)
     *
     * @param n the number
     * @return the square
     */
    private static double square(double n) {
        return n * n;
    }
}
