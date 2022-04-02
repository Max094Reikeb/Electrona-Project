package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

import net.minecraftforge.items.CapabilityItemHandler;

import net.reikeb.electrona.blockentities.TeleporterBlockEntity;

import static net.reikeb.electrona.init.ContainerInit.TELEPORTER_CONTAINER;

public class TeleporterContainer extends AbstractContainer {

    public TeleporterBlockEntity teleporterBlockEntity;

    public TeleporterContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(TELEPORTER_CONTAINER.get(), id, 1);

        this.teleporterBlockEntity = (TeleporterBlockEntity) player.getCommandSenderWorld().getBlockEntity(pos);
        if (teleporterBlockEntity == null) return;

        teleporterBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new TeleportSlot(h, 0, 81, 27));
        });

        this.layoutPlayerInventorySlots(inv);
        this.addSyncedInt(teleporterBlockEntity::setElectronicPower, teleporterBlockEntity::getElectronicPower);
        this.addSyncedInt(teleporterBlockEntity::setTeleportX, teleporterBlockEntity::getTeleportX);
        this.addSyncedInt(teleporterBlockEntity::setTeleportY, teleporterBlockEntity::getTeleportY);
        this.addSyncedInt(teleporterBlockEntity::setTeleportZ, teleporterBlockEntity::getTeleportZ);
        this.addSyncedInt(teleporterBlockEntity::setAutoDeletion, teleporterBlockEntity::isAutoDeletion);
        this.addSyncedInt(teleporterBlockEntity::setItemTeleportX, teleporterBlockEntity::getItemTeleportX);
        this.addSyncedInt(teleporterBlockEntity::setItemTeleportY, teleporterBlockEntity::getItemTeleportY);
        this.addSyncedInt(teleporterBlockEntity::setItemTeleportZ, teleporterBlockEntity::getItemTeleportZ);
        this.addSyncedInt(teleporterBlockEntity::setTeleportSaver, teleporterBlockEntity::isTeleportSaver);
        this.addSyncedInt(teleporterBlockEntity::setTeleporter, teleporterBlockEntity::isTeleporter);
    }

    public int getElectronicPower() {
        return teleporterBlockEntity.getElectronicPower();
    }

    public int getTeleportX() {
        return teleporterBlockEntity.getTeleportX();
    }

    public void setTeleportX(int teleportX) {
        teleporterBlockEntity.setTeleportX(teleportX);
    }

    public int getItemTeleportX() {
        return teleporterBlockEntity.getItemTeleportX();
    }

    public void setItemTeleportX(int itemTeleportX) {
        teleporterBlockEntity.setItemTeleportX(itemTeleportX);
    }

    public int getTeleportY() {
        return teleporterBlockEntity.getTeleportY();
    }

    public void setTeleportY(int teleportY) {
        teleporterBlockEntity.setTeleportY(teleportY);
    }

    public int getItemTeleportY() {
        return teleporterBlockEntity.getItemTeleportY();
    }

    public void setItemTeleportY(int itemTeleportY) {
        teleporterBlockEntity.setItemTeleportY(itemTeleportY);
    }

    public int getTeleportZ() {
        return teleporterBlockEntity.getTeleportZ();
    }

    public void setTeleportZ(int teleportZ) {
        teleporterBlockEntity.setTeleportZ(teleportZ);
    }

    public int getItemTeleportZ() {
        return teleporterBlockEntity.getItemTeleportZ();
    }

    public void setItemTeleportZ(int itemTeleportZ) {
        teleporterBlockEntity.setItemTeleportZ(itemTeleportZ);
    }

    public boolean isAutoDelete() {
        return teleporterBlockEntity.isAutoDeletion() == 1;
    }

    public void setAutoDelete(boolean isAutoDelete) {
        teleporterBlockEntity.setAutoDeletion(isAutoDelete ? 1 : 0);
    }

    public boolean isTeleportSaver() {
        return teleporterBlockEntity.isTeleportSaver() == 1;
    }

    public boolean isTeleporter() {
        return teleporterBlockEntity.isTeleporter() == 1;
    }

    public TeleporterBlockEntity getBlockEntity() {
        return this.teleporterBlockEntity;
    }
}
