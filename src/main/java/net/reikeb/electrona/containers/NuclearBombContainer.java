package net.reikeb.electrona.containers;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;

import static net.reikeb.electrona.init.ContainerInit.NUCLEAR_BOMB_CONTAINER;

public class NuclearBombContainer extends AbstractContainer {

    public NuclearBombContainer(int id, Inventory inv) {
        this(id, inv, new SimpleContainer(2));
    }

    public NuclearBombContainer(int id, Inventory inv, Container container) {
        super(NUCLEAR_BOMB_CONTAINER.get(), id, 2);

        this.addSlot(new Slots.BasicInputSlot(container, 0, 53, 36));
        this.addSlot(new Slots.BasicInputSlot(container, 1, 96, 36));

        this.layoutPlayerInventorySlots(inv);
    }
}
