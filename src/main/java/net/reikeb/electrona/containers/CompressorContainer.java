package net.reikeb.electrona.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;

import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import net.reikeb.electrona.containers.sync.CompressorSyncData;
import net.reikeb.electrona.tileentities.TileCompressor;

import static net.reikeb.electrona.init.ContainerInit.*;

public class CompressorContainer extends Container {

    public TileCompressor tileEntity;
    private final IIntArray compressorData;

    /**
     * Container factory for opening the container clientside
     **/
    public static CompressorContainer getClientContainer(int id, PlayerInventory playerInventory) {
        // Init client inventory with dummy slots
        return new CompressorContainer(id, playerInventory, BlockPos.ZERO, new IntArray(3));
    }

    /**
     * Get the server container provider for NetworkHooks.openGui
     */
    public static IContainerProvider getServerContainerProvider(TileCompressor te, BlockPos activationPos) {
        return (id, playerInventory, serverPlayer) -> new CompressorContainer(id, playerInventory, activationPos, new CompressorSyncData(te));
    }

    public CompressorContainer(int windowID, PlayerInventory playerInv, BlockPos pos, IIntArray compressorData) {
        super(COMPRESSOR_CONTAINER.get(), windowID);
        this.tileEntity = (TileCompressor) playerInv.player.level.getBlockEntity(pos);
        this.compressorData = compressorData;

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
                });
            });
        }
        layoutPlayerInventorySlots(playerInv);
        this.addDataSlots(compressorData);
    }

    public TileCompressor getTileEntity() {
        return this.tileEntity;
    }

    public int getElectronicPower() {
        return this.compressorData.get(0);
    }

    public int getCompressingTime() {
        return this.compressorData.get(1);
    }

    public int getCurrentCompressingTime() {
        return this.compressorData.get(2);
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
