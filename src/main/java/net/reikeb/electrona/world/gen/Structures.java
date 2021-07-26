package net.reikeb.electrona.world.gen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;

import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.world.structures.RuinsStructure;

import java.util.HashMap;
import java.util.Map;

public class Structures {

    public static final DeferredRegister<StructureFeature<?>> DEFERRED_REGISTRY_STRUCTURE = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, Electrona.MODID);

    public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> RUINS = DEFERRED_REGISTRY_STRUCTURE.register("ruins", () -> (new RuinsStructure(NoneFeatureConfiguration.CODEC)));

    public static void setupStructures() {
        setupMapSpacingAndLand(
                RUINS.get(),
                new StructureFeatureConfiguration(10 /* average distance apart in chunks between spawn attempts */,
                        8 /* minimum distance apart in chunks between spawn attempts */,
                        32034987),
                true);
    }

    public static <F extends StructureFeature<?>> void setupMapSpacingAndLand(
            F structure,
            StructureFeatureConfiguration structureSeparationSettings,
            boolean transformSurroundingLand) {

        StructureFeature.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

        if (transformSurroundingLand) {
            StructureFeature.NOISE_AFFECTING_FEATURES =
                    ImmutableList.<StructureFeature<?>>builder()
                            .addAll(StructureFeature.NOISE_AFFECTING_FEATURES)
                            .add(structure)
                            .build();
        }

        StructureSettings.DEFAULTS =
                ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder()
                        .putAll(StructureSettings.DEFAULTS)
                        .put(structure, structureSeparationSettings)
                        .build();

        BuiltinRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
            Map<StructureFeature<?>, StructureFeatureConfiguration> structureMap = settings.getValue().structureSettings().structureConfig();

            if (structureMap instanceof ImmutableMap) {
                Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, structureSeparationSettings);
                settings.getValue().structureSettings().structureConfig = tempMap;
            } else {
                structureMap.put(structure, structureSeparationSettings);
            }
        });
    }
}
