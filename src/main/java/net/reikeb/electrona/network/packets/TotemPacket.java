package net.reikeb.electrona.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

import net.minecraftforge.fmllegacy.network.NetworkEvent;

import net.reikeb.electrona.init.ItemInit;

import java.util.function.Supplier;

public class TotemPacket {

    public TotemPacket() {
    }

    public static TotemPacket decode(FriendlyByteBuf buf) {
        return new TotemPacket();
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Minecraft.getInstance().gameRenderer.displayItemActivation(ItemInit.ADVANCED_TOTEM_OF_UNDYING.get().getDefaultInstance());
        });
        context.get().setPacketHandled(true);
    }
}
