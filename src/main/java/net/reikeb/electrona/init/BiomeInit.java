package net.reikeb.electrona.init;

import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.biome.VanillaBiomes;
import net.minecraft.resources.*;
import net.minecraft.world.level.biome.Biome;

import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.*;

import net.reikeb.electrona.Electrona;

public class BiomeInit {

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES,
            Electrona.MODID);

    public static final ResourceKey<Biome> NUCLEAR_BIOME_KEY = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Electrona.MODID, "nuclear"));
    public static final RegistryObject<Biome> NUCLEAR = BIOMES.register("nuclear", VanillaBiomes::theVoidBiome);
}
