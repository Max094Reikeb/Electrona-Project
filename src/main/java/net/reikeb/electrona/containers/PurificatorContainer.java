package net.reikeb.electrona.containers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.PurificationPacket;
import net.reikeb.electrona.tileentities.TilePurificator;

import javax.annotation.Nonnull;

import static net.reikeb.electrona.init.ContainerInit.PURIFICATOR_CONTAINER;

public class PurificatorContainer extends AbstractContainer {

    public TilePurificator tileEntity;

    public PurificatorContainer(MenuType<?> type, int id) {
        super(type, id, 3);
    }

    // Client
    public PurificatorContainer(int id, Inventory inv, FriendlyByteBuf buf) {
        super(PURIFICATOR_CONTAINER.get(), id, 3);
        this.init(inv, this.tileEntity = (TilePurificator) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    // Server
    public PurificatorContainer(int id, Inventory inv, TilePurificator tile) {
        super(PURIFICATOR_CONTAINER.get(), id, 3);
        this.init(inv, this.tileEntity = tile);
    }

    public void init(Inventory playerInv, TilePurificator tile) {

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 27, 27) {
                    public boolean mayPlace(ItemStack itemStack) {
                        return Items.WATER_BUCKET == itemStack.getItem();
                    }

                    public int getMaxStackSize() {
                        return 1;
                    }
                });
                addSlot(new SlotItemHandler(h, 1, 56, 40));
                addSlot(new SlotItemHandler(h, 2, 136, 40) {
                    @Override
                    public boolean mayPlace(@Nonnull ItemStack stack) {
                        return false;
                    }

                    public void onTake(Player playerEntity, ItemStack stack) {
                        if (stack.getItem() == ItemInit.PURIFIED_URANIUM.get()) {
                            // Trigger Advancement
                            NetworkManager.INSTANCE.sendToServer(new PurificationPacket());
                        }
                    }
                });
            });
        }
        this.layoutPlayerInventorySlots(playerInv);
    }

    public TilePurificator getTileEntity() {
        return this.tileEntity;
    }
}
