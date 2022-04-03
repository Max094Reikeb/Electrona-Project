package net.reikeb.electrona.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.misc.Keys;

public class BiomeInit {

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES,
            Electrona.MODID);

    public static final ResourceKey<Biome> NUCLEAR = ResourceKey.create(Registry.BIOME_REGISTRY, Keys.NUCLEAR_BIOME);
}
