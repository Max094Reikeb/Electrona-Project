package net.reikeb.electrona.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;

import net.minecraftforge.fml.network.NetworkEvent;

import net.reikeb.electrona.init.ItemInit;

import java.util.function.Supplier;

public class TotemPacket {

    public TotemPacket() {
    }

    public static TotemPacket decode(PacketBuffer buf) {
        return new TotemPacket();
    }

    public void encode(PacketBuffer buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Minecraft.getInstance().gameRenderer.displayItemActivation(ItemInit.ADVANCED_TOTEM_OF_UNDYING.get().getDefaultInstance());
        });
        context.get().setPacketHandled(true);
    }
}
