package net.reikeb.electrona.world.structures;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.EntityInit;

import java.util.List;

public class RuinsStructure extends StructureFeature<NoneFeatureConfiguration> {

    public RuinsStructure(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return RuinsStructure.Start::new;
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    private static final List<MobSpawnSettings.SpawnerData> STRUCTURE_MONSTERS = ImmutableList.of(
            new MobSpawnSettings.SpawnerData(EntityInit.RADIOACTIVE_ZOMBIE.get(), 100, 4, 9)
    );

    @Override
    public List<MobSpawnSettings.SpawnerData> getDefaultSpawnList() {
        return STRUCTURE_MONSTERS;
    }

    @Override
    protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long seed, WorldgenRandom chunkRandom, ChunkPos chunkpos, Biome biome, ChunkPos chunkPos, NoneFeatureConfiguration featureConfig, LevelHeightAccessor levelHeightAccessor) {
        BlockPos centerOfChunk = new BlockPos((chunkpos.x << 4) + 7, 0, (chunkpos.z << 4) + 7);
        int landHeight = chunkGenerator.getFirstOccupiedHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor);
        NoiseColumn columnOfBlocks = chunkGenerator.getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ(), levelHeightAccessor);
        BlockState topBlock = columnOfBlocks.getBlockState(centerOfChunk.above(landHeight));
        return topBlock.getFluidState().isEmpty();
    }

    public static class Start extends StructureStart<NoneFeatureConfiguration> {

        public Start(StructureFeature<NoneFeatureConfiguration> structureIn, ChunkPos chunkPos, int referenceIn, long seedIn) {
            super(structureIn, chunkPos, referenceIn, seedIn);
        }

        @Override
        public void generatePieces(RegistryAccess dynamicRegistryManager, ChunkGenerator chunkGenerator, StructureManager templateManagerIn, ChunkPos chunkPos, Biome biomeIn, NoneFeatureConfiguration config, LevelHeightAccessor levelHeightAccessor) {
            int x = (chunkPos.x << 4) + 7;
            int z = (chunkPos.z << 4) + 7;

            BlockPos blockPos = new BlockPos(x, 0, z);

            JigsawPlacement.addPieces(
                    dynamicRegistryManager,
                    new JigsawConfiguration(() -> dynamicRegistryManager.registryOrThrow(
                            Registry.TEMPLATE_POOL_REGISTRY).get(Electrona.RL("ruins/start_pool")), 10),
                    PoolElementStructurePiece::new,
                    chunkGenerator,
                    templateManagerIn,
                    blockPos,
                    this,
                    this.random,
                    false,
                    false,
                    levelHeightAccessor);

            // **THE FOLLOWING TWO LINES ARE OPTIONAL**
            //
            // Right here, you can do interesting stuff with the pieces in this.pieces such as offset the
            // center piece by 50 blocks up for no reason, remove repeats of a piece or add a new piece so
            // only 1 of that piece exists, etc. But you do not have access to the piece's blocks as this list
            // holds just the piece's size and positions. Blocks will be placed later in JigsawManager.
            //
            // In this case, we do `piece.offset` to raise pieces up by 1 block so that the house is not right on
            // the surface of water or sunken into land a bit.
            //
            // Then we extend the bounding box down by 1 by doing `piece.getBoundingBox().minY` which will cause the
            // land formed around the structure to be lowered and not cover the doorstep. You can raise the bounding
            // box to force the structure to be buried as well. This bounding box stuff with land is only for structures
            // that you added to Structure.NOISE_AFFECTING_FEATURES field handles adding land around the base of structures.
            //
            // By lifting the house up by 1 and lowering the bounding box, the land at bottom of house will now be
            // flush with the surrounding terrain without blocking off the doorstep.
            this.pieces.forEach(piece -> piece.move(0, 1, 0));
            this.pieces.forEach(piece -> piece.getBoundingBox().minY -= 1);

            this.getBoundingBox();
        }
    }
}
