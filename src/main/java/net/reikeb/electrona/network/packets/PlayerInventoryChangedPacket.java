package net.reikeb.electrona.network.packets;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;

import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerInventoryChangedPacket {

    public PlayerInventoryChangedPacket() {
    }

    public static PlayerInventoryChangedPacket decode(PacketBuffer buf) {
        return new PlayerInventoryChangedPacket();
    }

    public void encode(PacketBuffer buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayerEntity playerEntity = context.get().getSender();
            if (playerEntity == null) return;
            playerEntity.inventory.setChanged();
        });
        context.get().setPacketHandled(true);
    }
}
