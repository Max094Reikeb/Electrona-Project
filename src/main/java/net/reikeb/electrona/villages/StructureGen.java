package net.reikeb.electrona.villages;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.LegacySinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.fml.common.Mod;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.misc.Keys;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Electrona.MODID)
public class StructureGen {

    private static final ResourceLocation mainEngineerHouse = Keys.ENGINEER_HOUSE_PLAINS;

    public static void setupVillageWorldGen(RegistryAccess dynamicRegistries) {
        // Add Houses to Vanilla Villages.
        addEngineerHouseToVillageConfig(dynamicRegistries, "village/plains/houses", mainEngineerHouse, 2);
        addEngineerHouseToVillageConfig(dynamicRegistries, "village/savanna/houses", Keys.ENGINEER_HOUSE_SAVANNA, 2);
        addEngineerHouseToVillageConfig(dynamicRegistries, "village/desert/houses", Keys.ENGINEER_HOUSE_DESERT, 2);
        addEngineerHouseToVillageConfig(dynamicRegistries, "village/taiga/houses", Keys.ENGINEER_HOUSE_TAIGA, 2);
        addEngineerHouseToVillageConfig(dynamicRegistries, "village/snowy/houses", Keys.ENGINEER_HOUSE_SNOWY, 2);

        // Add Houses to other mod's structures. (Make sure Houses piece Jigsaw Block's Name matches the other mod piece Jigsaw's Target Name.
        addEngineerHouseToVillageConfig(dynamicRegistries, "repurposed_structures:village/badlands/houses", mainEngineerHouse, 2);
        addEngineerHouseToVillageConfig(dynamicRegistries, "repurposed_structures:village/birch/houses", mainEngineerHouse, 2);
        addEngineerHouseToVillageConfig(dynamicRegistries, "repurposed_structures:village/dark_forest/houses", mainEngineerHouse, 2);
        addEngineerHouseToVillageConfig(dynamicRegistries, "repurposed_structures:village/jungle/houses", mainEngineerHouse, 2);
        addEngineerHouseToVillageConfig(dynamicRegistries, "repurposed_structures:village/mountains/houses", mainEngineerHouse, 2);
        addEngineerHouseToVillageConfig(dynamicRegistries, "repurposed_structures:village/oak/houses", mainEngineerHouse, 2);
        addEngineerHouseToVillageConfig(dynamicRegistries, "repurposed_structures:village/swamp/houses", mainEngineerHouse, 2);
    }

    private static void addEngineerHouseToVillageConfig(RegistryAccess dynamicRegistries, String villagePiece, ResourceLocation waystoneStructure, int weight) {
        LegacySinglePoolElement piece = StructurePoolElement.legacy(waystoneStructure.toString()).apply(StructureTemplatePool.Projection.RIGID);
        StructureTemplatePool pool = dynamicRegistries.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).getOptional(new ResourceLocation(villagePiece)).orElse(null);
        if (pool != null) {
            // Pretty sure this can be an immutable list (when datapacked) so gotta make a copy to be safe.
            List<StructurePoolElement> listOfPieces = new ArrayList<>(pool.templates);
            for (int i = 0; i < weight; i++) {
                listOfPieces.add(piece);
            }
            pool.templates = listOfPieces;
        }
    }
}
