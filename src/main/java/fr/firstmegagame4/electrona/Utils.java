package fr.firstmegagame4.electrona;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.Arrays;
import java.util.function.Predicate;

public class Utils {

    public static final String modIdentifier = "electrona";

    public static Identifier electronaIdentifier(String path) {
        return new Identifier(modIdentifier, path);
    }

    public static void registerItem(String itemId, Item item) {
        Registry.register(Registry.ITEM, electronaIdentifier(itemId), item);
    }

    public static void registerBlock(String blockId, Block block, BlockItem blockItem) {
        Registry.register(Registry.BLOCK, electronaIdentifier(blockId), block);
        Registry.register(Registry.ITEM, electronaIdentifier(blockId), blockItem);
    }

    public static void registerOverworldOre(String blockId, Block block, BlockItem blockItem, boolean isInDeepslate, int veinSize, int numVeins, int minHeight, int maxHeight) {
        RuleTest ruleTest;
        if (!isInDeepslate) ruleTest = OreConfiguredFeatures.STONE_ORE_REPLACEABLES;
        else ruleTest = OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES;
        registerOre(blockId, block, blockItem, veinSize, numVeins, minHeight, maxHeight, ruleTest, BiomeSelectors.foundInOverworld());
    }

    public static void registerNetherOre(String blockId, Block block, BlockItem blockItem, int veinSize, int numVeins, int minHeight, int maxHeight) {
        registerOre(blockId, block, blockItem, veinSize, numVeins, minHeight, maxHeight, OreConfiguredFeatures.NETHERRACK, BiomeSelectors.foundInTheNether());
    }

    public static void registerEndOre(String blockId, Block block, BlockItem blockItem, int veinSize, int numVeins, int minHeight, int maxHeight) {
        registerOre(blockId, block, blockItem, veinSize, numVeins, minHeight, maxHeight, new BlockMatchRuleTest(Blocks.END_STONE), BiomeSelectors.foundInTheEnd());
    }

    public static void registerOre(String blockId, Block block, BlockItem blockItem, int veinSize, int numVeins, int minHeight, int maxHeight, RuleTest ruleTest, Predicate<BiomeSelectionContext> biomeSelectionContextPredicate) {

        Identifier generationIdentifier = electronaIdentifier(blockId + "_generation");
        registerBlock(blockId, block, blockItem);

        ConfiguredFeature<?, ?> configuredFeature = new ConfiguredFeature<>(
                Feature.ORE, new OreFeatureConfig(ruleTest, block.getDefaultState(), veinSize
        ));

        PlacedFeature placedFeature = new PlacedFeature(
                RegistryEntry.of(configuredFeature), Arrays.asList(
                CountPlacementModifier.of(numVeins),
                SquarePlacementModifier.of(),
                HeightRangePlacementModifier.uniform(YOffset.fixed(minHeight), YOffset.fixed(maxHeight))
        ));

        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, generationIdentifier, configuredFeature);
        Registry.register(BuiltinRegistries.PLACED_FEATURE, generationIdentifier, placedFeature);

        BiomeModifications.addFeature(biomeSelectionContextPredicate, GenerationStep.Feature.UNDERGROUND_ORES,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY, generationIdentifier));

    }

}
