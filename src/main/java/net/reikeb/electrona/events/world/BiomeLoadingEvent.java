package net.reikeb.electrona.events.world;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.world.gen.features.ConfiguredFeatures;

@Mod.EventBusSubscriber(modid = Electrona.MODID)
public class BiomeLoadingEvent {

    @SubscribeEvent
    public static void addFeatureToBiomes(net.minecraftforge.event.world.BiomeLoadingEvent event) {
        if (event.getCategory().equals(Biome.BiomeCategory.THEEND)) {
            event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(() -> ConfiguredFeatures.GRAVITONIUM_ORE_CONFIGURED_FEATURE);
        }
        if (!event.getCategory().equals(Biome.BiomeCategory.NETHER)
                && !event.getCategory().equals(Biome.BiomeCategory.THEEND)
                && !event.getCategory().equals(Biome.BiomeCategory.NONE)) {
            event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(() -> ConfiguredFeatures.TIN_ORE_CONFIGURED_FEATURE);
            event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(() -> ConfiguredFeatures.URANIUM_ORE_CONFIGURED_FEATURE);
            event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(() -> ConfiguredFeatures.LEAD_ORE_CONFIGURED_FEATURE);
        }
    }
}
