package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.CapabilityItemHandler;
import net.reikeb.electrona.blockentities.XPGeneratorBlockEntity;
import net.reikeb.maxilib.abs.AbstractContainer;
import net.reikeb.maxilib.inventory.Slots;

import static net.reikeb.electrona.init.ContainerInit.XP_GENERATOR_CONTAINER;

public class XPGeneratorContainer extends AbstractContainer {

    public XPGeneratorBlockEntity xpGeneratorBlockEntity;

    public XPGeneratorContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(XP_GENERATOR_CONTAINER.get(), id, 1);

        this.xpGeneratorBlockEntity = (XPGeneratorBlockEntity) player.getCommandSenderWorld().getBlockEntity(pos);
        if (xpGeneratorBlockEntity == null) return;

        xpGeneratorBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new Slots(h, 0, 81, 19, c -> c.getItem() == Items.EMERALD));
        });

        this.layoutPlayerInventorySlots(inv);
        this.addSyncedInt(xpGeneratorBlockEntity::setHundredEnergy, xpGeneratorBlockEntity::getHundredEnergy);
        this.addSyncedInt(xpGeneratorBlockEntity::setWait, xpGeneratorBlockEntity::getWait);
        this.addSyncedInt(xpGeneratorBlockEntity::setXpLevels, xpGeneratorBlockEntity::getXpLevels);
    }

    public double getEnergy() {
        return xpGeneratorBlockEntity.getEnergy();
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
