package net.reikeb.electrona.init;

import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.reikeb.electrona.Electrona;

public class BiomeInit {

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES,
            Electrona.MODID);

    public static final ResourceKey<Biome> NUCLEAR_BIOME_KEY = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Electrona.MODID, "nuclear"));
}
