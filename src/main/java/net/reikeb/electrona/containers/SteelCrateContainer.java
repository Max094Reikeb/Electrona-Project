package net.reikeb.electrona.containers;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;

import static net.reikeb.electrona.init.ContainerInit.STEEL_CRATE_CONTAINER;

public class SteelCrateContainer extends AbstractContainer {

    public SteelCrateContainer(int id, Inventory inv) {
        this(id, inv, new SimpleContainer(27));
    }

    public SteelCrateContainer(int id, Inventory inv, Container container) {
        super(STEEL_CRATE_CONTAINER.get(), id, 27);

        this.addSlot(new Slots.BasicInputSlot(container, 0, 8, 18));
        this.addSlot(new Slots.BasicInputSlot(container, 1, 26, 18));
        this.addSlot(new Slots.BasicInputSlot(container, 2, 44, 18));
        this.addSlot(new Slots.BasicInputSlot(container, 3, 62, 18));
        this.addSlot(new Slots.BasicInputSlot(container, 4, 80, 18));
        this.addSlot(new Slots.BasicInputSlot(container, 5, 98, 18));
        this.addSlot(new Slots.BasicInputSlot(container, 6, 116, 18));
        this.addSlot(new Slots.BasicInputSlot(container, 7, 134, 18));
        this.addSlot(new Slots.BasicInputSlot(container, 8, 152, 18));
        this.addSlot(new Slots.BasicInputSlot(container, 9, 8, 36));
        this.addSlot(new Slots.BasicInputSlot(container, 10, 26, 36));
        this.addSlot(new Slots.BasicInputSlot(container, 11, 44, 36));
        this.addSlot(new Slots.BasicInputSlot(container, 12, 62, 36));
        this.addSlot(new Slots.BasicInputSlot(container, 13, 80, 36));
        this.addSlot(new Slots.BasicInputSlot(container, 14, 98, 36));
        this.addSlot(new Slots.BasicInputSlot(container, 15, 116, 36));
        this.addSlot(new Slots.BasicInputSlot(container, 16, 134, 36));
        this.addSlot(new Slots.BasicInputSlot(container, 17, 152, 36));
        this.addSlot(new Slots.BasicInputSlot(container, 18, 8, 54));
        this.addSlot(new Slots.BasicInputSlot(container, 19, 26, 54));
        this.addSlot(new Slots.BasicInputSlot(container, 20, 44, 54));
        this.addSlot(new Slots.BasicInputSlot(container, 21, 62, 54));
        this.addSlot(new Slots.BasicInputSlot(container, 22, 80, 54));
        this.addSlot(new Slots.BasicInputSlot(container, 23, 98, 54));
        this.addSlot(new Slots.BasicInputSlot(container, 24, 116, 54));
        this.addSlot(new Slots.BasicInputSlot(container, 25, 134, 54));
        this.addSlot(new Slots.BasicInputSlot(container, 26, 152, 54));

        this.layoutPlayerInventorySlots(inv);
    }
}
