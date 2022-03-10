package net.reikeb.electrona.world.gen;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.PlainVillagePools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;

import net.reikeb.electrona.Electrona;

public class ConfiguredStructures {

    public static ConfiguredStructureFeature<?, ?> CONFIGURED_RUINS = Structures.RUINS.get().configured(new JigsawConfiguration(() -> PlainVillagePools.START, 0));

    public static void registerConfiguredStructures() {
        Registry<ConfiguredStructureFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(Electrona.MODID, "configured_ruins"), CONFIGURED_RUINS);

    }
}
