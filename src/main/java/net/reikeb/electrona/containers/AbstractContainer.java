package net.reikeb.electrona.containers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.CompressionPacket;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractContainer extends AbstractContainerMenu {

    private final int slotCount;

    public AbstractContainer(MenuType<?> type, int id, int slotCount) {
        super(type, id);
        this.slotCount = slotCount;
    }

    @Override
    public boolean stillValid(@NotNull Player playerEntity) {
        return true;
    }

    public void layoutPlayerInventorySlots(Inventory playerInv) {
        int si;
        int sj;
        for (si = 0; si < 3; ++si)
            for (sj = 0; sj < 9; ++sj)
                this.addSlot(new Slot(playerInv, sj + (si + 1) * 9, 8 + sj * 18, 84 + si * 18));
        for (si = 0; si < 9; ++si)
            this.addSlot(new Slot(playerInv, si, 8 + si * 18, 142));
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.slotCount) {
                if (!this.moveItemStackTo(itemstack1, this.slotCount, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (!this.moveItemStackTo(itemstack1, 0, this.slotCount, false)) {
                if (index < this.slotCount + 27) {
                    if (!this.moveItemStackTo(itemstack1, this.slotCount + 27, this.slots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(itemstack1, this.slotCount, this.slotCount + 27, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                return ItemStack.EMPTY;
            }
            if (itemstack1.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, itemstack1);
        }
        return itemstack;
    }

    public static class BatterySlot extends SlotItemHandler {
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

    public static class BasicInputSlot extends SlotItemHandler {
        public BasicInputSlot(IItemHandler itemHandler, int id, int x, int y) {
            super(itemHandler, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return true;
        }
    }

    public static class BucketSlot extends SlotItemHandler {
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

    public static class WaterBucketSlot extends SlotItemHandler {
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

    static class CompressorOutputSlot extends SlotItemHandler {
        public CompressorOutputSlot(IItemHandler itemHandler, int id, int x, int y) {
            super(itemHandler, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return false;
        }

        public void onTake(Player playerEntity, ItemStack stack) {
            // Trigger Advancement
            NetworkManager.INSTANCE.sendToServer(new CompressionPacket());
        }
    }
}
