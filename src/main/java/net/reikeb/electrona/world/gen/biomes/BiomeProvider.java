package net.reikeb.electrona.world.gen.biomes;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.SurfaceRules;

import net.reikeb.electrona.init.BiomeInit;

import terrablender.worldgen.TBClimate;

import java.util.Optional;
import java.util.function.Consumer;

public class BiomeProvider extends terrablender.api.BiomeProvider {

    public BiomeProvider(ResourceLocation name, int overworldWeight) {
        super(name, overworldWeight);
    }

    public void addOverworldBiomes(Registry<Biome> registry, Consumer<Pair<TBClimate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        this.addBiomeSimilar(mapper, Biomes.DESERT, BiomeInit.NUCLEAR);
    }

    @Override
    public Optional<SurfaceRules.RuleSource> getOverworldSurfaceRules() {
        return Optional.of(SurfaceRuleData.makeRules());
    }
}
