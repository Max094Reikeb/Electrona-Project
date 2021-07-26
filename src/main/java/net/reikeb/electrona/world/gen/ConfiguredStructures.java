package net.reikeb.electrona.world.gen;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;

import net.reikeb.electrona.Electrona;

public class ConfiguredStructures {

    public static ConfiguredStructureFeature<?, ?> CONFIGURED_RUINS = Structures.RUINS.get().configured(FeatureConfiguration.NONE);

    public static void registerConfiguredStructures() {
        Registry<ConfiguredStructureFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(Electrona.MODID, "configured_ruins"), CONFIGURED_RUINS);

        FlatLevelGeneratorSettings.STRUCTURE_FEATURES.put(Structures.RUINS.get(), CONFIGURED_RUINS);
    }
}
