package net.reikeb.electrona.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import net.minecraftforge.fmllegacy.network.NetworkEvent;

import net.reikeb.electrona.advancements.TTriggers;

import java.util.function.Supplier;

public class PurificationPacket {

    public PurificationPacket() {
    }

    public static PurificationPacket decode(FriendlyByteBuf buf) {
        return new PurificationPacket();
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer playerEntity = context.get().getSender();
            if (playerEntity == null) return;
            TTriggers.A_NEW_MECHANIC.trigger(playerEntity);
        });
        context.get().setPacketHandled(true);
    }
}
