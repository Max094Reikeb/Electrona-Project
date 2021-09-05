package net.reikeb.electrona.containers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import net.reikeb.electrona.tileentities.TileXPGenerator;

import static net.reikeb.electrona.init.ContainerInit.XP_GENERATOR_CONTAINER;

public class XPGeneratorContainer extends AbstractContainer {

    public TileXPGenerator tileEntity;

    public XPGeneratorContainer(MenuType<?> type, int id) {
        super(type, id, 1);
    }

    // Client
    public XPGeneratorContainer(int id, Inventory inv, FriendlyByteBuf buf) {
        super(XP_GENERATOR_CONTAINER.get(), id, 1);
        this.init(inv, this.tileEntity = (TileXPGenerator) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    // Server
    public XPGeneratorContainer(int id, Inventory inv, TileXPGenerator tile) {
        super(XP_GENERATOR_CONTAINER.get(), id, 1);
        this.init(inv, this.tileEntity = tile);
    }

    public void init(Inventory playerInv, TileXPGenerator tile) {

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 81, 19) {
                    @Override
                    public boolean mayPlace(ItemStack itemStack) {
                        return (itemStack.getItem() == Items.EMERALD);
                    }
                });
            });
        }
        this.layoutPlayerInventorySlots(playerInv);
    }

    public TileXPGenerator getTileEntity() {
        return this.tileEntity;
    }
}
