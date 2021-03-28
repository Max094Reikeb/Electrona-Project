package net.reikeb.electrona.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;

import net.minecraftforge.items.*;

import net.reikeb.electrona.tileentities.TileNuclearGeneratorController;

import static net.reikeb.electrona.init.ContainerInit.*;

public class NuclearGeneratorControllerContainer extends Container {

    public TileNuclearGeneratorController tileEntity;

    public NuclearGeneratorControllerContainer(ContainerType<?> type, int id) {
        super(type, id);
    }

    // Client
    public NuclearGeneratorControllerContainer(int id, PlayerInventory inv, PacketBuffer buf) {
        super(NUCLEAR_GENERATOR_CONTAINER.get(), id);
        this.init(inv, this.tileEntity = (TileNuclearGeneratorController) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    // Server
    public NuclearGeneratorControllerContainer(int id, PlayerInventory inv, TileNuclearGeneratorController tile) {
        super(NUCLEAR_GENERATOR_CONTAINER.get(), id);
        this.init(inv, this.tileEntity = tile);
    }

    public void init(PlayerInventory playerInv, TileNuclearGeneratorController tile) {

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 27, 32) {
                    public boolean mayPlace(ItemStack itemStack) {
                        return itemStack.getItem() == Items.WATER_BUCKET;
                    }

                    public int getMaxStackSize() {
                        return 1;
                    }
                });
                addSlot(new SlotItemHandler(h, 1, 55, 32) {
                    public boolean mayPlace(ItemStack itemStack) {
                        return !tileEntity.getTileData().getBoolean("UBIn");
                    }

                    public boolean mayPickup(PlayerEntity playerEntity) {
                        return !tileEntity.getTileData().getBoolean("UBIn");
                    }

                    public int getMaxStackSize() {
                        return 1;
                    }
                });
            });
        }
        layoutPlayerInventorySlots(playerInv);
    }

    public TileNuclearGeneratorController getTileEntity() {
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
