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

import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.PurificationPacket;
import net.reikeb.electrona.tileentities.TilePurificator;

import javax.annotation.Nonnull;

import static net.reikeb.electrona.init.ContainerInit.PURIFICATOR_CONTAINER;

public class PurificatorContainer extends AbstractContainerMenu {

    public TilePurificator tileEntity;

    public PurificatorContainer(MenuType<?> type, int id) {
        super(type, id);
    }

    // Client
    public PurificatorContainer(int id, Inventory inv, FriendlyByteBuf buf) {
        super(PURIFICATOR_CONTAINER.get(), id);
        this.init(inv, this.tileEntity = (TilePurificator) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    // Server
    public PurificatorContainer(int id, Inventory inv, TilePurificator tile) {
        super(PURIFICATOR_CONTAINER.get(), id);
        this.init(inv, this.tileEntity = tile);
    }

    public void init(Inventory playerInv, TilePurificator tile) {

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 27, 43) {
                    public boolean mayPlace(ItemStack itemStack) {
                        return Items.WATER_BUCKET == itemStack.getItem();
                    }

                    public int getMaxStackSize() {
                        return 1;
                    }
                });
                addSlot(new SlotItemHandler(h, 1, 56, 28));
                addSlot(new SlotItemHandler(h, 2, 136, 28) {
                    @Override
                    public boolean mayPlace(@Nonnull ItemStack stack) {
                        return false;
                    }

                    public void onTake(Player playerEntity, ItemStack stack) {
                        if (stack.getItem() == ItemInit.GOLD_POWDER.get()) {
                            // Trigger Advancement
                            NetworkManager.INSTANCE.sendToServer(new PurificationPacket());
                        }
                    }
                });
            });
        }
        layoutPlayerInventorySlots(playerInv);
    }

    public TilePurificator getTileEntity() {
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
        Slot slot = (Slot) this.slots.get(index);
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
