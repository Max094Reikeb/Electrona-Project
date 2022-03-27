package net.reikeb.electrona.events.world;

import net.minecraft.world.level.biome.Biome;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import net.reikeb.electrona.init.BiomeInit;
import net.reikeb.electrona.world.gen.biomes.OverworldBiomes;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterBiomeEvent {

    @SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> event) {
        IForgeRegistry<Biome> registry = event.getRegistry();
        registry.register(OverworldBiomes.nuclear().setRegistryName(BiomeInit.NUCLEAR.location()));
    }
}
