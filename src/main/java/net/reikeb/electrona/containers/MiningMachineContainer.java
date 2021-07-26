package net.reikeb.electrona.containers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import net.reikeb.electrona.tileentities.TileMiningMachine;

import static net.reikeb.electrona.init.ContainerInit.MINING_MACHINE_CONTAINER;

public class MiningMachineContainer extends AbstractContainerMenu {

    public TileMiningMachine tileEntity;

    public MiningMachineContainer(MenuType<?> type, int id) {
        super(type, id);
    }

    // Client
    public MiningMachineContainer(int id, Inventory inv, FriendlyByteBuf buf) {
        super(MINING_MACHINE_CONTAINER.get(), id);
        this.init(inv, this.tileEntity = (TileMiningMachine) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    // Server
    public MiningMachineContainer(int id, Inventory inv, TileMiningMachine tile) {
        super(MINING_MACHINE_CONTAINER.get(), id);
        this.init(inv, this.tileEntity = tile);
    }

    public void init(Inventory playerInv, TileMiningMachine tile) {

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 79, 35));
                addSlot(new SlotItemHandler(h, 1, 34, 26) {
                    public boolean mayPlace(ItemStack itemStack) {
                        return itemStack.getItem() == Items.BUCKET;
                    }
                });
                addSlot(new SlotItemHandler(h, 2, 34, 44) {
                    public boolean mayPlace(ItemStack itemStack) {
                        return itemStack.getItem() == Items.BUCKET;
                    }
                });
            });
        }
        layoutPlayerInventorySlots(playerInv);
    }

    public TileMiningMachine getTileEntity() {
        return this.tileEntity;
    }

    private void layoutPlayerInventorySlots(Inventory playerInv) {
        int si;
        int sj;
        for (si = 0; si < 3; ++si)
            for (sj = 0; sj < 9; ++sj)
                addSlot(new Slot(playerInv, sj + (si + 1) * 9, 8 + sj * 18, 84 + si * 18));
        for (si = 0; si < 9; ++si)
            addSlot(new Slot(playerInv, si, 8 + si * 18, 142));
    }

    @Override
    public boolean stillValid(Player playerEntity) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 3) {
                if (!this.moveItemStackTo(itemstack1, 3, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (!this.moveItemStackTo(itemstack1, 0, 3, false)) {
                if (index < 3 + 27) {
                    if (!this.moveItemStackTo(itemstack1, 3 + 27, this.slots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(itemstack1, 3, 3 + 27, false)) {
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
