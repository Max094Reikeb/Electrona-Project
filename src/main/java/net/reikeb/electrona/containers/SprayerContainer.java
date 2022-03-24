package net.reikeb.electrona.containers;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import net.reikeb.electrona.init.ItemInit;

import static net.reikeb.electrona.init.ContainerInit.SPRAYER_CONTAINER;

public class SprayerContainer extends AbstractContainer {

    private final ContainerData sprayerData;

    public SprayerContainer(int id, Inventory inv) {
        this(id, inv, new SimpleContainer(4), new SimpleContainerData(2));
    }

    public SprayerContainer(int id, Inventory inv, Container container, ContainerData containerData) {
        super(SPRAYER_CONTAINER.get(), id, 4);

        this.sprayerData = containerData;

        this.addSlot(new Slots.BasicInputSlot(container, 0, 80, 14));
        this.addSlot(new WirelessSlot(container, 1, 20, 44));
        this.addSlot(new WirelessSlot(container, 2, 50, 44));
        this.addSlot(new WirelessSlot(container, 3, 80, 44));

        this.layoutPlayerInventorySlots(inv);
    }

    public int getElectronicPower() {
        return this.sprayerData.get(0);
    }

    public int getRadius() {
        return this.sprayerData.get(1);
    }

    static class WirelessSlot extends Slot {
        public WirelessSlot(Container container, int id, int x, int y) {
            super(container, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return itemStack.getItem() == ItemInit.WIRELESS_BOOSTER.get();
        }

        public int getMaxStackSize() {
            return 1;
        }
    }
}
