package net.reikeb.electrona.network.packets;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.*;

import net.minecraftforge.fml.network.NetworkEvent;

import net.reikeb.electrona.containers.XPGeneratorContainer;

import java.util.function.Supplier;

public class ExperienceHarvestPacket {

    public ExperienceHarvestPacket() {
    }

    public static ExperienceHarvestPacket decode(PacketBuffer buf) {
        return new ExperienceHarvestPacket();
    }

    public void encode(PacketBuffer buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            PlayerEntity playerEntity = context.get().getSender();
            if ((playerEntity == null) || (!(playerEntity.containerMenu instanceof XPGeneratorContainer))) return;
            TileEntity tileEntity = ((XPGeneratorContainer) playerEntity.containerMenu).getTileEntity();
            int xpLevels = tileEntity.getTileData().getInt("XPLevels");
            if (xpLevels > 0) {
                playerEntity.closeContainer();
                playerEntity.displayClientMessage(new StringTextComponent((xpLevels + "" +
                        (new TranslationTextComponent("electrona.block.xp_generator.levels_harvested_success_info").getString()))), false);
                playerEntity.giveExperienceLevels(xpLevels);
                tileEntity.getTileData().putInt("XPLevels", 0);
            } else {
                playerEntity.displayClientMessage(new StringTextComponent(
                        (new TranslationTextComponent("electrona.block.xp_generator.levels_none_to_harvest_info").getString())), false);
            }
        });
        context.get().setPacketHandled(true);
    }
}
