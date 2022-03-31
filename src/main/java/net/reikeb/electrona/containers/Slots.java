package net.reikeb.electrona.containers;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import net.minecraftforge.items.IItemHandler;

public class Slots {

    private static final Container emptyInventory = new SimpleContainer(0);

    static class BatterySlot extends Slot {
        public BatterySlot(IItemHandler itemHandler, int id, int x, int y) {
            super(emptyInventory, id, x, y);
        }

        public BatterySlot(Container container, int id, int x, int y) {
            super(container, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return true;
        }

        public int getMaxStackSize() {
            return 1;
        }
    }

    static class BasicInputSlot extends Slot {
        public BasicInputSlot(IItemHandler itemHandler, int id, int x, int y) {
            super(emptyInventory, id, x, y);
        }

        public BasicInputSlot(Container container, int id, int x, int y) {
            super(container, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return true;
        }
    }

    static class BucketSlot extends Slot {
        public BucketSlot(IItemHandler itemHandler, int id, int x, int y) {
            super(emptyInventory, id, x, y);
        }

        public BucketSlot(Container container, int id, int x, int y) {
            super(container, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return Items.BUCKET == itemStack.getItem();
        }

        public int getMaxStackSize() {
            return 1;
        }
    }

    static class WaterBucketSlot extends Slot {
        public WaterBucketSlot(IItemHandler itemHandler, int id, int x, int y) {
            super(emptyInventory, id, x, y);
        }

        public WaterBucketSlot(Container container, int id, int x, int y) {
            super(container, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return Items.WATER_BUCKET == itemStack.getItem();
        }

        public int getMaxStackSize() {
            return 1;
        }
    }
}
