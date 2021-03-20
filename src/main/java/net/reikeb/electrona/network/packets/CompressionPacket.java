package net.reikeb.electrona.network.packets;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;

import net.minecraftforge.fml.network.NetworkEvent;

import net.reikeb.electrona.advancements.TTriggers;

import java.util.function.Supplier;

public class CompressionPacket {

    public CompressionPacket() {
    }

    public static CompressionPacket decode(PacketBuffer buf) {
        return new CompressionPacket();
    }

    public void encode(PacketBuffer buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayerEntity playerEntity = context.get().getSender();
            if (playerEntity == null) return;
            TTriggers.FIRST_COMPRESSION.trigger(playerEntity);
        });
        context.get().setPacketHandled(true);
    }
}
