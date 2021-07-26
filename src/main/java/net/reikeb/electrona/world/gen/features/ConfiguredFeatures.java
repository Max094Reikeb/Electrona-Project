package net.reikeb.electrona.world.gen.features;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.registry.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.template.*;

import net.minecraft.world.level.levelgen.feature.configurations.RangeDecoratorConfiguration;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.BlockInit;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

public class ConfiguredFeatures {

    public static ConfiguredFeature<?, ?> TIN_ORE_CONFIGURED_FEATURE = new OreFeature(OreConfiguration.CODEC)
            .configured(new OreConfiguration(new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD),
                    BlockInit.TIN_ORE.get().defaultBlockState(), 10)).range(new RangeDecoratorConfiguration(HeightProvider))
            .squared().count(5);

    public static ConfiguredFeature<?, ?> URANIUM_ORE_CONFIGURED_FEATURE = new OreFeature(OreConfiguration.CODEC)
            .configured(new OreConfiguration(new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD),
                    BlockInit.URANIUM_ORE.get().defaultBlockState(), 8)).range(30)
            .squared().count(6);

    public static ConfiguredFeature<?, ?> LEAD_ORE_CONFIGURED_FEATURE = new OreFeature(OreConfiguration.CODEC)
            .configured(new OreConfiguration(new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD),
                    BlockInit.LEAD_ORE.get().defaultBlockState(), 10)).range(25)
            .squared().count(6);

    public static ConfiguredFeature<?, ?> GRAVITONIUM_ORE_CONFIGURED_FEATURE = new OreFeature(OreConfiguration.CODEC)
            .configured(new OreConfiguration(new BlockMatchTest(Blocks.END_STONE),
                    BlockInit.GRAVITONIUM_ORE.get().defaultBlockState(), 6)).range(50)
            .squared().count(4);

    public static void registerConfiguredFeatures() {
        Registry<ConfiguredFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_FEATURE;
        Registry.register(registry, new ResourceLocation(Electrona.MODID, "tin_ore"), TIN_ORE_CONFIGURED_FEATURE);
        Registry.register(registry, new ResourceLocation(Electrona.MODID, "uranium_ore"), URANIUM_ORE_CONFIGURED_FEATURE);
        Registry.register(registry, new ResourceLocation(Electrona.MODID, "lead_ore"), LEAD_ORE_CONFIGURED_FEATURE);
        Registry.register(registry, new ResourceLocation(Electrona.MODID, "gravitonium_ore"), GRAVITONIUM_ORE_CONFIGURED_FEATURE);
    }
}
