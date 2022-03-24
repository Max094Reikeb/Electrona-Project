package net.reikeb.electrona.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;

import net.minecraftforge.network.NetworkEvent;

import net.reikeb.electrona.containers.TeleporterContainer;

import java.util.function.Supplier;

public class TeleporterLinkPacket {

    public TeleporterLinkPacket() {
    }

    public static TeleporterLinkPacket decode(FriendlyByteBuf buf) {
        return new TeleporterLinkPacket();
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player playerEntity = context.get().getSender();
            if ((playerEntity == null) || (!(playerEntity.containerMenu instanceof TeleporterContainer))) return;
            if (((TeleporterContainer) playerEntity.containerMenu).isTeleportSaver()) {
                ((TeleporterContainer) playerEntity.containerMenu).setTeleportX(((TeleporterContainer) playerEntity.containerMenu).getItemTeleportX());
                ((TeleporterContainer) playerEntity.containerMenu).setTeleportY(((TeleporterContainer) playerEntity.containerMenu).getItemTeleportY());
                ((TeleporterContainer) playerEntity.containerMenu).setTeleportZ(((TeleporterContainer) playerEntity.containerMenu).getItemTeleportZ());
                playerEntity.displayClientMessage(new TextComponent((
                        new TranslatableComponent("message.electrona.coordinates_transfered_info").getString())), true);
            } else if (((TeleporterContainer) playerEntity.containerMenu).isTeleporter()) {
                ((TeleporterContainer) playerEntity.containerMenu).setItemTeleportX(((TeleporterContainer) playerEntity.containerMenu).getTeleportX());
                ((TeleporterContainer) playerEntity.containerMenu).setItemTeleportY(((TeleporterContainer) playerEntity.containerMenu).getTeleportY());
                ((TeleporterContainer) playerEntity.containerMenu).setItemTeleportZ(((TeleporterContainer) playerEntity.containerMenu).getTeleportZ());
                playerEntity.displayClientMessage(new TextComponent((
                        new TranslatableComponent("message.electrona.coordinates_transfered_info").getString())), true);
            }
        });
        context.get().setPacketHandled(true);
    }
}
