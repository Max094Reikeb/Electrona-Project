package net.reikeb.electrona.containers;

import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import net.reikeb.electrona.tileentities.TileDimensionLinker;

import static net.reikeb.electrona.init.ContainerInit.*;

import java.util.HashMap;

public class DimensionLinkerContainer extends Container {

    public TileDimensionLinker tileEntity;
    public static HashMap textFieldWidget = new HashMap();

    public DimensionLinkerContainer(ContainerType<?> type, int id) {
        super(type, id);
    }

    // Client
    public DimensionLinkerContainer(int id, PlayerInventory inv, PacketBuffer buf) {
        super(DIMENSION_LINKER_CONTAINER.get(), id);
        this.init(inv, this.tileEntity = (TileDimensionLinker) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    // Server
    public DimensionLinkerContainer(int id, PlayerInventory inv, TileDimensionLinker tile) {
        super(DIMENSION_LINKER_CONTAINER.get(), id);
        this.init(inv, this.tileEntity = tile);
    }

    public void init(PlayerInventory playerInv, TileDimensionLinker tile) {

        layoutPlayerInventorySlots(playerInv);
    }

    public TileDimensionLinker getTileEntity() {
        return this.tileEntity;
    }

    public HashMap getTextFieldWidget() {
        return textFieldWidget;
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
            if (!this.moveItemStackTo(itemstack1, 0, 0, false)) {
                if (index < 27) {
                    if (!this.moveItemStackTo(itemstack1, 27, this.slots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(itemstack1, 0, 27, false)) {
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
