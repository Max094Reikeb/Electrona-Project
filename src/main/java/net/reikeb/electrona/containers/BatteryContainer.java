package net.reikeb.electrona.containers;

import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import net.minecraftforge.items.*;

import net.reikeb.electrona.tileentities.TileBattery;

import static net.reikeb.electrona.init.ContainerInit.*;

public class BatteryContainer extends Container {

    public TileBattery tileEntity;

    public BatteryContainer(ContainerType<?> type, int id) {
        super(type, id);
    }

    // Client
    public BatteryContainer(int id, PlayerInventory inv, PacketBuffer buf) {
        super(BATTERY_CONTAINER.get(), id);
        this.init(inv, this.tileEntity = (TileBattery) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    // Server
    public BatteryContainer(int id, PlayerInventory inv, TileBattery tile) {
        super(BATTERY_CONTAINER.get(), id);
        this.init(inv, this.tileEntity = tile);
    }

    public void init(PlayerInventory playerInv, TileBattery tile) {

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 45, 33) {
                    public boolean mayPlace(ItemStack itemStack) {
                        return true;
                    }

                    public int getMaxStackSize() {
                        return 1;
                    }
                });
                addSlot(new SlotItemHandler(h, 1, 117, 33) {
                    public boolean mayPlace(ItemStack itemStack) {
                        return true;
                    }

                    public int getMaxStackSize() {
                        return 1;
                    }
                });
            });
        }
        layoutPlayerInventorySlots(playerInv);
    }

    public TileBattery getTileEntity() {
        return this.tileEntity;
    }

    private void layoutPlayerInventorySlots(PlayerInventory playerInv) {
        int si;
        int sj;
        for (si = 0; si < 3; ++si)
            for (sj = 0; sj < 9; ++sj)
                addSlot(new Slot(playerInv, sj + (si + 1) * 9, 8 + sj * 18, 84 + si * 18));
        for (si = 0; si < 9; ++si)
            addSlot(new Slot(playerInv, si, 8 + si * 18, 142));
    }

    @Override
    public boolean stillValid(PlayerEntity playerEntity) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 2) {
                if (!this.moveItemStackTo(itemstack1, 2, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (!this.moveItemStackTo(itemstack1, 0, 2, false)) {
                if (index < 2 + 27) {
                    if (!this.moveItemStackTo(itemstack1, 2 + 27, this.slots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(itemstack1, 2, 2 + 27, false)) {
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
}
