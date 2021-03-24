package net.reikeb.electrona.events;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.world.gen.ConfiguredFeatures;

@Mod.EventBusSubscriber(modid = Electrona.MODID)
public class BiomeLoadingEvent {

    @SubscribeEvent
    public static void addFeatureToBiomes(net.minecraftforge.event.world.BiomeLoadingEvent event) {
        if (!event.getCategory().equals(Biome.Category.NETHER)
                && !event.getCategory().equals(Biome.Category.THEEND)
                && !event.getCategory().equals(Biome.Category.NONE)) {
            event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).add(() -> ConfiguredFeatures.TIN_ORE_CONFIGURED_FEATURE);
            event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).add(() -> ConfiguredFeatures.URANIUM_ORE_CONFIGURED_FEATURE);
            event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).add(() -> ConfiguredFeatures.LEAD_ORE_CONFIGURED_FEATURE);
        }
    }
}
