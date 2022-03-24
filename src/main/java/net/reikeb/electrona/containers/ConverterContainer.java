package net.reikeb.electrona.containers;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

import static net.reikeb.electrona.init.ContainerInit.CONVERTER_CONTAINER;

public class ConverterContainer extends AbstractContainer {

    private final ContainerData converterData;

    public ConverterContainer(int id, Inventory inv) {
        this(id, inv, new SimpleContainer(1), new SimpleContainerData(5));
    }

    public ConverterContainer(int id, Inventory inv, Container container, ContainerData containerData) {
        super(CONVERTER_CONTAINER.get(), id, 1);

        this.converterData = containerData;

        this.addSlot(new Slots.BatterySlot(container, 0, 81, 31));

        this.layoutPlayerInventorySlots(inv);
    }

    public int getElectronicPower() {
        return this.converterData.get(0);
    }

    public int getFE() {
        return this.converterData.get(1);
    }

    public int getVP() {
        return this.converterData.get(2);
    }

    public boolean isToVP() {
        return this.converterData.get(3) == 1;
    }

    public void setToVP(boolean isToVP) {
        this.converterData.set(3, (isToVP ? 1 : 0));
    }

    public boolean isToOthers() {
        return this.converterData.get(4) == 1;
    }

    public void setToOthers(boolean isToOthers) {
        this.converterData.set(4, (isToOthers ? 1 : 0));
    }
}
