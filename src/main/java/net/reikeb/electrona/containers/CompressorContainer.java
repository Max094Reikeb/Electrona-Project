package net.reikeb.electrona.containers;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.CompressionPacket;

import static net.reikeb.electrona.init.ContainerInit.COMPRESSOR_CONTAINER;

public class CompressorContainer extends AbstractContainer {

    private final ContainerData compressorData;

    public CompressorContainer(int id, Inventory inv) {
        this(id, inv, new SimpleContainer(3), new SimpleContainerData(3));
    }

    public CompressorContainer(int id, Inventory inv, Container container, ContainerData containerData) {
        super(COMPRESSOR_CONTAINER.get(), id, 3);

        this.compressorData = containerData;

        this.addSlot(new Slots.BasicInputSlot(container, 0, 27, 39));
        this.addSlot(new Slots.BasicInputSlot(container, 1, 81, 39));
        this.addSlot(new OutputSlot(container, 2, 135, 39));

        this.layoutPlayerInventorySlots(inv);
    }

    public int getElectronicPower() {
        return this.compressorData.get(0);
    }

    public int getCompressingTime() {
        return this.compressorData.get(1);
    }

    public int getCurrentCompressingTime() {
        return this.compressorData.get(2);
    }

    static class OutputSlot extends Slot {
        public OutputSlot(Container container, int id, int x, int y) {
            super(container, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return false;
        }

        public void onTake(Player playerEntity, ItemStack stack) {
            // Trigger Advancement
            NetworkManager.INSTANCE.sendToServer(new CompressionPacket());
        }
    }
}
