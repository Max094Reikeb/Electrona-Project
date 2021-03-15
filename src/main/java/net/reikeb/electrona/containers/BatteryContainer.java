package net.reikeb.electrona.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import net.reikeb.electrona.setup.RegistryHandler;

import javax.annotation.Nonnull;

public class BatteryContainer extends Container {

    private TileEntity tileEntity;
    private PlayerEntity playerEntity;
    private IItemHandler playerInventory;
    private PlayerInventory playerInv;

    public BatteryContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(RegistryHandler.BATTERY_CONTAINER.get(), windowId);
        tileEntity = world.getBlockEntity(pos);
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
        this.playerInv = playerInventory;


        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 45, 33));
                addSlot(new SlotItemHandler(h, 1, 117, 33));
            });
        }
        layoutPlayerInventorySlots();
    }

    public TileEntity getTileEntity() {
        return this.tileEntity;
    }

    public int getInventorySize() {
        if (playerInv == null) {
            return 0;
        }
        return playerInv.getContainerSize();
    }

    private void layoutPlayerInventorySlots() {
        int si;
        int sj;
        for (si = 0; si < 3; ++si)
            for (sj = 0; sj < 9; ++sj)
                addSlot(new SlotItemHandler(playerInventory, sj + (si + 1) * 9, 8 + sj * 18, 84 + si * 18));
        for (si = 0; si < 9; ++si)
            addSlot(new SlotItemHandler(playerInventory, si, 8 + si * 18, 142));
    }

    @Override
    public boolean stillValid(PlayerEntity playerEntity) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull PlayerEntity player, int index) {
        Slot slot = slots.get(index);
        if (slot == null || !slot.hasItem()) return ItemStack.EMPTY;

        ItemStack existing = slot.getItem().copy();
        ItemStack result = existing.copy();

        if (index == 0) {
            // Insert into player inventory
            if (!moveItemStackTo(existing, 1, 37, true)) return ItemStack.EMPTY;
        } else {
            // Insert into battery inventory
            if (!moveItemStackTo(existing, 0, 1, false)) return ItemStack.EMPTY;
        }

        if (existing.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (existing.getCount() == result.getCount()) return ItemStack.EMPTY;

        slot.onTake(player, existing);
        return result;
    }
}
