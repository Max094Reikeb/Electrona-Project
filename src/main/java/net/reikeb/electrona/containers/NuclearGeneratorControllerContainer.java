package net.reikeb.electrona.containers;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import static net.reikeb.electrona.init.ContainerInit.NUCLEAR_GENERATOR_CONTAINER;

public class NuclearGeneratorControllerContainer extends AbstractContainer {

    private final ContainerData nuclearGeneratorControllerData;

    public NuclearGeneratorControllerContainer(int id, Inventory inv) {
        this(id, inv, new SimpleContainer(2), new SimpleContainerData(10));
    }

    public NuclearGeneratorControllerContainer(int id, Inventory inv, Container container, ContainerData containerData) {
        super(NUCLEAR_GENERATOR_CONTAINER.get(), id, 2);

        this.nuclearGeneratorControllerData = containerData;

        this.addSlot(new Slots.WaterBucketSlot(container, 0, 27, 32));
        this.addSlot(new UraniumSlot(container, containerData, 1, 55, 32));

        this.layoutPlayerInventorySlots(inv);
    }

    public int getElectronicPower() {
        return this.nuclearGeneratorControllerData.get(0);
    }

    public int getUnderWater() {
        return this.nuclearGeneratorControllerData.get(1);
    }

    public int getTemperature() {
        return this.nuclearGeneratorControllerData.get(2);
    }

    public boolean isPowered() {
        return this.nuclearGeneratorControllerData.get(3) == 1;
    }

    public void setPowered(boolean powered) {
        this.nuclearGeneratorControllerData.set(3, (powered ? 1 : 0));
    }

    public boolean areUBIn() {
        return this.nuclearGeneratorControllerData.get(4) == 1;
    }

    public void setUBIn(boolean ubIn) {
        this.nuclearGeneratorControllerData.set(4, (ubIn ? 1 : 0));
    }

    public boolean alert() {
        return this.nuclearGeneratorControllerData.get(5) == 1;
    }

    public boolean isAboveCooler() {
        return this.nuclearGeneratorControllerData.get(6) == 1;
    }

    public int getPosXUnder() {
        return this.nuclearGeneratorControllerData.get(7);
    }

    public int getPosYUnder() {
        return this.nuclearGeneratorControllerData.get(8);
    }

    public int getPosZUnder() {
        return this.nuclearGeneratorControllerData.get(9);
    }

    static class UraniumSlot extends Slot {

        private final ContainerData containerData;

        public UraniumSlot(Container container, ContainerData containerData, int id, int x, int y) {
            super(container, id, x, y);
            this.containerData = containerData;
        }

        public boolean mayPlace(ItemStack itemStack) {
            return containerData.get(4) == 0;
        }

        public boolean mayPickup(Player player) {
            return containerData.get(4) == 0;
        }

        public int getMaxStackSize() {
            return 1;
        }
    }
}
