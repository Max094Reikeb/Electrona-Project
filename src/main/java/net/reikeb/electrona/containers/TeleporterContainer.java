package net.reikeb.electrona.containers;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.tileentities.TileTeleporter;

import static net.reikeb.electrona.init.ContainerInit.TELEPORTER_CONTAINER;

public class TeleporterContainer extends AbstractContainer {

    private final ContainerData teleporterData;
    public TileTeleporter tileEntity;

    public TeleporterContainer(int id, Inventory inv) {
        this(id, inv, new SimpleContainer(1), new SimpleContainerData(10));
    }

    public TeleporterContainer(int id, Inventory inv, Container container, ContainerData containerData) {
        super(TELEPORTER_CONTAINER.get(), id, 1);

        this.teleporterData = containerData;

        this.addSlot(new TeleportSlot(container, 0, 81, 27));

        this.layoutPlayerInventorySlots(inv);
    }

    public int getElectronicPower() {
        return this.teleporterData.get(0);
    }

    public int getTeleportX() {
        return this.teleporterData.get(1);
    }

    public void setTeleportX(int teleportX) {
        this.teleporterData.set(1, teleportX);
    }

    public int getItemTeleportX() {
        return this.teleporterData.get(5);
    }

    public void setItemTeleportX(int itemTeleportX) {
        this.teleporterData.set(5, itemTeleportX);
    }

    public int getTeleportY() {
        return this.teleporterData.get(2);
    }

    public void setTeleportY(int teleportY) {
        this.teleporterData.set(2, teleportY);
    }

    public int getItemTeleportY() {
        return this.teleporterData.get(6);
    }

    public void setItemTeleportY(int itemTeleportY) {
        this.teleporterData.set(6, itemTeleportY);
    }

    public int getTeleportZ() {
        return this.teleporterData.get(3);
    }

    public void setTeleportZ(int teleportZ) {
        this.teleporterData.set(3, teleportZ);
    }

    public int getItemTeleportZ() {
        return this.teleporterData.get(7);
    }

    public void setItemTeleportZ(int itemTeleportZ) {
        this.teleporterData.set(7, itemTeleportZ);
    }

    public boolean isAutoDelete() {
        return this.teleporterData.get(4) == 1;
    }

    public void setAutoDelete(boolean isAutoDelete) {
        this.teleporterData.set(4, (isAutoDelete ? 1 : 0));
    }

    public boolean isTeleportSaver() {
        return this.teleporterData.get(8) == 1;
    }

    public boolean isTeleporter() {
        return this.teleporterData.get(9) == 1;
    }

    public TileTeleporter getTileEntity() {
        return this.tileEntity;
    }

    static class TeleportSlot extends Slot {
        public TeleportSlot(Container container, int id, int x, int y) {
            super(container, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return ((itemStack.getItem() == ItemInit.TELEPORT_SAVER.get())
                    || (itemStack.getItem() == ItemInit.PORTABLE_TELEPORTER.get()));
        }

        public int getMaxStackSize() {
            return 1;
        }
    }
}
