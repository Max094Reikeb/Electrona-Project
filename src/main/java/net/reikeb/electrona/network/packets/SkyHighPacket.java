package net.reikeb.electrona.network.packets;

import net.minecraft.advancements.Advancement;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import net.minecraftforge.network.NetworkEvent;

import net.reikeb.electrona.misc.Keys;
import net.reikeb.electrona.utils.ElectronaUtils;

import java.util.function.Supplier;

public class SkyHighPacket {

    public SkyHighPacket() {
    }

    public static SkyHighPacket decode(FriendlyByteBuf buf) {
        return new SkyHighPacket();
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer entity = context.get().getSender();
            if (entity == null) return;
            Advancement advancement = entity.server.getAdvancements().getAdvancement(Keys.SKY_HIGH_ADVANCEMENT);
            ElectronaUtils.awardAdvancement(entity, advancement, "SkyHigh!");
        });
        context.get().setPacketHandled(true);
    }
}
