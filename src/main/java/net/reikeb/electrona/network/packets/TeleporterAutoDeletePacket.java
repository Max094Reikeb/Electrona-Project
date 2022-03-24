package net.reikeb.electrona.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import net.minecraftforge.network.NetworkEvent;

import net.reikeb.electrona.containers.TeleporterContainer;

import java.util.function.Supplier;

public class TeleporterAutoDeletePacket {

    public TeleporterAutoDeletePacket() {
    }

    public static TeleporterAutoDeletePacket decode(FriendlyByteBuf buf) {
        return new TeleporterAutoDeletePacket();
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player playerEntity = context.get().getSender();
            if ((playerEntity == null) || (!(playerEntity.containerMenu instanceof TeleporterContainer))) return;
            ((TeleporterContainer) playerEntity.containerMenu).setAutoDelete(!((TeleporterContainer) playerEntity.containerMenu).isAutoDelete());
        });
        context.get().setPacketHandled(true);
    }
}
