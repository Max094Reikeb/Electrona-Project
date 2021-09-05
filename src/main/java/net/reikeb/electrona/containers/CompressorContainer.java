package net.reikeb.electrona.containers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.CompressionPacket;
import net.reikeb.electrona.tileentities.TileCompressor;

import static net.reikeb.electrona.init.ContainerInit.COMPRESSOR_CONTAINER;

public class CompressorContainer extends AbstractContainer {

    public TileCompressor tileEntity;

    public CompressorContainer(MenuType<?> type, int id) {
        super(type, id, 3);
    }

    // Client
    public CompressorContainer(int id, Inventory inv, FriendlyByteBuf buf) {
        super(COMPRESSOR_CONTAINER.get(), id, 3);
        this.init(inv, this.tileEntity = (TileCompressor) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    // Server
    public CompressorContainer(int id, Inventory inv, TileCompressor tile) {
        super(COMPRESSOR_CONTAINER.get(), id, 3);
        this.init(inv, this.tileEntity = tile);
    }

    public void init(Inventory playerInv, TileCompressor tile) {

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

                    public void onTake(Player playerEntity, ItemStack stack) {
                        // Trigger Advancement
                        NetworkManager.INSTANCE.sendToServer(new CompressionPacket());
                    }
                });
            });
        }
        this.layoutPlayerInventorySlots(playerInv);
    }

    public TileCompressor getTileEntity() {
        return this.tileEntity;
    }
}
