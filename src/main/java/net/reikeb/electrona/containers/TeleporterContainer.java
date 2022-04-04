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
        this.addSyncedInt(teleporterBlockEntity::setElectronicPowerTimesHundred, teleporterBlockEntity::getElectronicPowerTimesHundred);
        this.addSyncedInt(teleporterBlockEntity::setTeleportXTimesHundred, teleporterBlockEntity::getTeleportXTimesHundred);
        this.addSyncedInt(teleporterBlockEntity::setTeleportYTimesHundred, teleporterBlockEntity::getTeleportYTimesHundred);
        this.addSyncedInt(teleporterBlockEntity::setTeleportZTimesHundred, teleporterBlockEntity::getTeleportZTimesHundred);
        this.addSyncedInt(teleporterBlockEntity::setItemTeleportXTimesHundred, teleporterBlockEntity::getItemTeleportXTimesHundred);
        this.addSyncedInt(teleporterBlockEntity::setItemTeleportYTimesHundred, teleporterBlockEntity::getItemTeleportYTimesHundred);
        this.addSyncedInt(teleporterBlockEntity::setItemTeleportZTimesHundred, teleporterBlockEntity::getItemTeleportZTimesHundred);
        this.addSyncedInt(teleporterBlockEntity::setAutoDeletion, teleporterBlockEntity::isAutoDeletion);
        this.addSyncedInt(teleporterBlockEntity::setTeleportSaver, teleporterBlockEntity::isTeleportSaver);
        this.addSyncedInt(teleporterBlockEntity::setTeleporter, teleporterBlockEntity::isTeleporter);
    }

    public double getElectronicPower() {
        return teleporterBlockEntity.getElectronicPower();
    }

    public double getTeleportX() {
        return teleporterBlockEntity.getTeleportX();
    }

    public void setTeleportX(double teleportX) {
        teleporterBlockEntity.setTeleportX(teleportX);
    }

    public double getItemTeleportX() {
        return teleporterBlockEntity.getItemTeleportX();
    }

    public void setItemTeleportX(double itemTeleportX) {
        teleporterBlockEntity.setItemTeleportX(itemTeleportX);
    }

    public double getTeleportY() {
        return teleporterBlockEntity.getTeleportY();
    }

    public void setTeleportY(double teleportY) {
        teleporterBlockEntity.setTeleportY(teleportY);
    }

    public double getItemTeleportY() {
        return teleporterBlockEntity.getItemTeleportY();
    }

    public void setItemTeleportY(double itemTeleportY) {
        teleporterBlockEntity.setItemTeleportY(itemTeleportY);
    }

    public double getTeleportZ() {
        return teleporterBlockEntity.getTeleportZ();
    }

    public void setTeleportZ(double teleportZ) {
        teleporterBlockEntity.setTeleportZ(teleportZ);
    }

    public double getItemTeleportZ() {
        return teleporterBlockEntity.getItemTeleportZ();
    }

    public void setItemTeleportZ(double itemTeleportZ) {
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
