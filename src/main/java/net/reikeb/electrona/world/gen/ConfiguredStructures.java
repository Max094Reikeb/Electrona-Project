package net.reikeb.electrona.world.gen;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.*;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.*;

import net.reikeb.electrona.Electrona;

public class ConfiguredStructures {

    public static StructureFeature<?, ?> CONFIGURED_RUINS = Structures.RUINS.get().configured(IFeatureConfig.NONE);

    public static void registerConfiguredStructures() {
        Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(Electrona.MODID, "configured_ruins"), CONFIGURED_RUINS);

        FlatGenerationSettings.STRUCTURE_FEATURES.put(Structures.RUINS.get(), CONFIGURED_RUINS);
    }
}
