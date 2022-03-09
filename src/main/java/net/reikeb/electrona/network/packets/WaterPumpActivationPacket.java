package net.reikeb.electrona.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraftforge.network.NetworkEvent;

import net.reikeb.electrona.containers.WaterPumpContainer;

import java.util.function.Supplier;

public class WaterPumpActivationPacket {

    public WaterPumpActivationPacket() {
    }

    public static WaterPumpActivationPacket decode(FriendlyByteBuf buf) {
        return new WaterPumpActivationPacket();
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player playerEntity = context.get().getSender();
            if ((playerEntity == null) || (!(playerEntity.containerMenu instanceof WaterPumpContainer))) return;
            BlockEntity tileEntity = ((WaterPumpContainer) playerEntity.containerMenu).getTileEntity();
            tileEntity.getTileData().putBoolean("isOn", !(tileEntity.getTileData().getBoolean("isOn")));
        });
        context.get().setPacketHandled(true);
    }
}
