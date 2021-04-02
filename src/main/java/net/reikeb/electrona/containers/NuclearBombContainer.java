package net.reikeb.electrona.containers;

import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import net.minecraftforge.items.*;

import net.reikeb.electrona.tileentities.TileNuclearBomb;

import static net.reikeb.electrona.init.ContainerInit.*;

public class NuclearBombContainer extends Container {

    public TileNuclearBomb tileEntity;

    public NuclearBombContainer(ContainerType<?> type, int id) {
        super(type, id);
    }

    // Client
    public NuclearBombContainer(int id, PlayerInventory inv, PacketBuffer buf) {
        super(NUCLEAR_BOMB_CONTAINER.get(), id);
        this.init(inv, this.tileEntity = (TileNuclearBomb) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    // Server
    public NuclearBombContainer(int id, PlayerInventory inv, TileNuclearBomb tile) {
        super(NUCLEAR_BOMB_CONTAINER.get(), id);
        this.init(inv, this.tileEntity = tile);
    }

    public void init(PlayerInventory playerInv, TileNuclearBomb tile) {

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 53, 31));
                addSlot(new SlotItemHandler(h, 1, 107, 31));
            });
        }
        layoutPlayerInventorySlots(playerInv);
    }

    public TileNuclearBomb getTileEntity() {
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
