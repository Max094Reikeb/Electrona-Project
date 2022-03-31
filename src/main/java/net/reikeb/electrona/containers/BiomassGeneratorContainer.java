package net.reikeb.electrona.containers;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

import static net.reikeb.electrona.init.ContainerInit.BIOMASS_GENERATOR_CONTAINER;

public class BiomassGeneratorContainer extends AbstractContainer {

    private final ContainerData biomassGeneratorData;

    public BiomassGeneratorContainer(int id, Inventory inv) {
        this(id, inv, new SimpleContainer(1), new SimpleContainerData(1));
    }

    public BiomassGeneratorContainer(int id, Inventory inv, Container container, ContainerData containerData) {
        super(BIOMASS_GENERATOR_CONTAINER.get(), id, 1);

        this.biomassGeneratorData = containerData;

        // this.addSlot(new Slots.BasicInputSlot(container, 0, 80, 36));

        this.layoutPlayerInventorySlots(inv);
    }

    public int getElectronicPower() {
        return this.biomassGeneratorData.get(0);
    }
}
