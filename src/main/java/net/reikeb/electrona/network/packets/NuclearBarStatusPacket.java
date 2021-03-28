package net.reikeb.electrona.network.packets;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.CapabilityItemHandler;

import net.reikeb.electrona.containers.NuclearGeneratorControllerContainer;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.tileentities.TileCooler;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class NuclearBarStatusPacket {

    public NuclearBarStatusPacket() {
    }

    public static NuclearBarStatusPacket decode(PacketBuffer buf) {
        return new NuclearBarStatusPacket();
    }

    public void encode(PacketBuffer buf) {
    }

    public void whenThisPacketIsReceived(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            PlayerEntity playerEntity = context.get().getSender();
            if ((playerEntity == null) || (!(playerEntity.containerMenu instanceof NuclearGeneratorControllerContainer)))
                return;
            TileEntity tileEntity = ((NuclearGeneratorControllerContainer) playerEntity.containerMenu).getTileEntity();
            BlockPos posUnder = new BlockPos(tileEntity.getBlockPos().getX(),
                    (tileEntity.getBlockPos().getY() - 1), tileEntity.getBlockPos().getZ());
            Block blockUnder = (tileEntity.getLevel().getBlockState(posUnder)).getBlock();
            TileEntity tileUnder = playerEntity.level.getBlockEntity(posUnder);
            if (!(tileUnder instanceof TileCooler)) return;
            AtomicReference<ItemStack> stackInSlot1 = new AtomicReference<>();
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                stackInSlot1.set(h.getStackInSlot(1));
            });

            if (tileEntity.getTileData().getBoolean("UBIn")) {
                tileEntity.getTileData().putBoolean("UBIn", false);
                tileUnder.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    h.extractItem(0, 1, false);
                });
            } else if ((!tileEntity.getTileData().getBoolean("UBIn"))
                    && (blockUnder == BlockInit.COOLER.get())) {
                tileEntity.getTileData().putBoolean("UBIn", true);
                tileUnder.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    h.insertItem(0, stackInSlot1.get(), false);
                });
            }
        });
        context.get().setPacketHandled(true);
    }
}
