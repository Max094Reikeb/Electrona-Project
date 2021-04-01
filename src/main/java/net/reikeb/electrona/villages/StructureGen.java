package net.reikeb.electrona.villages;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.*;
import net.minecraft.world.gen.feature.jigsaw.*;

import net.minecraftforge.fml.common.Mod;

import net.reikeb.electrona.Electrona;

import java.util.*;

@Mod.EventBusSubscriber(modid = Electrona.MODID)
public class StructureGen {

    private static final ResourceLocation mainEngineerHouse = new ResourceLocation("electrona", "villages/engineer_house_plains");

    public static void setupVillageWorldGen(DynamicRegistries dynamicRegistries) {
        // Add Houses to Vanilla Villages.
        addEngineerHouseToVillageConfig(dynamicRegistries, "village/plains/houses", mainEngineerHouse, 2);
        addEngineerHouseToVillageConfig(dynamicRegistries, "village/savanna/houses", new ResourceLocation("electrona", "villages/engineer_house_savanna"), 2);
        addEngineerHouseToVillageConfig(dynamicRegistries, "village/desert/houses", new ResourceLocation("electrona", "villages/engineer_house_desert"), 2);
        addEngineerHouseToVillageConfig(dynamicRegistries, "village/taiga/houses", new ResourceLocation("electrona", "villages/engineer_house_taiga"), 2);

        // Add Houses to other mod's structures. (Make sure Houses piece Jigsaw Block's Name matches the other mod piece Jigsaw's Target Name.
        addEngineerHouseToVillageConfig(dynamicRegistries, "repurposed_structures:village/badlands/houses", mainEngineerHouse, 2);
        addEngineerHouseToVillageConfig(dynamicRegistries, "repurposed_structures:village/birch/houses", mainEngineerHouse, 2);
        addEngineerHouseToVillageConfig(dynamicRegistries, "repurposed_structures:village/dark_forest/houses", mainEngineerHouse, 2);
        addEngineerHouseToVillageConfig(dynamicRegistries, "repurposed_structures:village/jungle/houses", mainEngineerHouse, 2);
        addEngineerHouseToVillageConfig(dynamicRegistries, "repurposed_structures:village/mountains/houses", mainEngineerHouse, 2);
        addEngineerHouseToVillageConfig(dynamicRegistries, "repurposed_structures:village/oak/houses", mainEngineerHouse, 2);
        addEngineerHouseToVillageConfig(dynamicRegistries, "repurposed_structures:village/swamp/houses", mainEngineerHouse, 2);
    }

    private static void addEngineerHouseToVillageConfig(DynamicRegistries dynamicRegistries, String villagePiece, ResourceLocation waystoneStructure, int weight) {
        LegacySingleJigsawPiece piece = JigsawPiece.legacy(waystoneStructure.toString()).apply(JigsawPattern.PlacementBehaviour.RIGID);
        JigsawPattern pool = dynamicRegistries.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).getOptional(new ResourceLocation(villagePiece)).orElse(null);
        if (pool != null) {
            // Pretty sure this can be an immutable list (when datapacked) so gotta make a copy to be safe.
            List<JigsawPiece> listOfPieces = new ArrayList<>(pool.templates);
            for (int i = 0; i < weight; i++) {
                listOfPieces.add(piece);
            }
            pool.templates = listOfPieces;
        }
    }
}
