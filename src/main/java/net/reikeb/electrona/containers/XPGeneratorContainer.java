package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.CapabilityItemHandler;
import net.reikeb.electrona.blockentities.XPGeneratorBlockEntity;

import static net.reikeb.electrona.init.ContainerInit.XP_GENERATOR_CONTAINER;

public class XPGeneratorContainer extends AbstractContainer {

    public XPGeneratorBlockEntity xpGeneratorBlockEntity;

    public XPGeneratorContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(XP_GENERATOR_CONTAINER.get(), id, 1);

        this.xpGeneratorBlockEntity = (XPGeneratorBlockEntity) player.getCommandSenderWorld().getBlockEntity(pos);
        if (xpGeneratorBlockEntity == null) return;

        xpGeneratorBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new EmeraldSlot(h, 0, 81, 19));
        });

        this.layoutPlayerInventorySlots(inv);
        this.addSyncedInt(xpGeneratorBlockEntity::setElectronicPowerTimesHundred, xpGeneratorBlockEntity::getElectronicPowerTimesHundred);
        this.addSyncedInt(xpGeneratorBlockEntity::setWait, xpGeneratorBlockEntity::getWait);
        this.addSyncedInt(xpGeneratorBlockEntity::setXpLevels, xpGeneratorBlockEntity::getXpLevels);
    }

    public double getElectronicPower() {
        return xpGeneratorBlockEntity.getElectronicPower();
    }

    public int getWait() {
        return xpGeneratorBlockEntity.getWait();
    }

    public int getXpLevels() {
        return xpGeneratorBlockEntity.getXpLevels();
    }

    public void setXpLevels(int level) {
        xpGeneratorBlockEntity.setXpLevels(level);
    }
}
