package net.reikeb.electrona.events.world;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.world.gen.biomes.BiomeFeatures;

@Mod.EventBusSubscriber(modid = Electrona.MODID)
public class LoadBiomeEvent {

    @SubscribeEvent
    public static void addFeatureToBiomes(BiomeLoadingEvent event) {
        if (!event.getCategory().equals(Biome.BiomeCategory.NETHER)
                && !event.getCategory().equals(Biome.BiomeCategory.THEEND)
                && !event.getCategory().equals(Biome.BiomeCategory.NONE)) {
            event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(BiomeFeatures.TIN_ORE);
            event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(BiomeFeatures.DEEPSLATE_TIN_ORE);
            event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(BiomeFeatures.LEAD_ORE);
            event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(BiomeFeatures.DEEPSLATE_LEAD_ORE);
            event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(BiomeFeatures.DEEPSLATE_URANIUM_ORE);
        }

        if (event.getCategory().equals(Biome.BiomeCategory.THEEND)) {
            event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(BiomeFeatures.GRAVITONIUM_ORE);
        }
    }
}
