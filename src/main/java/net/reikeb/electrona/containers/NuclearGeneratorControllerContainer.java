package net.reikeb.electrona.containers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import net.reikeb.electrona.tileentities.TileNuclearGeneratorController;

import static net.reikeb.electrona.init.ContainerInit.NUCLEAR_GENERATOR_CONTAINER;

public class NuclearGeneratorControllerContainer extends AbstractContainer {

    public TileNuclearGeneratorController tileEntity;

    public NuclearGeneratorControllerContainer(MenuType<?> type, int id) {
        super(type, id, 2);
    }

    // Client
    public NuclearGeneratorControllerContainer(int id, Inventory inv, FriendlyByteBuf buf) {
        super(NUCLEAR_GENERATOR_CONTAINER.get(), id, 2);
        this.init(inv, this.tileEntity = (TileNuclearGeneratorController) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    // Server
    public NuclearGeneratorControllerContainer(int id, Inventory inv, TileNuclearGeneratorController tile) {
        super(NUCLEAR_GENERATOR_CONTAINER.get(), id, 2);
        this.init(inv, this.tileEntity = tile);
    }

    public void init(Inventory playerInv, TileNuclearGeneratorController tile) {

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 27, 32) {
                    public boolean mayPlace(ItemStack itemStack) {
                        return itemStack.getItem() == Items.WATER_BUCKET;
                    }

                    public int getMaxStackSize() {
                        return 1;
                    }
                });
                addSlot(new SlotItemHandler(h, 1, 55, 32) {
                    public boolean mayPlace(ItemStack itemStack) {
                        return !tileEntity.getTileData().getBoolean("UBIn");
                    }

                    public boolean mayPickup(Player playerEntity) {
                        return !tileEntity.getTileData().getBoolean("UBIn");
                    }

                    public int getMaxStackSize() {
                        return 1;
                    }
                });
            });
        }
    }

    public TileNuclearGeneratorController getTileEntity() {
        return this.tileEntity;
    }
}
