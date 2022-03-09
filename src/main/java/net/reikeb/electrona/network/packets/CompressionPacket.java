package net.reikeb.electrona.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import net.minecraftforge.network.NetworkEvent;

import net.reikeb.electrona.advancements.TTriggers;

import java.util.function.Supplier;

public class CompressionPacket {

    public CompressionPacket() {
    }

    public static CompressionPacket decode(FriendlyByteBuf buf) {
        return new CompressionPacket();
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer playerEntity = context.get().getSender();
            if (playerEntity == null) return;
            TTriggers.FIRST_COMPRESSION.trigger(playerEntity);
        });
        context.get().setPacketHandled(true);
    }
}
