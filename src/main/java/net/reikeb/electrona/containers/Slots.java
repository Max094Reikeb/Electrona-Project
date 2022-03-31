package net.reikeb.electrona.containers;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class Slots {

    static class BatterySlot extends SlotItemHandler {
        public BatterySlot(IItemHandler itemHandler, int id, int x, int y) {
            super(itemHandler, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return true;
        }

        public int getMaxStackSize() {
            return 1;
        }
    }

    static class BasicInputSlot extends SlotItemHandler {
        public BasicInputSlot(IItemHandler itemHandler, int id, int x, int y) {
            super(itemHandler, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return true;
        }
    }

    static class BucketSlot extends SlotItemHandler {
        public BucketSlot(IItemHandler itemHandler, int id, int x, int y) {
            super(itemHandler, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return Items.BUCKET == itemStack.getItem();
        }

        public int getMaxStackSize() {
            return 1;
        }
    }

    static class WaterBucketSlot extends SlotItemHandler {
        public WaterBucketSlot(IItemHandler itemHandler, int id, int x, int y) {
            super(itemHandler, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return Items.WATER_BUCKET == itemStack.getItem();
        }

        public int getMaxStackSize() {
            return 1;
        }
    }
}
