package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

import net.minecraftforge.items.CapabilityItemHandler;

import net.reikeb.electrona.tileentities.TileCompressor;

import static net.reikeb.electrona.init.ContainerInit.COMPRESSOR_CONTAINER;

public class CompressorContainer extends AbstractContainer {

    public TileCompressor tileCompressor;

    public CompressorContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(COMPRESSOR_CONTAINER.get(), id, 3);

        this.tileCompressor = (TileCompressor) player.getCommandSenderWorld().getBlockEntity(pos);
        if (tileCompressor == null) return;

        tileCompressor.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new BasicInputSlot(h, 0, 27, 39));
            addSlot(new BasicInputSlot(h, 1, 81, 39));
            addSlot(new CompressorOutputSlot(h, 2, 135, 39));
        });

        this.layoutPlayerInventorySlots(inv);
        this.addSyncedInt(tileCompressor::setElectronicPower, tileCompressor::getElectronicPower);
        this.addSyncedInt(tileCompressor::setCompressingTime, tileCompressor::getCompressingTime);
        this.addSyncedInt(tileCompressor::setCurrentCompressingTime, tileCompressor::getCurrentCompressingTime);
    }

    public int getElectronicPower() {
        return tileCompressor.getElectronicPower();
    }

    public int getCompressingTime() {
        return tileCompressor.getCompressingTime();
    }

    public int getCurrentCompressingTime() {
        return tileCompressor.getCurrentCompressingTime();
    }
}
