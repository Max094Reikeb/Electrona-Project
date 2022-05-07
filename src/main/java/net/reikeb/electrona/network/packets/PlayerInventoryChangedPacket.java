package net.reikeb.electrona.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerInventoryChangedPacket {

    public PlayerInventoryChangedPacket() {
    }

    public static PlayerInventoryChangedPacket decode(FriendlyByteBuf buf) {
        return new PlayerInventoryChangedPacket();
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer playerEntity = context.get().getSender();
            if (playerEntity == null) return;
            playerEntity.inventory.setChanged();
        });
        context.get().setPacketHandled(true);
    }
}
