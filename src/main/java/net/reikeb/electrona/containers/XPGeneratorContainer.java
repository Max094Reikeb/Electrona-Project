package net.reikeb.electrona.containers;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import static net.reikeb.electrona.init.ContainerInit.XP_GENERATOR_CONTAINER;

public class XPGeneratorContainer extends AbstractContainer {

    private final ContainerData xpGeneratorData;

    public XPGeneratorContainer(int id, Inventory inv) {
        this(id, inv, new SimpleContainer(1), new SimpleContainerData(3));
    }

    public XPGeneratorContainer(int id, Inventory inv, Container container, ContainerData containerData) {
        super(XP_GENERATOR_CONTAINER.get(), id, 1);

        this.xpGeneratorData = containerData;

        this.addSlot(new EmeraldSlot(container, 0, 81, 19));

        this.layoutPlayerInventorySlots(inv);
    }

    public int getElectronicPower() {
        return this.xpGeneratorData.get(0);
    }

    public int getWait() {
        return this.xpGeneratorData.get(1);
    }

    public int getXpLevels() {
        return this.xpGeneratorData.get(2);
    }

    public void setXpLevels(int level) {
        this.xpGeneratorData.set(2, level);
    }

    static class EmeraldSlot extends Slot {
        public EmeraldSlot(Container container, int id, int x, int y) {
            super(container, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return (itemStack.getItem() == Items.EMERALD);
        }

    }
}
