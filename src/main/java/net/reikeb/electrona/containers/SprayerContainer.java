package net.reikeb.electrona.containers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.tileentities.TileSprayer;

import static net.reikeb.electrona.init.ContainerInit.SPRAYER_CONTAINER;

public class SprayerContainer extends AbstractContainer {

    public TileSprayer tileEntity;

    public SprayerContainer(MenuType<?> type, int id) {
        super(type, id, 4);
    }

    // Client
    public SprayerContainer(int id, Inventory inv, FriendlyByteBuf buf) {
        super(SPRAYER_CONTAINER.get(), id, 4);
        this.init(inv, this.tileEntity = (TileSprayer) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    // Server
    public SprayerContainer(int id, Inventory inv, TileSprayer tile) {
        super(SPRAYER_CONTAINER.get(), id, 4);
        this.init(inv, this.tileEntity = tile);
    }

    public void init(Inventory playerInv, TileSprayer tile) {

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 80, 14));
                addSlot(new SlotItemHandler(h, 1, 20, 44) {
                    public boolean mayPlace(ItemStack itemStack) {
                        return itemStack.getItem() == ItemInit.WIRELESS_BOOSTER.get();
                    }
                });
                addSlot(new SlotItemHandler(h, 2, 50, 44) {
                    public boolean mayPlace(ItemStack itemStack) {
                        return itemStack.getItem() == ItemInit.WIRELESS_BOOSTER.get();
                    }
                });
                addSlot(new SlotItemHandler(h, 3, 80, 44) {
                    public boolean mayPlace(ItemStack itemStack) {
                        return itemStack.getItem() == ItemInit.WIRELESS_BOOSTER.get();
                    }
                });
            });
        }
        this.layoutPlayerInventorySlots(playerInv);
    }

    public TileSprayer getTileEntity() {
        return this.tileEntity;
    }
}
