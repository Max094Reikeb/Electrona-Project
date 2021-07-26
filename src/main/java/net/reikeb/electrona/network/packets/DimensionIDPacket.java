package net.reikeb.electrona.network.packets;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraftforge.fmllegacy.network.NetworkEvent;

import net.reikeb.electrona.containers.DimensionLinkerContainer;

import java.util.function.Supplier;

public class DimensionIDPacket {

    public DimensionIDPacket() {
    }

    public static DimensionIDPacket decode(FriendlyByteBuf buf) {
        return new DimensionIDPacket();
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player playerEntity = context.get().getSender();
            if ((playerEntity == null) || (!(playerEntity.containerMenu instanceof DimensionLinkerContainer))) return;
            BlockEntity tileEntity = ((DimensionLinkerContainer) playerEntity.containerMenu).getTileEntity();
            EditBox textField = (EditBox) ((DimensionLinkerContainer) playerEntity.containerMenu).getTextFieldWidget().get("text:dimension_id");
            tileEntity.getTileData().putString("dimensionID", textField.getValue());
        });
        context.get().setPacketHandled(true);
    }
}
