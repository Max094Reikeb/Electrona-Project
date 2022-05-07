package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.CapabilityItemHandler;
import net.reikeb.electrona.blockentities.NuclearGeneratorControllerBlockEntity;

import static net.reikeb.electrona.init.ContainerInit.NUCLEAR_GENERATOR_CONTAINER;

public class NuclearGeneratorControllerContainer extends AbstractContainer {

    public NuclearGeneratorControllerBlockEntity nuclearGeneratorControllerBlockEntity;

    public NuclearGeneratorControllerContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(NUCLEAR_GENERATOR_CONTAINER.get(), id, 2);

        this.nuclearGeneratorControllerBlockEntity = (NuclearGeneratorControllerBlockEntity) player.getCommandSenderWorld().getBlockEntity(pos);
        if (nuclearGeneratorControllerBlockEntity == null) return;

        nuclearGeneratorControllerBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new WaterBucketSlot(h, 0, 27, 32));
            addSlot(new UraniumSlot(h, nuclearGeneratorControllerBlockEntity, 1, 55, 32));
        });

        this.layoutPlayerInventorySlots(inv);
        this.addSyncedInt(nuclearGeneratorControllerBlockEntity::setElectronicPowerTimesHundred, nuclearGeneratorControllerBlockEntity::getElectronicPowerTimesHundred);
        this.addSyncedInt(nuclearGeneratorControllerBlockEntity::setUnderWater, nuclearGeneratorControllerBlockEntity::getUnderWater);
        this.addSyncedInt(nuclearGeneratorControllerBlockEntity::setTemperature, nuclearGeneratorControllerBlockEntity::getTemperature);
        this.addSyncedInt(nuclearGeneratorControllerBlockEntity::setPowered, nuclearGeneratorControllerBlockEntity::isPowered);
        this.addSyncedInt(nuclearGeneratorControllerBlockEntity::setUbIn, nuclearGeneratorControllerBlockEntity::areUbIn);
        this.addSyncedInt(nuclearGeneratorControllerBlockEntity::setAlert, nuclearGeneratorControllerBlockEntity::isAlert);
        this.addSyncedInt(nuclearGeneratorControllerBlockEntity::setOverCooler, nuclearGeneratorControllerBlockEntity::isOverCooler);
        this.addSyncedInt(nuclearGeneratorControllerBlockEntity::setPosXUnder, nuclearGeneratorControllerBlockEntity::getPosXUnder);
        this.addSyncedInt(nuclearGeneratorControllerBlockEntity::setPosYUnder, nuclearGeneratorControllerBlockEntity::getPosYUnder);
        this.addSyncedInt(nuclearGeneratorControllerBlockEntity::setPosZUnder, nuclearGeneratorControllerBlockEntity::getPosZUnder);
    }

    public double getElectronicPower() {
        return nuclearGeneratorControllerBlockEntity.getElectronicPower();
    }

    public int getUnderWater() {
        return nuclearGeneratorControllerBlockEntity.getUnderWater();
    }

    public int getTemperature() {
        return nuclearGeneratorControllerBlockEntity.getTemperature();
    }

    public boolean isPowered() {
        return nuclearGeneratorControllerBlockEntity.isPowered() == 1;
    }

    public void setPowered(boolean powered) {
        nuclearGeneratorControllerBlockEntity.setPowered(powered ? 1 : 0);
    }

    public boolean areUBIn() {
        return this.nuclearGeneratorControllerBlockEntity.areUbIn() == 1;
    }

    public void setUBIn(boolean ubIn) {
        nuclearGeneratorControllerBlockEntity.setUbIn(ubIn ? 1 : 0);
    }

    public boolean alert() {
        return nuclearGeneratorControllerBlockEntity.isAlert() == 1;
    }

    public boolean isAboveCooler() {
        return nuclearGeneratorControllerBlockEntity.isOverCooler() == 1;
    }

    public int getPosXUnder() {
        return nuclearGeneratorControllerBlockEntity.getPosXUnder();
    }

    public int getPosYUnder() {
        return nuclearGeneratorControllerBlockEntity.getPosYUnder();
    }

    public int getPosZUnder() {
        return nuclearGeneratorControllerBlockEntity.getPosZUnder();
    }
}
