package net.reikeb.electrona.world.gen;

import com.google.common.collect.*;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.settings.*;

import net.minecraftforge.event.RegistryEvent;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.world.structures.*;

public class Structures {

    public static Structure<NoFeatureConfig> RUINS = new RuinsStructure(NoFeatureConfig.CODEC);
    public static IStructurePieceType RUINS_PIECES = RuinsPieces.Piece::new;

    public static void registerStructures(RegistryEvent.Register<Structure<?>> event) {

        Electrona.register(event.getRegistry(), RUINS, "ruins");

        registerStructure(
                RUINS,
                new StructureSeparationSettings(10,
                        5,
                        1234567890),
                true);


        Structures.registerAllPieces();
    }

    public static <F extends Structure<?>> void registerStructure(
            F structure,
            StructureSeparationSettings structureSeparationSettings,
            boolean transformSurroundingLand) {
        Structure.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

        if (transformSurroundingLand) {
            Structure.NOISE_AFFECTING_FEATURES =
                    ImmutableList.<Structure<?>>builder()
                            .addAll(Structure.NOISE_AFFECTING_FEATURES)
                            .add(structure)
                            .build();
        }

        DimensionStructuresSettings.DEFAULTS =
                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                        .putAll(DimensionStructuresSettings.DEFAULTS)
                        .put(structure, structureSeparationSettings)
                        .build();
    }

    public static void registerAllPieces() {
        registerStructurePiece(RUINS_PIECES, new ResourceLocation(Electrona.MODID, "ruins_pieces"));
    }

    static void registerStructurePiece(IStructurePieceType structurePiece, ResourceLocation rl) {
        Registry.register(Registry.STRUCTURE_PIECE, rl, structurePiece);
    }
}
