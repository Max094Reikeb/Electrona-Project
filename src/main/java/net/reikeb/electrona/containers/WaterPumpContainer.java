package net.reikeb.electrona.containers;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

import static net.reikeb.electrona.init.ContainerInit.WATER_PUMP_CONTAINER;

public class WaterPumpContainer extends AbstractContainer {

    private final ContainerData waterPumpData;

    public WaterPumpContainer(int id, Inventory inv) {
        this(id, inv, new SimpleContainer(2), new SimpleContainerData(3));
    }

    public WaterPumpContainer(int id, Inventory inv, Container container, ContainerData containerData) {
        super(WATER_PUMP_CONTAINER.get(), id, 2);

        this.waterPumpData = containerData;

        this.addSlot(new Slots.BucketSlot(container, 0, 36, 35));
        this.addSlot(new Slots.BatterySlot(container, 1, 137, 29));

        this.layoutPlayerInventorySlots(inv);
    }

    public int getElectronicPower() {
        return this.waterPumpData.get(0);
    }

    public int getWaterLevel() {
        return this.waterPumpData.get(1);
    }

    public boolean isOn() {
        return this.waterPumpData.get(2) == 1;
    }

    public void setOn(boolean isOn) {
        this.waterPumpData.set(2, (isOn ? 1 : 0));
    }
}
