package net.reikeb.electrona.events.world;

import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.BlockInit;

import java.util.List;

@Mod.EventBusSubscriber(modid = Electrona.MODID)
public class LoadBiomeEvent {

    public static final RuleTest END_STONE = new BlockMatchTest(Blocks.END_STONE);

    @SubscribeEvent
    public static void addFeatureToBiomes(net.minecraftforge.event.world.BiomeLoadingEvent event) {
        if (!event.getCategory().equals(Biome.BiomeCategory.NETHER)
                && !event.getCategory().equals(Biome.BiomeCategory.THEEND)
                && !event.getCategory().equals(Biome.BiomeCategory.NONE)) {
            generateOre(event.getGeneration(), OreFeatures.NATURAL_STONE, BlockInit.TIN_ORE.get().defaultBlockState(), 10, 0, 50, 5);
            generateOre(event.getGeneration(), OreFeatures.NATURAL_STONE, BlockInit.URANIUM_ORE.get().defaultBlockState(), 8, 0, 30, 6);
            generateOre(event.getGeneration(), OreFeatures.NATURAL_STONE, BlockInit.LEAD_ORE.get().defaultBlockState(), 10, 0, 25, 6);
        }

        if (event.getCategory().equals(Biome.BiomeCategory.THEEND)) {
            generateOre(event.getGeneration(), END_STONE, BlockInit.GRAVITONIUM_ORE.get().defaultBlockState(), 6, 0, 50, 4);
        }
    }

    private static void generateOre(BiomeGenerationSettingsBuilder settings, RuleTest fillerType, BlockState state, int veinSize, int minHeight, int maxHeight, int count) {
        settings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                Feature.ORE.configured(new OreConfiguration(fillerType, state, veinSize))
                        .placed(List.of((CountPlacement.of(UniformInt.of(0, count))),
                                InSquarePlacement.spread(),
                                HeightRangePlacement.uniform(VerticalAnchor.absolute(minHeight), VerticalAnchor.absolute(maxHeight)),
                                BiomeFilter.biome())));
    }
}
