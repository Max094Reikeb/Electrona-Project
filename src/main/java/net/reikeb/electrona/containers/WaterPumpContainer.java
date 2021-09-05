package net.reikeb.electrona.containers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import net.reikeb.electrona.tileentities.TileWaterPump;

import static net.reikeb.electrona.init.ContainerInit.WATER_PUMP_CONTAINER;

public class WaterPumpContainer extends AbstractContainer {

    public TileWaterPump tileEntity;

    public WaterPumpContainer(MenuType<?> type, int id) {
        super(type, id, 2);
    }

    // Client
    public WaterPumpContainer(int id, Inventory inv, FriendlyByteBuf buf) {
        super(WATER_PUMP_CONTAINER.get(), id, 2);
        this.init(inv, this.tileEntity = (TileWaterPump) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    // Server
    public WaterPumpContainer(int id, Inventory inv, TileWaterPump tile) {
        super(WATER_PUMP_CONTAINER.get(), id, 2);
        this.init(inv, this.tileEntity = tile);
    }

    public void init(Inventory playerInv, TileWaterPump tile) {

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 36, 35) {
                    public boolean mayPlace(ItemStack itemStack) {
                        return Items.BUCKET == itemStack.getItem();
                    }

                    public int getMaxStackSize() {
                        return 1;
                    }
                });
                addSlot(new SlotItemHandler(h, 1, 137, 29) {
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

    public TileWaterPump getTileEntity() {
        return this.tileEntity;
    }
}
