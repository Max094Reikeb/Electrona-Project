package net.reikeb.electrona.network.packets;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.fml.network.NetworkEvent;

import net.reikeb.electrona.containers.TeleporterContainer;

import java.util.function.Supplier;

public class TeleporterAutoDeletePacket {

    public TeleporterAutoDeletePacket() {
    }

    public static TeleporterAutoDeletePacket decode(PacketBuffer buf) {
        return new TeleporterAutoDeletePacket();
    }

    public void encode(PacketBuffer buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            PlayerEntity playerEntity = context.get().getSender();
            if ((playerEntity == null) || (!(playerEntity.containerMenu instanceof TeleporterContainer))) return;
            TileEntity tileEntity = ((TeleporterContainer) playerEntity.containerMenu).getTileEntity();
            tileEntity.getTileData().putBoolean("autoDeletion", !tileEntity.getTileData().getBoolean("autoDeletion"));
        });
        context.get().setPacketHandled(true);
    }
}
