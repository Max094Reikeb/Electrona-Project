package net.reikeb.electrona.containers;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class Slots {

    static class BatterySlot extends Slot {
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
        public BasicInputSlot(Container container, int id, int x, int y) {
            super(container, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return true;
        }
    }

    static class BucketSlot extends Slot {
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
