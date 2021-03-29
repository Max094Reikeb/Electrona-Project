package net.reikeb.electrona.network.packets;

import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.fml.network.NetworkEvent;

import net.reikeb.electrona.containers.DimensionLinkerContainer;

import java.util.function.Supplier;

public class DimensionIDPacket {

    public DimensionIDPacket() {
    }

    public static DimensionIDPacket decode(PacketBuffer buf) {
        return new DimensionIDPacket();
    }

    public void encode(PacketBuffer buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            PlayerEntity playerEntity = context.get().getSender();
            if ((playerEntity == null) || (!(playerEntity.containerMenu instanceof DimensionLinkerContainer))) return;
            TileEntity tileEntity = ((DimensionLinkerContainer) playerEntity.containerMenu).getTileEntity();
            TextFieldWidget textField = (TextFieldWidget) ((DimensionLinkerContainer) playerEntity.containerMenu).getTextFieldWidget().get("text:dimension_id");
            tileEntity.getTileData().putString("dimensionID", textField.getValue());
        });
        context.get().setPacketHandled(true);
    }
}
