package net.reikeb.electrona.containers;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.PurificationPacket;

import static net.reikeb.electrona.init.ContainerInit.PURIFICATOR_CONTAINER;

public class PurificatorContainer extends AbstractContainer {

    private final ContainerData purificatorData;

    public PurificatorContainer(int id, Inventory inv) {
        this(id, inv, new SimpleContainer(3), new SimpleContainerData(3));
    }

    public PurificatorContainer(int id, Inventory inv, Container container, ContainerData containerData) {
        super(PURIFICATOR_CONTAINER.get(), id, 3);

        this.purificatorData = containerData;

        // this.addSlot(new Slots.WaterBucketSlot(container, 0, 27, 27));
        // this.addSlot(new Slots.BasicInputSlot(container, 1, 56, 40));
        // this.addSlot(new InputSlot(container, 2, 136, 40));

        this.layoutPlayerInventorySlots(inv);
    }

    public int getCurrentWater() {
        return this.purificatorData.get(0);
    }

    public int getPurifyingTime() {
        return this.purificatorData.get(1);
    }

    public int getCurrentPurifyingTime() {
        return this.purificatorData.get(2);
    }

    static class InputSlot extends Slot {
        public InputSlot(Container container, int id, int x, int y) {
            super(container, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return false;
        }

        public void onTake(Player playerEntity, ItemStack stack) {
            if (stack.getItem() == ItemInit.PURIFIED_URANIUM.get()) {
                // Trigger Advancement
                NetworkManager.INSTANCE.sendToServer(new PurificationPacket());
            }
        }
    }
}
