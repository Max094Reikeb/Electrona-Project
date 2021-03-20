package net.reikeb.electrona.network;

import net.minecraft.util.ResourceLocation;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.network.packets.TotemPacket;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Electrona.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NetworkManager {

    public static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Electrona.MODID, "main"), () -> NetworkManager.PROTOCOL_VERSION, NetworkManager.PROTOCOL_VERSION::equals, NetworkManager.PROTOCOL_VERSION::equals);

    @SuppressWarnings("UnusedAssignment")
    @SubscribeEvent
    public static void registerNetworkStuff(FMLCommonSetupEvent event) {
        int index = 0;
        INSTANCE.registerMessage(index++, TotemPacket.class, TotemPacket::encode, TotemPacket::decode, TotemPacket::whenThisPacketIsReceived);

    }
}
