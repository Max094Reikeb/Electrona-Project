package net.reikeb.electrona.init;

import net.minecraft.util.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.*;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.*;

import net.reikeb.electrona.Electrona;

public class BiomeInit {

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES,
            Electrona.MODID);

    public static final RegistryKey<Biome> NUCLEAR_BIOME_KEY = RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Electrona.MODID, "nuclear"));
    public static final RegistryObject<Biome> NUCLEAR = BIOMES.register("nuclear", BiomeMaker::theVoidBiome);
}
