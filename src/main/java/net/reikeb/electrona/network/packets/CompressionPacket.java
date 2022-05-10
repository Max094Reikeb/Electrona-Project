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
            ServerPlayer serverPlayer = context.get().getSender();
            if (serverPlayer == null) return;
            TTriggers.FIRST_COMPRESSION.trigger(serverPlayer);
        });
        context.get().setPacketHandled(true);
    }
}
