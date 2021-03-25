package net.reikeb.electrona.network.packets;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.fml.network.NetworkEvent;

import net.reikeb.electrona.containers.WaterPumpContainer;

import java.util.function.Supplier;

public class WaterPumpActivationPacket {

    public WaterPumpActivationPacket() {
    }

    public static WaterPumpActivationPacket decode(PacketBuffer buf) {
        return new WaterPumpActivationPacket();
    }

    public void encode(PacketBuffer buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            PlayerEntity playerEntity = context.get().getSender();
            if ((playerEntity == null) || (!(playerEntity.containerMenu instanceof WaterPumpContainer))) return;
            TileEntity tileEntity = ((WaterPumpContainer) playerEntity.containerMenu).getTileEntity();
            tileEntity.getTileData().putBoolean("isOn", !(tileEntity.getTileData().getBoolean("isOn")));
        });
        context.get().setPacketHandled(true);
    }
}
