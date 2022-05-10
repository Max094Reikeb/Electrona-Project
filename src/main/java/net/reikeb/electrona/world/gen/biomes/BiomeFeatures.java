package net.reikeb.electrona.world.gen.biomes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.BiasedToBottomInt;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.reikeb.electrona.init.BlockInit;

import java.util.List;

public class BiomeFeatures {

    public static final RuleTest END_STONE = new BlockMatchTest(Blocks.END_STONE);

    public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> RADIOACTIVE_GRASS_FEATURE = FeatureUtils.register("patch_radioactive_grass", Feature.RANDOM_PATCH, grassPatch(BlockStateProvider.simple(BlockInit.RADIOACTIVE_GRASS.get()), 5));
    public static final Holder<PlacedFeature> PATCH_RADIOACTIVE_GRASS = PlacementUtils.register("patch_radioactive_grass", RADIOACTIVE_GRASS_FEATURE, RarityFilter.onAverageOnceEvery(4), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());

    public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> RADIOACTIVE_TALL_GRASS_FEATURE = FeatureUtils.register("patch_radioactive_tall_grass", Feature.RANDOM_PATCH, grassPatch(BlockStateProvider.simple(BlockInit.RADIOACTIVE_TALL_GRASS.get()), 2));
    public static final Holder<PlacedFeature> PATCH_RADIOACTIVE_TALL_GRASS = PlacementUtils.register("patch_radioactive_tall_grass", RADIOACTIVE_TALL_GRASS_FEATURE, RarityFilter.onAverageOnceEvery(4), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());

    public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> CHARDWOOD_LOG_FEATURE = FeatureUtils.register("patch_chardwood_log", Feature.RANDOM_PATCH, FeatureUtils.simpleRandomPatchConfiguration(10, PlacementUtils.inlinePlaced(Feature.BLOCK_COLUMN, BlockColumnConfiguration.simple(BiasedToBottomInt.of(1, 3), BlockStateProvider.simple(BlockInit.CHARDWOOD_LOG.get())), BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.matchesBlock(BlockInit.RADIOACTIVE_DIRT.get(), new BlockPos(0, -1, 0)))))));
    public static final Holder<PlacedFeature> PATCH_CHARDWOOD_LOG = PlacementUtils.register("parch_chardwood_log", CHARDWOOD_LOG_FEATURE, RarityFilter.onAverageOnceEvery(5), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());

    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> TIN_ORE_FEATURE = FeatureUtils.register("tin_ore", Feature.ORE, new OreConfiguration(OreFeatures.STONE_ORE_REPLACEABLES, BlockInit.TIN_ORE.get().defaultBlockState(), 10)); // veinSize
    public static final Holder<PlacedFeature> TIN_ORE = PlacementUtils.register("tin_ore", TIN_ORE_FEATURE, orePlacement(8, 6, 80));

    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> DEEPSLATE_TIN_ORE_FEATURE = FeatureUtils.register("deepslate_tin_ore", Feature.ORE, new OreConfiguration(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, BlockInit.DEEPSLATE_TIN_ORE.get().defaultBlockState(), 10)); // veinSize
    public static final Holder<PlacedFeature> DEEPSLATE_TIN_ORE = PlacementUtils.register("deepslate_tin_ore", DEEPSLATE_TIN_ORE_FEATURE, orePlacement(8, -64, 5));

    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> LEAD_ORE_FEATURE = FeatureUtils.register("lead_ore", Feature.ORE, new OreConfiguration(OreFeatures.STONE_ORE_REPLACEABLES, BlockInit.LEAD_ORE.get().defaultBlockState(), 10)); // veinSize
    public static final Holder<PlacedFeature> LEAD_ORE = PlacementUtils.register("lead_ore", LEAD_ORE_FEATURE, orePlacement(5, 6, 55));

    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> DEEPSLATE_LEAD_ORE_FEATURE = FeatureUtils.register("deepslate_lead_ore", Feature.ORE, new OreConfiguration(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, BlockInit.DEEPSLATE_LEAD_ORE.get().defaultBlockState(), 10)); // veinSize
    public static final Holder<PlacedFeature> DEEPSLATE_LEAD_ORE = PlacementUtils.register("deepslate_lead_ore", DEEPSLATE_LEAD_ORE_FEATURE, orePlacement(5, -64, 5));

    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> DEEPSLATE_URANIUM_ORE_FEATURE = FeatureUtils.register("deepslate_uranium_ore", Feature.ORE, new OreConfiguration(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, BlockInit.DEEPSLATE_URANIUM_ORE.get().defaultBlockState(), 6)); // veinSize
    public static final Holder<PlacedFeature> DEEPSLATE_URANIUM_ORE = PlacementUtils.register("deepslate_uranium_ore", DEEPSLATE_URANIUM_ORE_FEATURE, orePlacement(3, -64, -5));

    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> GRAVITONIUM_ORE_FEATURE = FeatureUtils.register("gravitonium_ore", Feature.ORE, new OreConfiguration(END_STONE, BlockInit.GRAVITONIUM_ORE.get().defaultBlockState(), 6)); // veinSize
    public static final Holder<PlacedFeature> GRAVITONIUM_ORE = PlacementUtils.register("gravitonium_ore", GRAVITONIUM_ORE_FEATURE, orePlacement(4, 0, 50));

    public static void addNuclearVegetation(BiomeGenerationSettings.Builder builder) {
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PATCH_RADIOACTIVE_GRASS);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PATCH_RADIOACTIVE_TALL_GRASS);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PATCH_CHARDWOOD_LOG);
    }

    private static RandomPatchConfiguration grassPatch(BlockStateProvider stateProvider, int xzSpread) {
        return radioactiveGrassConfiguration(xzSpread, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(stateProvider), simplePatchPredicate(List.of())));
    }

    private static RandomPatchConfiguration radioactiveGrassConfiguration(int xzSpread, Holder<PlacedFeature> placedFeature) {
        return new RandomPatchConfiguration(xzSpread, 4, 3, placedFeature);
    }

    private static BlockPredicate simplePatchPredicate(List<Block> p_195009_) {
        BlockPredicate blockpredicate;
        if (!p_195009_.isEmpty()) {
            blockpredicate = BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.matchesBlocks(p_195009_, new BlockPos(0, -1, 0)));
        } else {
            blockpredicate = BlockPredicate.ONLY_IN_AIR_PREDICATE;
        }
        return blockpredicate;
    }

    private static List<PlacementModifier> orePlacement(int count, int minHeight, int maxHeight) {
        return List.of(CountPlacement.of(count), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(minHeight), VerticalAnchor.absolute(maxHeight)), BiomeFilter.biome());
    }
}