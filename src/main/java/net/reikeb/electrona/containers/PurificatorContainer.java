package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.reikeb.electrona.blockentities.PurificatorBlockEntity;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.PurificationPacket;
import net.reikeb.maxilib.abs.AbstractContainer;
import net.reikeb.maxilib.inventory.Slots;
import org.jetbrains.annotations.NotNull;

import static net.reikeb.electrona.init.ContainerInit.PURIFICATOR_CONTAINER;

public class PurificatorContainer extends AbstractContainer {

    public PurificatorBlockEntity purificatorBlockEntity;

    public PurificatorContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(PURIFICATOR_CONTAINER.get(), id, 3);

        this.purificatorBlockEntity = (PurificatorBlockEntity) player.getCommandSenderWorld().getBlockEntity(pos);
        if (purificatorBlockEntity == null) return;

        purificatorBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new Slots(h, 0, 27, 27, c -> c.getItem() == Items.WATER_BUCKET, 1));
            addSlot(new Slots(h, 1, 56, 40));
            addSlot(new PurificatorSlot(h, 2, 136, 40));
        });

        this.layoutPlayerInventorySlots(inv);
        this.addSyncedInt(purificatorBlockEntity::setWaterLevel, purificatorBlockEntity::getWaterLevel);
        this.addSyncedInt(purificatorBlockEntity::setPurifyingTime, purificatorBlockEntity::getPurifyingTime);
        this.addSyncedInt(purificatorBlockEntity::setCurrentPurifyingTime, purificatorBlockEntity::getCurrentPurifyingTime);
    }

    public int getCurrentWater() {
        return purificatorBlockEntity.getWaterLevel();
    }

    public int getPurifyingTime() {
        return purificatorBlockEntity.getPurifyingTime();
    }

    public int getCurrentPurifyingTime() {
        return purificatorBlockEntity.getCurrentPurifyingTime();
    }

    public static class PurificatorSlot extends SlotItemHandler {

        public PurificatorSlot(IItemHandler itemHandler, int id, int x, int y) {
            super(itemHandler, id, x, y);
        }

        public boolean mayPlace(@NotNull ItemStack itemStack) {
            return false;
        }

        public void onTake(@NotNull Player playerEntity, ItemStack stack) {
            if (stack.getItem() == ItemInit.PURIFIED_URANIUM.get()) {
                // Trigger Advancement
                NetworkManager.INSTANCE.sendToServer(new PurificationPacket());
            }
        }
    }
}
