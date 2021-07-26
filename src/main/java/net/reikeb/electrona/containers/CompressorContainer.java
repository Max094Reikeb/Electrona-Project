package net.reikeb.electrona.containers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.CompressionPacket;
import net.reikeb.electrona.tileentities.TileCompressor;

import static net.reikeb.electrona.init.ContainerInit.COMPRESSOR_CONTAINER;

public class CompressorContainer extends AbstractContainerMenu {

    public TileCompressor tileEntity;

    public CompressorContainer(MenuType<?> type, int id) {
        super(type, id);
    }

    // Client
    public CompressorContainer(int id, Inventory inv, FriendlyByteBuf buf) {
        super(COMPRESSOR_CONTAINER.get(), id);
        this.init(inv, this.tileEntity = (TileCompressor) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    // Server
    public CompressorContainer(int id, Inventory inv, TileCompressor tile) {
        super(COMPRESSOR_CONTAINER.get(), id);
        this.init(inv, this.tileEntity = tile);
    }

    public void init(Inventory playerInv, TileCompressor tile) {

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 27, 39) {
                    public boolean mayPlace(ItemStack itemStack) {
                        return true;
                    }
                });
                addSlot(new SlotItemHandler(h, 1, 81, 39) {
                    public boolean mayPlace(ItemStack itemStack) {
                        return true;
                    }
                });
                addSlot(new SlotItemHandler(h, 2, 135, 39) {
                    public boolean mayPlace(ItemStack itemStack) {
                        return false;
                    }

                    public void onTake(Player playerEntity, ItemStack stack) {
                        // Trigger Advancement
                        NetworkManager.INSTANCE.sendToServer(new CompressionPacket());
                    }
                });
            });
        }
        layoutPlayerInventorySlots(playerInv);
    }

    public TileCompressor getTileEntity() {
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
