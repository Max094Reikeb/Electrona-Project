package net.reikeb.electrona.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraftforge.network.NetworkEvent;

import net.reikeb.electrona.containers.NuclearGeneratorControllerContainer;

import java.util.function.Supplier;

public class NuclearActivatePacket {

    public NuclearActivatePacket() {
    }

    public static NuclearActivatePacket decode(FriendlyByteBuf buf) {
        return new NuclearActivatePacket();
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player playerEntity = context.get().getSender();
            if ((playerEntity == null) || (!(playerEntity.containerMenu instanceof NuclearGeneratorControllerContainer)))
                return;
            BlockEntity tileEntity = ((NuclearGeneratorControllerContainer) playerEntity.containerMenu).getTileEntity();
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
