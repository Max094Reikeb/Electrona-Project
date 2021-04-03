package net.reikeb.electrona.network;

import net.minecraft.util.ResourceLocation;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.network.packets.*;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Electrona.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NetworkManager {

    public static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Electrona.MODID, "main"), () -> NetworkManager.PROTOCOL_VERSION,
            NetworkManager.PROTOCOL_VERSION::equals, NetworkManager.PROTOCOL_VERSION::equals);

    @SuppressWarnings("UnusedAssignment")
    @SubscribeEvent
    public static void registerNetworkStuff(FMLCommonSetupEvent event) {
        int index = 0;
        INSTANCE.registerMessage(index++, TotemPacket.class, TotemPacket::encode, TotemPacket::decode, TotemPacket::whenThisPacketIsReceived);
        INSTANCE.registerMessage(index++, CompressionPacket.class, CompressionPacket::encode, CompressionPacket::decode, CompressionPacket::whenThisPacketIsReceived);
        INSTANCE.registerMessage(index++, PlayerInventoryChangedPacket.class, PlayerInventoryChangedPacket::encode, PlayerInventoryChangedPacket::decode, PlayerInventoryChangedPacket::whenThisPacketIsReceived);
        INSTANCE.registerMessage(index++, ExperienceHarvestPacket.class, ExperienceHarvestPacket::encode, ExperienceHarvestPacket::decode, ExperienceHarvestPacket::whenThisPacketIsReceived);
        INSTANCE.registerMessage(index++, TeleporterLinkPacket.class, TeleporterLinkPacket::encode, TeleporterLinkPacket::decode, TeleporterLinkPacket::whenThisPacketIsReceived);
        INSTANCE.registerMessage(index++, TeleporterAutoDeletePacket.class, TeleporterAutoDeletePacket::encode, TeleporterAutoDeletePacket::decode, TeleporterAutoDeletePacket::whenThisPacketIsReceived);
        INSTANCE.registerMessage(index++, ConverterPacket.class, ConverterPacket::encode, ConverterPacket::decode, ConverterPacket::whenThisPacketIsReceived);
        INSTANCE.registerMessage(index++, WaterPumpActivationPacket.class, WaterPumpActivationPacket::encode, WaterPumpActivationPacket::decode, WaterPumpActivationPacket::whenThisPacketIsReceived);
        INSTANCE.registerMessage(index++, NuclearActivatePacket.class, NuclearActivatePacket::encode, NuclearActivatePacket::decode, NuclearActivatePacket::whenThisPacketIsReceived);
        INSTANCE.registerMessage(index++, NuclearBarStatusPacket.class, NuclearBarStatusPacket::encode, NuclearBarStatusPacket::decode, NuclearBarStatusPacket::whenThisPacketIsReceived);
        INSTANCE.registerMessage(index++, DimensionIDPacket.class, DimensionIDPacket::encode, DimensionIDPacket::decode, DimensionIDPacket::whenThisPacketIsReceived);
        INSTANCE.registerMessage(index++, SkyHighPacket.class, SkyHighPacket::encode, SkyHighPacket::decode, SkyHighPacket::whenThisPacketIsReceived);
        INSTANCE.registerMessage(index++, PurificationPacket.class, PurificationPacket::encode, PurificationPacket::decode, PurificationPacket::whenThisPacketIsReceived);
    }
}
