package net.reikeb.electrona.containers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import net.reikeb.electrona.tileentities.TileSteelCrate;

import static net.reikeb.electrona.init.ContainerInit.STEEL_CRATE_CONTAINER;

public class SteelCrateContainer extends AbstractContainer {

    public TileSteelCrate tileEntity;

    public SteelCrateContainer(MenuType<?> type, int id) {
        super(type, id, 27);
    }

    // Client
    public SteelCrateContainer(int id, Inventory inv, FriendlyByteBuf buf) {
        super(STEEL_CRATE_CONTAINER.get(), id, 27);
        this.init(inv, this.tileEntity = (TileSteelCrate) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    // Server
    public SteelCrateContainer(int id, Inventory inv, TileSteelCrate tile) {
        super(STEEL_CRATE_CONTAINER.get(), id, 27);
        this.init(inv, this.tileEntity = tile);
    }

    public void init(Inventory playerInv, TileSteelCrate tile) {

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 8, 18));
                addSlot(new SlotItemHandler(h, 1, 26, 18));
                addSlot(new SlotItemHandler(h, 2, 44, 18));
                addSlot(new SlotItemHandler(h, 3, 62, 18));
                addSlot(new SlotItemHandler(h, 4, 80, 18));
                addSlot(new SlotItemHandler(h, 5, 98, 18));
                addSlot(new SlotItemHandler(h, 6, 116, 18));
                addSlot(new SlotItemHandler(h, 7, 134, 18));
                addSlot(new SlotItemHandler(h, 8, 152, 18));
                addSlot(new SlotItemHandler(h, 9, 8, 36));
                addSlot(new SlotItemHandler(h, 10, 26, 36));
                addSlot(new SlotItemHandler(h, 11, 44, 36));
                addSlot(new SlotItemHandler(h, 12, 62, 36));
                addSlot(new SlotItemHandler(h, 13, 80, 36));
                addSlot(new SlotItemHandler(h, 14, 98, 36));
                addSlot(new SlotItemHandler(h, 15, 116, 36));
                addSlot(new SlotItemHandler(h, 16, 134, 36));
                addSlot(new SlotItemHandler(h, 17, 152, 36));
                addSlot(new SlotItemHandler(h, 18, 8, 54));
                addSlot(new SlotItemHandler(h, 19, 26, 54));
                addSlot(new SlotItemHandler(h, 20, 44, 54));
                addSlot(new SlotItemHandler(h, 21, 62, 54));
                addSlot(new SlotItemHandler(h, 22, 80, 54));
                addSlot(new SlotItemHandler(h, 23, 98, 54));
                addSlot(new SlotItemHandler(h, 24, 116, 54));
                addSlot(new SlotItemHandler(h, 25, 134, 54));
                addSlot(new SlotItemHandler(h, 26, 152, 54));
            });
        }
        this.layoutPlayerInventorySlots(playerInv);
    }

    public TileSteelCrate getTileEntity() {
        return this.tileEntity;
    }
}
