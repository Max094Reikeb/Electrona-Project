package net.reikeb.electrona.containers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.CompressionPacket;
import net.reikeb.electrona.network.packets.PurificationPacket;

import org.jetbrains.annotations.NotNull;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

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

    public void addSyncedInt(IntConsumer intConsumer, IntSupplier intSupplier) {
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return intSupplier.getAsInt() & 0xffff;
            }

            @Override
            public void set(int value) {
                int stored = intSupplier.getAsInt() & 0xffff0000;
                intConsumer.accept(stored + (value & 0xffff));
            }
        });
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return (intSupplier.getAsInt() >> 16) & 0xffff;
            }

            @Override
            public void set(int value) {
                int stored = intSupplier.getAsInt() & 0x0000ffff;
                intConsumer.accept(stored | (value << 16));
            }
        });
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

    static class PurificatorOutputSlot extends SlotItemHandler {

        public PurificatorOutputSlot(IItemHandler itemHandler, int id, int x, int y) {
            super(itemHandler, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return false;
        }

        public void onTake(Player playerEntity, ItemStack stack) {
            if (stack.getItem() == ItemInit.PURIFIED_URANIUM.get()) {
                // Trigger Advancement
                NetworkManager.INSTANCE.sendToServer(new PurificationPacket());
            }
        }
    }

    static class WirelessSlot extends SlotItemHandler {

        public WirelessSlot(IItemHandler itemHandler, int id, int x, int y) {
            super(itemHandler, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return itemStack.getItem() == ItemInit.WIRELESS_BOOSTER.get();
        }

        public int getMaxStackSize() {
            return 1;
        }
    }

    static class TeleportSlot extends SlotItemHandler {

        public TeleportSlot(IItemHandler itemHandler, int id, int x, int y) {
            super(itemHandler, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return ((itemStack.getItem() == ItemInit.TELEPORT_SAVER.get())
                    || (itemStack.getItem() == ItemInit.PORTABLE_TELEPORTER.get()));
        }

        public int getMaxStackSize() {
            return 1;
        }
    }

    static class EmeraldSlot extends SlotItemHandler {

        public EmeraldSlot(IItemHandler itemHandler, int id, int x, int y) {
            super(itemHandler, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return (itemStack.getItem() == Items.EMERALD);
        }
    }
}
