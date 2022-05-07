package net.reikeb.electrona.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.reikeb.electrona.containers.ConverterContainer;

import java.util.function.Supplier;

public class ConverterPacket {

    public ConverterPacket() {
    }

    public static ConverterPacket decode(FriendlyByteBuf buf) {
        return new ConverterPacket();
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player playerEntity = context.get().getSender();
            if ((playerEntity == null) || (!(playerEntity.containerMenu instanceof ConverterContainer))) return;
            ((ConverterContainer) playerEntity.containerMenu).setToOthers(((ConverterContainer) playerEntity.containerMenu).isToVP());
            ((ConverterContainer) playerEntity.containerMenu).setToVP(!((ConverterContainer) playerEntity.containerMenu).isToVP());
        });
        context.get().setPacketHandled(true);
    }
}
