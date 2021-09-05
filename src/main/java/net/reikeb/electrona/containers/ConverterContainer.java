package net.reikeb.electrona.containers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import net.reikeb.electrona.tileentities.TileConverter;

import static net.reikeb.electrona.init.ContainerInit.CONVERTER_CONTAINER;

public class ConverterContainer extends AbstractContainer {

    public TileConverter tileEntity;

    public ConverterContainer(MenuType<?> type, int id) {
        super(type, id, 1);
    }

    // Client
    public ConverterContainer(int id, Inventory inv, FriendlyByteBuf buf) {
        super(CONVERTER_CONTAINER.get(), id, 1);
        this.init(inv, this.tileEntity = (TileConverter) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    // Server
    public ConverterContainer(int id, Inventory inv, TileConverter tile) {
        super(CONVERTER_CONTAINER.get(), id, 1);
        this.init(inv, this.tileEntity = tile);
    }

    public void init(Inventory playerInv, TileConverter tile) {

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 81, 31) {
                    public boolean mayPlace(ItemStack itemStack) {
                        return true;
                    }

                    public int getMaxStackSize() {
                        return 1;
                    }
                });
            });
        }
    }

    public TileConverter getTileEntity() {
        return this.tileEntity;
    }
}
