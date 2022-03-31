package net.reikeb.electrona.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.network.NetworkEvent;

import net.reikeb.electrona.blockentities.TileCooler;
import net.reikeb.electrona.blockentities.TileNuclearGeneratorController;
import net.reikeb.electrona.containers.NuclearGeneratorControllerContainer;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class NuclearBarStatusPacket {

    public NuclearBarStatusPacket() {
    }

    public static NuclearBarStatusPacket decode(FriendlyByteBuf buf) {
        return new NuclearBarStatusPacket();
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player playerEntity = context.get().getSender();
            if ((playerEntity == null) || (!(playerEntity.containerMenu instanceof NuclearGeneratorControllerContainer)))
                return;
            BlockPos posUnder = new BlockPos(((NuclearGeneratorControllerContainer) playerEntity.containerMenu).getPosXUnder(),
                    ((NuclearGeneratorControllerContainer) playerEntity.containerMenu).getPosYUnder(),
                    ((NuclearGeneratorControllerContainer) playerEntity.containerMenu).getPosZUnder());
            BlockPos blockPos = posUnder.above();
            BlockEntity blockEntity = playerEntity.level.getBlockEntity(blockPos);
            BlockEntity tileUnder = playerEntity.level.getBlockEntity(posUnder);
            if ((!(tileUnder instanceof TileCooler)) || (!(blockEntity instanceof TileNuclearGeneratorController)))
                return;

            AtomicReference<ItemStack> stackInSlot1 = new AtomicReference<>();
            blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                stackInSlot1.set(h.getStackInSlot(1));
            });

            if (((NuclearGeneratorControllerContainer) playerEntity.containerMenu).areUBIn()) {
                ((NuclearGeneratorControllerContainer) playerEntity.containerMenu).setUBIn(false);
                tileUnder.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    h.extractItem(0, 1, false);
                });
            } else if (((NuclearGeneratorControllerContainer) playerEntity.containerMenu).isAboveCooler()) {
                ((NuclearGeneratorControllerContainer) playerEntity.containerMenu).setUBIn(true);
                tileUnder.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    h.insertItem(0, stackInSlot1.get(), false);
                });
            }
        });
        context.get().setPacketHandled(true);
    }
}
