package net.reikeb.electrona.world.structures;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;

import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.EntityInit;
import net.reikeb.electrona.world.gen.Structures;

import java.util.List;
import java.util.Optional;

public class RuinsStructure extends StructureFeature<JigsawConfiguration> {

    public RuinsStructure(Codec<JigsawConfiguration> codec) {
        super(codec, RuinsStructure::createPiecesGenerator, PostPlacementProcessor.NONE);
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    private static final Lazy<List<MobSpawnSettings.SpawnerData>> STRUCTURE_MONSTERS = Lazy.of(() -> ImmutableList.of(
            new MobSpawnSettings.SpawnerData(EntityInit.RADIOACTIVE_ZOMBIE.get(), 100, 4, 9)
    ));

    public static void setupStructureSpawns(final StructureSpawnListGatherEvent event) {
        if (event.getStructure() == Structures.RUINS.get()) {
            event.addEntitySpawns(MobCategory.MONSTER, STRUCTURE_MONSTERS.get());
        }
    }

    private static boolean isFeatureChunk(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        BlockPos blockPos = context.chunkPos().getWorldPosition();

        int landHeight = context.chunkGenerator().getFirstOccupiedHeight(blockPos.getX(), blockPos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
        NoiseColumn columnOfBlocks = context.chunkGenerator().getBaseColumn(blockPos.getX(), blockPos.getZ(), context.heightAccessor());

        BlockState topBlock = columnOfBlocks.getBlock(landHeight);
        return topBlock.getFluidState().isEmpty();
    }

    public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        if (!isFeatureChunk(context)) {
            return Optional.empty();
        }
        JigsawConfiguration newConfig = new JigsawConfiguration(() ->
                context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                        .get(Electrona.RL("ruins/start_pool")), 10);

        PieceGeneratorSupplier.Context<JigsawConfiguration> newContext = new PieceGeneratorSupplier.Context<>(
                context.chunkGenerator(),
                context.biomeSource(),
                context.seed(),
                context.chunkPos(),
                newConfig,
                context.heightAccessor(),
                context.validBiome(),
                context.structureManager(),
                context.registryAccess());

        int x = (context.chunkPos().x << 4) + 7;
        int z = (context.chunkPos().z << 4) + 7;
        BlockPos blockpos = new BlockPos(x, 0, z);

        Optional<PieceGenerator<JigsawConfiguration>> structurePiecesGenerator = JigsawPlacement.addPieces(
                newContext,
                PoolElementStructurePiece::new, // Needed in order to create a list of jigsaw pieces when making the structure's layout.
                blockpos, // Position of the structure. Y value is ignored if last parameter is set to true.
                false,  // Special boundary adjustments for villages. It's... hard to explain. Keep this false and make your pieces not be partially intersecting.
                // Either not intersecting or fully contained will make children pieces spawn just fine. It's easier that way.
                true // Place at heightmap (top land). Set this to false for structure to be place at the passed in blockpos's Y value instead.
                // Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.
        );

        return structurePiecesGenerator;
    }
}
