package net.reikeb.electrona.containers;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

import static net.reikeb.electrona.init.ContainerInit.BATTERY_CONTAINER;

public class BatteryContainer extends AbstractContainer {

    private final ContainerData batteryData;

    public BatteryContainer(int id, Inventory inv) {
        this(id, inv, new SimpleContainer(2), new SimpleContainerData(1));
    }

    public BatteryContainer(int id, Inventory inv, Container container, ContainerData containerData) {
        super(BATTERY_CONTAINER.get(), id, 2);

        this.batteryData = containerData;

        this.addSlot(new Slots.BatterySlot(container, 0, 45, 33));
        this.addSlot(new Slots.BatterySlot(container, 1, 117, 33));

        this.layoutPlayerInventorySlots(inv);
    }

    public int getElectronicPower() {
        return this.batteryData.get(0);
    }
}
