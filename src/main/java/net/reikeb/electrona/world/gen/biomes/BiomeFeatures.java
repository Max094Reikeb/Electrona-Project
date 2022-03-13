package net.reikeb.electrona.world.gen.biomes;

import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.BiasedToBottomInt;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;

import net.reikeb.electrona.init.BlockInit;

public class BiomeFeatures {

    public static final ConfiguredFeature<RandomPatchConfiguration, ?> RADIOACTIVE_GRASS_FEATURE = FeatureUtils.register("patch_radioactive_grass", Feature.RANDOM_PATCH.configured(grassPatch(BlockStateProvider.simple(BlockInit.RADIOACTIVE_GRASS.get()), 5)));
    public static final PlacedFeature PATCH_RADIOACTIVE_GRASS = PlacementUtils.register("patch_radioactive_grass", RADIOACTIVE_GRASS_FEATURE.placed(RarityFilter.onAverageOnceEvery(4), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));

    public static final ConfiguredFeature<RandomPatchConfiguration, ?> RADIOACTIVE_TALL_GRASS_FEATURE = FeatureUtils.register("patch_radioactive_tall_grass", Feature.RANDOM_PATCH.configured(grassPatch(BlockStateProvider.simple(BlockInit.RADIOACTIVE_TALL_GRASS.get()), 2)));
    public static final PlacedFeature PATCH_RADIOACTIVE_TALL_GRASS = PlacementUtils.register("patch_radioactive_tall_grass", RADIOACTIVE_TALL_GRASS_FEATURE.placed(RarityFilter.onAverageOnceEvery(4), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));

    public static final ConfiguredFeature<?, ?> CHARDWOOD_LOG_FEATURE = FeatureUtils.register("patch_chardwood_log", Feature.RANDOM_PATCH.configured(FeatureUtils.simpleRandomPatchConfiguration(10, Feature.BLOCK_COLUMN.configured(BlockColumnConfiguration.simple(BiasedToBottomInt.of(1, 3), BlockStateProvider.simple(BlockInit.CHARDWOOD_LOG.get()))).placed(BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.matchesBlock(BlockInit.RADIOACTIVE_DIRT.get(), new BlockPos(0, -1, 0))))))));
    public static final PlacedFeature PATCH_CHARDWOOD_LOG = PlacementUtils.register("parch_chardwood_log", CHARDWOOD_LOG_FEATURE.placed(RarityFilter.onAverageOnceEvery(5), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));

    public static void addNuclearVegetation(BiomeGenerationSettings.Builder builder) {
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PATCH_RADIOACTIVE_GRASS);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PATCH_RADIOACTIVE_TALL_GRASS);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PATCH_CHARDWOOD_LOG);
    }

    private static RandomPatchConfiguration grassPatch(BlockStateProvider stateProvider, int xzSpread) {
        return radioactiveGrassConfiguration(xzSpread, Feature.SIMPLE_BLOCK
                .configured(new SimpleBlockConfiguration(stateProvider)).onlyWhenEmpty());
    }

    public static RandomPatchConfiguration radioactiveGrassConfiguration(int xzSpread, PlacedFeature placedFeature) {
        return new RandomPatchConfiguration(4, xzSpread, 3, () -> {
            return placedFeature;
        });
    }
}