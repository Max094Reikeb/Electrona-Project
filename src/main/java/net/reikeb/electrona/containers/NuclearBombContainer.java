package net.reikeb.electrona.containers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import net.reikeb.electrona.tileentities.TileNuclearBomb;

import static net.reikeb.electrona.init.ContainerInit.NUCLEAR_BOMB_CONTAINER;

public class NuclearBombContainer extends AbstractContainer {

    public TileNuclearBomb tileEntity;

    public NuclearBombContainer(MenuType<?> type, int id) {
        super(type, id, 2);
    }

    // Client
    public NuclearBombContainer(int id, Inventory inv, FriendlyByteBuf buf) {
        super(NUCLEAR_BOMB_CONTAINER.get(), id, 2);
        this.init(inv, this.tileEntity = (TileNuclearBomb) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    // Server
    public NuclearBombContainer(int id, Inventory inv, TileNuclearBomb tile) {
        super(NUCLEAR_BOMB_CONTAINER.get(), id, 2);
        this.init(inv, this.tileEntity = tile);
    }

    public void init(Inventory playerInv, TileNuclearBomb tile) {

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 53, 36));
                addSlot(new SlotItemHandler(h, 1, 96, 36));
            });
        }
        this.layoutPlayerInventorySlots(playerInv);
    }

    public TileNuclearBomb getTileEntity() {
        return this.tileEntity;
    }
}
