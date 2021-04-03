package net.reikeb.electrona.network.packets;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;

import net.minecraftforge.fml.network.NetworkEvent;

import net.reikeb.electrona.advancements.TTriggers;

import java.util.function.Supplier;

public class PurificationPacket {

    public PurificationPacket() {
    }

    public static PurificationPacket decode(PacketBuffer buf) {
        return new PurificationPacket();
    }

    public void encode(PacketBuffer buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayerEntity playerEntity = context.get().getSender();
            if (playerEntity == null) return;
            TTriggers.A_NEW_MECHANIC.trigger(playerEntity);
        });
        context.get().setPacketHandled(true);
    }
}
