package net.reikeb.electrona.containers;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

import static net.reikeb.electrona.init.ContainerInit.MINING_MACHINE_CONTAINER;

public class MiningMachineContainer extends AbstractContainer {

    private final ContainerData miningMachineData;

    public MiningMachineContainer(int id, Inventory inv) {
        this(id, inv, new SimpleContainer(3), new SimpleContainerData(1));
    }

    public MiningMachineContainer(int id, Inventory inv, Container container, ContainerData containerData) {
        super(MINING_MACHINE_CONTAINER.get(), id, 3);

        this.miningMachineData = containerData;

        this.addSlot(new Slots.BasicInputSlot(container, 0, 91, 12));
        this.addSlot(new Slots.BucketSlot(container, 1, 74, 51));
        this.addSlot(new Slots.BucketSlot(container, 2, 180, 51));

        this.layoutPlayerInventorySlots(inv);
    }

    public int getElectronicPower() {
        return this.miningMachineData.get(0);
    }
}
