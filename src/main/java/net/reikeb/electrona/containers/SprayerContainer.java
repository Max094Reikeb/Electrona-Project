package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.CapabilityItemHandler;
import net.reikeb.electrona.blockentities.SprayerBlockEntity;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.maxilib.abs.AbstractContainer;
import net.reikeb.maxilib.inventory.Slots;

import static net.reikeb.electrona.init.ContainerInit.SPRAYER_CONTAINER;

public class SprayerContainer extends AbstractContainer {

    public SprayerBlockEntity sprayerBlockEntity;

    public SprayerContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(SPRAYER_CONTAINER.get(), id, 4);

        this.sprayerBlockEntity = (SprayerBlockEntity) player.getCommandSenderWorld().getBlockEntity(pos);
        if (sprayerBlockEntity == null) return;

        sprayerBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new Slots(h, 0, 80, 14));
            addSlot(new Slots(h, 1, 20, 44, c -> c.getItem() == ItemInit.WIRELESS_BOOSTER.get(), 1));
            addSlot(new Slots(h, 2, 50, 44, c -> c.getItem() == ItemInit.WIRELESS_BOOSTER.get(), 1));
            addSlot(new Slots(h, 3, 80, 44, c -> c.getItem() == ItemInit.WIRELESS_BOOSTER.get(), 1));
        });

        this.layoutPlayerInventorySlots(inv);
        this.addSyncedInt(sprayerBlockEntity::setElectronicPowerTimesHundred, sprayerBlockEntity::getElectronicPowerTimesHundred);
        this.addSyncedInt(sprayerBlockEntity::setRadius, sprayerBlockEntity::getRadius);
    }

    public double getElectronicPower() {
        return sprayerBlockEntity.getElectronicPower();
    }

    public int getRadius() {
        return sprayerBlockEntity.getRadius();
    }
}
