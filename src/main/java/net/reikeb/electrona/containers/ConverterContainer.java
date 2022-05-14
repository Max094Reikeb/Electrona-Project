package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.CapabilityItemHandler;
import net.reikeb.electrona.blockentities.ConverterBlockEntity;
import net.reikeb.maxilib.abs.AbstractContainer;
import net.reikeb.maxilib.inventory.Slots;

import static net.reikeb.electrona.init.ContainerInit.CONVERTER_CONTAINER;

public class ConverterContainer extends AbstractContainer {

    public ConverterBlockEntity converterBlockEntity;

    public ConverterContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(CONVERTER_CONTAINER.get(), id, 1);

        this.converterBlockEntity = (ConverterBlockEntity) player.getCommandSenderWorld().getBlockEntity(pos);
        if (converterBlockEntity == null) return;

        converterBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new Slots(h, 0, 81, 31, 1));
        });

        this.layoutPlayerInventorySlots(inv);
        this.addSyncedInt(converterBlockEntity::setElectronicPowerTimesHundred, converterBlockEntity::getElectronicPowerTimesHundred);
        this.addSyncedInt(converterBlockEntity::setForgeEnergy, converterBlockEntity::getForgeEnergy);
        this.addSyncedInt(converterBlockEntity::setVPTimesHundred, converterBlockEntity::getVPTimesHundred);
        this.addSyncedInt(converterBlockEntity::setToVP, converterBlockEntity::isToVP);
        this.addSyncedInt(converterBlockEntity::setToOthers, converterBlockEntity::isToOthers);
    }

    public double getElectronicPower() {
        return converterBlockEntity.getElectronicPower();
    }

    public int getFE() {
        return converterBlockEntity.getForgeEnergy();
    }

    public double getVP() {
        return converterBlockEntity.getVP();
    }

    public boolean isToVP() {
        return converterBlockEntity.isToVP() == 1;
    }

    public void setToVP(boolean isToVP) {
        converterBlockEntity.setToVP(isToVP ? 1 : 0);
    }

    public boolean isToOthers() {
        return converterBlockEntity.isToOthers() == 1;
    }

    public void setToOthers(boolean isToOthers) {
        converterBlockEntity.setToOthers(isToOthers ? 1 : 0);
    }
}
