package net.reikeb.electrona.containers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import net.reikeb.electrona.tileentities.TileBattery;

import static net.reikeb.electrona.init.ContainerInit.BATTERY_CONTAINER;

public class BatteryContainer extends AbstractContainer {

    public TileBattery tileEntity;

    public BatteryContainer(MenuType<?> type, int id) {
        super(type, id, 2);
    }

    // Client
    public BatteryContainer(int id, Inventory inv, FriendlyByteBuf buf) {
        super(BATTERY_CONTAINER.get(), id, 2);
        this.init(inv, this.tileEntity = (TileBattery) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    // Server
    public BatteryContainer(int id, Inventory inv, TileBattery tile) {
        super(BATTERY_CONTAINER.get(), id, 2);
        this.init(inv, this.tileEntity = tile);
    }

    public void init(Inventory playerInv, TileBattery tile) {

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
        this.layoutPlayerInventorySlots(playerInv);
    }

    public TileBattery getTileEntity() {
        return this.tileEntity;
    }
}
