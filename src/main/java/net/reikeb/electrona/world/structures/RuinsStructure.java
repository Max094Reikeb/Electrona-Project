package net.reikeb.electrona.world.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class RuinsStructure extends StructureFeature<JigsawConfiguration> {

    public RuinsStructure() {
        super(JigsawConfiguration.CODEC, RuinsStructure::createPiecesGenerator, PostPlacementProcessor.NONE);
    }

    private static boolean isFeatureChunk(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        ChunkPos chunkPos = context.chunkPos();

        return !context.chunkGenerator().hasFeatureChunkInRange(BuiltinStructureSets.OCEAN_MONUMENTS, context.seed(), chunkPos.x, chunkPos.z, 9);
    }

    public static @NotNull Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        if (!isFeatureChunk(context)) {
            return Optional.empty();
        }
        BlockPos blockPos = context.chunkPos().getMiddleBlockPosition(0);

        return JigsawPlacement.addPieces(
                context,
                PoolElementStructurePiece::new, // Needed in order to create a list of jigsaw pieces when making the structure's layout.
                blockPos, // Position of the structure. Y value is ignored if last parameter is set to true.
                false,  // Special boundary adjustments for villages. It's... hard to explain. Keep this false and make your pieces not be partially intersecting.
                // Either not intersecting or fully contained will make children pieces spawn just fine. It's easier that way.
                true // Place at heightmap (top land). Set this to false for structure to be place at the passed in blockpos's Y value instead.
                // Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.
        );
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }
}
