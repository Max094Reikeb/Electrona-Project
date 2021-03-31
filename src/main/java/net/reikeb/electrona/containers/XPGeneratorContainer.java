package net.reikeb.electrona.containers;

import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.*;
import net.minecraft.item.*;
import net.minecraft.network.PacketBuffer;

import net.minecraftforge.items.*;

import net.reikeb.electrona.tileentities.TileXPGenerator;

import static net.reikeb.electrona.init.ContainerInit.*;

public class XPGeneratorContainer extends Container {

    public TileXPGenerator tileEntity;

    public XPGeneratorContainer(ContainerType<?> type, int id) {
        super(type, id);
    }

    // Client
    public XPGeneratorContainer(int id, PlayerInventory inv, PacketBuffer buf) {
        super(XP_GENERATOR_CONTAINER.get(), id);
        this.init(inv, this.tileEntity = (TileXPGenerator) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    // Server
    public XPGeneratorContainer(int id, PlayerInventory inv, TileXPGenerator tile) {
        super(XP_GENERATOR_CONTAINER.get(), id);
        this.init(inv, this.tileEntity = tile);
    }

    public void init(PlayerInventory playerInv, TileXPGenerator tile) {

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 81, 19) {
                    @Override
                    public boolean mayPlace(ItemStack itemStack) {
                        return (itemStack.getItem() == Items.EMERALD);
                    }
                });
            });
        }
        layoutPlayerInventorySlots(playerInv);
    }

    public TileXPGenerator getTileEntity() {
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
        Slot slot = (Slot) this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 1) {
                if (!this.moveItemStackTo(itemstack1, 1, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                if (index < 1 + 27) {
                    if (!this.moveItemStackTo(itemstack1, 1 + 27, this.slots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(itemstack1, 1, 1 + 27, false)) {
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
