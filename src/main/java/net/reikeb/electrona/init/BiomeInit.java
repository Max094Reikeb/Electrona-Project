package net.reikeb.electrona.init;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeMaker;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.*;

import net.reikeb.electrona.Electrona;

public class BiomeInit {

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES,
            Electrona.MODID);

    public static final RegistryObject<Biome> NUCLEAR = BIOMES.register("nuclear", BiomeMaker::theVoidBiome);
}
