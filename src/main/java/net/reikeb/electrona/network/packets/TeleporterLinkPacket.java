package net.reikeb.electrona.network.packets;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.*;

import net.minecraftforge.fml.network.NetworkEvent;

import net.reikeb.electrona.containers.TeleporterContainer;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.tileentities.TileTeleporter;

import java.util.function.Supplier;

public class TeleporterLinkPacket {

    public TeleporterLinkPacket() {
    }

    public static TeleporterLinkPacket decode(PacketBuffer buf) {
        return new TeleporterLinkPacket();
    }

    public void encode(PacketBuffer buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            PlayerEntity playerEntity = context.get().getSender();
            if ((playerEntity == null) || (!(playerEntity.containerMenu instanceof TeleporterContainer))) return;
            TileTeleporter tileEntity = ((TeleporterContainer) playerEntity.containerMenu).getTileEntity();
            ItemStack stack = tileEntity.getInventory().getStackInSlot(0);
            if (stack.getItem() == ItemInit.TELEPORT_SAVER.get()) {
                tileEntity.getTileData().putDouble("teleportX", stack.getOrCreateTag().getDouble("teleportX"));
                tileEntity.getTileData().putDouble("teleportY", stack.getOrCreateTag().getDouble("teleportY"));
                tileEntity.getTileData().putDouble("teleportZ", stack.getOrCreateTag().getDouble("teleportZ"));
                playerEntity.displayClientMessage(new StringTextComponent((
                        new TranslationTextComponent("message.electrona.coordinates_transfered_info").getString())), true);
            } else if (stack.getItem() == ItemInit.PORTABLE_TELEPORTER.get()) {
                stack.getOrCreateTag().putDouble("teleportX", tileEntity.getTileData().getDouble("teleportX"));
                stack.getOrCreateTag().putDouble("teleportY", tileEntity.getTileData().getDouble("teleportY"));
                stack.getOrCreateTag().putDouble("teleportZ", tileEntity.getTileData().getDouble("teleportZ"));
                playerEntity.displayClientMessage(new StringTextComponent((
                        new TranslationTextComponent("message.electrona.coordinates_transfered_info").getString())), true);
            }
        });
        context.get().setPacketHandled(true);
    }
}
