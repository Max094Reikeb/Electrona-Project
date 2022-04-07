package net.reikeb.electrona.setup;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.reikeb.electrona.init.*;
import net.reikeb.electrona.villages.Villagers;
import net.reikeb.electrona.world.gen.Structures;

public class RegistryHandler {

    public static void init() {
        BiomeInit.BIOMES.register(FMLJavaModLoadingContext.get().getModEventBus());
        BlockInit.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BlockEntityInit.BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ContainerInit.CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        PotionEffectInit.EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        EntityInit.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        EnchantmentInit.ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ItemInit.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ParticleInit.PARTICLES.register(FMLJavaModLoadingContext.get().getModEventBus());
        SoundsInit.SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
        Structures.DEFERRED_REGISTRY_STRUCTURE.register(FMLJavaModLoadingContext.get().getModEventBus());
        Villagers.POI.register(FMLJavaModLoadingContext.get().getModEventBus());
        Villagers.PROFESSIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
