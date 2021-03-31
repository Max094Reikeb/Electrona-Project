package net.reikeb.electrona.world.gen;

import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.BlockInit;

public class ConfiguredFeatures {

    public static ConfiguredFeature<?, ?> TIN_ORE_CONFIGURED_FEATURE = new OreFeature(OreFeatureConfig.CODEC)
            .configured(new OreFeatureConfig(new TagMatchRuleTest(BlockTags.BASE_STONE_OVERWORLD),
                    BlockInit.TIN_ORE.get().defaultBlockState(), 10)).range(40)
            .squared().count(5);

    public static ConfiguredFeature<?, ?> URANIUM_ORE_CONFIGURED_FEATURE = new OreFeature(OreFeatureConfig.CODEC)
            .configured(new OreFeatureConfig(new TagMatchRuleTest(BlockTags.BASE_STONE_OVERWORLD),
                    BlockInit.URANIUM_ORE.get().defaultBlockState(), 8)).range(30)
            .squared().count(6);

    public static ConfiguredFeature<?, ?> LEAD_ORE_CONFIGURED_FEATURE = new OreFeature(OreFeatureConfig.CODEC)
            .configured(new OreFeatureConfig(new TagMatchRuleTest(BlockTags.BASE_STONE_OVERWORLD),
                    BlockInit.LEAD_ORE.get().defaultBlockState(), 10)).range(25)
            .squared().count(6);

    public static void registerConfiguredFeatures() {
        Registry<ConfiguredFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_FEATURE;
        Registry.register(registry, new ResourceLocation(Electrona.MODID, "tin_ore"), TIN_ORE_CONFIGURED_FEATURE);
        Registry.register(registry, new ResourceLocation(Electrona.MODID, "uranium_ore"), URANIUM_ORE_CONFIGURED_FEATURE);
        Registry.register(registry, new ResourceLocation(Electrona.MODID, "lead_ore"), LEAD_ORE_CONFIGURED_FEATURE);
    }
}
