package net.reikeb.electrona.network.packets;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.fml.network.NetworkEvent;

import net.reikeb.electrona.containers.ConverterContainer;

import java.util.function.Supplier;

public class ConverterPacket {

    public ConverterPacket() {
    }

    public static ConverterPacket decode(PacketBuffer buf) {
        return new ConverterPacket();
    }

    public void encode(PacketBuffer buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            PlayerEntity playerEntity = context.get().getSender();
            if ((playerEntity == null) || (!(playerEntity.containerMenu instanceof ConverterContainer))) return;
            TileEntity tileEntity = ((ConverterContainer) playerEntity.containerMenu).getTileEntity();
            if (tileEntity.getTileData().getBoolean("toVP")) {
                tileEntity.getTileData().putBoolean("toVP", false);
                tileEntity.getTileData().putBoolean("toOthers", true);
            } else {
                tileEntity.getTileData().putBoolean("toVP", true);
                tileEntity.getTileData().putBoolean("toOthers", false);
            }
        });
        context.get().setPacketHandled(true);
    }
}
