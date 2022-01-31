package net.reikeb.electrona.containers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import net.reikeb.electrona.tileentities.TileMiningMachine;

import static net.reikeb.electrona.init.ContainerInit.MINING_MACHINE_CONTAINER;

public class MiningMachineContainer extends AbstractContainer {

    public TileMiningMachine tileEntity;

    public MiningMachineContainer(MenuType<?> type, int id) {
        super(type, id, 3);
    }

    // Client
    public MiningMachineContainer(int id, Inventory inv, FriendlyByteBuf buf) {
        super(MINING_MACHINE_CONTAINER.get(), id, 3);
        this.init(inv, this.tileEntity = (TileMiningMachine) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    // Server
    public MiningMachineContainer(int id, Inventory inv, TileMiningMachine tile) {
        super(MINING_MACHINE_CONTAINER.get(), id, 3);
        this.init(inv, this.tileEntity = tile);
    }

    public void init(Inventory playerInv, TileMiningMachine tile) {

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 91, 12));
                addSlot(new SlotItemHandler(h, 1, 74, 51) {
                    public boolean mayPlace(ItemStack itemStack) {
                        return itemStack.getItem() == Items.BUCKET;
                    }
                });
                addSlot(new SlotItemHandler(h, 2, 108, 51) {
                    public boolean mayPlace(ItemStack itemStack) {
                        return itemStack.getItem() == Items.BUCKET;
                    }
                });
            });
        }
        this.layoutPlayerInventorySlots(playerInv);
    }

    public TileMiningMachine getTileEntity() {
        return this.tileEntity;
    }
}
