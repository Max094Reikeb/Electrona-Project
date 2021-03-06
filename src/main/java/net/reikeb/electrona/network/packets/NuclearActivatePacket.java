package net.reikeb.electrona.network.packets;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.fml.network.NetworkEvent;

import net.reikeb.electrona.containers.NuclearGeneratorControllerContainer;

import java.util.function.Supplier;

public class NuclearActivatePacket {

    public NuclearActivatePacket() {
    }

    public static NuclearActivatePacket decode(PacketBuffer buf) {
        return new NuclearActivatePacket();
    }

    public void encode(PacketBuffer buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            PlayerEntity playerEntity = context.get().getSender();
            if ((playerEntity == null) || (!(playerEntity.containerMenu instanceof NuclearGeneratorControllerContainer)))
                return;
            TileEntity tileEntity = ((NuclearGeneratorControllerContainer) playerEntity.containerMenu).getTileEntity();
            if (tileEntity.getTileData().getBoolean("powered")) {
                if (!tileEntity.getTileData().getBoolean("UBIn")) {
                    tileEntity.getTileData().putBoolean("powered", false);
                }
            } else {
                tileEntity.getTileData().putBoolean("powered", true);
            }
        });
        context.get().setPacketHandled(true);
    }
}
