package net.reikeb.electrona.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraftforge.network.NetworkEvent;

import net.reikeb.electrona.containers.ConverterContainer;

import java.util.function.Supplier;

public class ConverterPacket {

    public ConverterPacket() {
    }

    public static ConverterPacket decode(FriendlyByteBuf buf) {
        return new ConverterPacket();
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player playerEntity = context.get().getSender();
            if ((playerEntity == null) || (!(playerEntity.containerMenu instanceof ConverterContainer))) return;
            BlockEntity tileEntity = ((ConverterContainer) playerEntity.containerMenu).getTileEntity();
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
