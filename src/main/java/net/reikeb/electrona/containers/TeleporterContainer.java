package net.reikeb.electrona.containers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.tileentities.TileTeleporter;

import static net.reikeb.electrona.init.ContainerInit.TELEPORTER_CONTAINER;

public class TeleporterContainer extends AbstractContainer {

    public TileTeleporter tileEntity;

    public TeleporterContainer(MenuType<?> type, int id) {
        super(type, id, 1);
    }

    // Client
    public TeleporterContainer(int id, Inventory inv, FriendlyByteBuf buf) {
        super(TELEPORTER_CONTAINER.get(), id, 1);
        this.init(inv, this.tileEntity = (TileTeleporter) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    // Server
    public TeleporterContainer(int id, Inventory inv, TileTeleporter tile) {
        super(TELEPORTER_CONTAINER.get(), id, 1);
        this.init(inv, this.tileEntity = tile);
    }

    public void init(Inventory playerInv, TileTeleporter tile) {

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 81, 27) {
                    @Override
                    public boolean mayPlace(ItemStack itemStack) {
                        return ((itemStack.getItem() == ItemInit.TELEPORT_SAVER.get())
                                || (itemStack.getItem() == ItemInit.PORTABLE_TELEPORTER.get()));
                    }
                });
            });
        }
    }

    public TileTeleporter getTileEntity() {
        return this.tileEntity;
    }
}
