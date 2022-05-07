package net.reikeb.electrona.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.reikeb.electrona.containers.XPGeneratorContainer;

import java.util.function.Supplier;

public class ExperienceHarvestPacket {

    public ExperienceHarvestPacket() {
    }

    public static ExperienceHarvestPacket decode(FriendlyByteBuf buf) {
        return new ExperienceHarvestPacket();
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player playerEntity = context.get().getSender();
            if ((playerEntity == null) || (!(playerEntity.containerMenu instanceof XPGeneratorContainer))) return;
            int xpLevels = ((XPGeneratorContainer) playerEntity.containerMenu).getXpLevels();
            if (xpLevels > 0) {
                playerEntity.closeContainer();
                playerEntity.displayClientMessage(new TextComponent((xpLevels + "" +
                        (new TranslatableComponent("message.electrona.levels_harvested_success_info").getString()))), false);
                playerEntity.giveExperienceLevels(xpLevels);
                ((XPGeneratorContainer) playerEntity.containerMenu).setXpLevels(0);
            } else {
                playerEntity.displayClientMessage(new TextComponent(
                        (new TranslatableComponent("message.electrona.levels_none_to_harvest_info").getString())), false);
            }
        });
        context.get().setPacketHandled(true);
    }
}
