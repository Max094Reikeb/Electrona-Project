package net.reikeb.electrona.network.packets;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import net.minecraftforge.fmllegacy.network.NetworkEvent;

import net.reikeb.electrona.misc.Keys;

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
            if (advancement == null) System.out.println("Advancement SkyHigh! seems to be null");
            if (advancement == null) return;
            AdvancementProgress advancementProgress = entity.getAdvancements().getOrStartProgress(advancement);
            if (!advancementProgress.isDone()) {
                for (String criteria : advancementProgress.getRemainingCriteria()) {
                    entity.getAdvancements().award(advancement, criteria);
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
