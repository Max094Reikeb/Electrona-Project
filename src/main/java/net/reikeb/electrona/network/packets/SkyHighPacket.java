package net.reikeb.electrona.network.packets;

import net.minecraft.advancements.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SkyHighPacket {

    public SkyHighPacket() {
    }

    public static SkyHighPacket decode(PacketBuffer buf) {
        return new SkyHighPacket();
    }

    public void encode(PacketBuffer buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayerEntity entity = context.get().getSender();
            if (entity == null) return;
            Advancement advancement = entity.server.getAdvancements().getAdvancement(new ResourceLocation("electrona:sky_high"));
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
