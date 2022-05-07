package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.CapabilityItemHandler;
import net.reikeb.electrona.blockentities.CompressorBlockEntity;

import static net.reikeb.electrona.init.ContainerInit.COMPRESSOR_CONTAINER;

public class CompressorContainer extends AbstractContainer {

    public CompressorBlockEntity compressorBlockEntity;

    public CompressorContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(COMPRESSOR_CONTAINER.get(), id, 3);

        this.compressorBlockEntity = (CompressorBlockEntity) player.getCommandSenderWorld().getBlockEntity(pos);
        if (compressorBlockEntity == null) return;

        compressorBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new BasicInputSlot(h, 0, 27, 39));
            addSlot(new BasicInputSlot(h, 1, 81, 39));
            addSlot(new CompressorOutputSlot(h, 2, 135, 39));
        });

        this.layoutPlayerInventorySlots(inv);
        this.addSyncedInt(compressorBlockEntity::setElectronicPowerTimesHundred, compressorBlockEntity::getElectronicPowerTimesHundred);
        this.addSyncedInt(compressorBlockEntity::setCompressingTime, compressorBlockEntity::getCompressingTime);
        this.addSyncedInt(compressorBlockEntity::setCurrentCompressingTime, compressorBlockEntity::getCurrentCompressingTime);
    }

    public double getElectronicPower() {
        return compressorBlockEntity.getElectronicPower();
    }

    public int getCompressingTime() {
        return compressorBlockEntity.getCompressingTime();
    }

    public int getCurrentCompressingTime() {
        return compressorBlockEntity.getCurrentCompressingTime();
    }
}
