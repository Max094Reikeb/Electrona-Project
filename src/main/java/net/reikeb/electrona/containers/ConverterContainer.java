package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

import net.minecraftforge.items.CapabilityItemHandler;

import net.reikeb.electrona.tileentities.TileConverter;

import static net.reikeb.electrona.init.ContainerInit.CONVERTER_CONTAINER;

public class ConverterContainer extends AbstractContainer {

    public TileConverter tileConverter;

    public ConverterContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(CONVERTER_CONTAINER.get(), id, 1);

        this.tileConverter = (TileConverter) player.getCommandSenderWorld().getBlockEntity(pos);
        if (tileConverter == null) return;

        tileConverter.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new BatterySlot(h, 0, 81, 31));
        });

        this.layoutPlayerInventorySlots(inv);
        this.addSyncedInt(tileConverter::setElectronicPower, tileConverter::getElectronicPower);
        this.addSyncedInt(tileConverter::setForgeEnergy, tileConverter::getForgeEnergy);
        this.addSyncedInt(tileConverter::setVP, tileConverter::getVP);
        this.addSyncedInt(tileConverter::setToVP, tileConverter::isToVP);
        this.addSyncedInt(tileConverter::setToOthers, tileConverter::isToOthers);
    }

    public int getElectronicPower() {
        return tileConverter.getElectronicPower();
    }

    public int getFE() {
        return tileConverter.getForgeEnergy();
    }

    public int getVP() {
        return tileConverter.getVP();
    }

    public boolean isToVP() {
        return tileConverter.isToVP() == 1;
    }

    public void setToVP(boolean isToVP) {
        tileConverter.setToVP(isToVP ? 1 : 0);
    }

    public boolean isToOthers() {
        return tileConverter.isToOthers() == 1;
    }

    public void setToOthers(boolean isToOthers) {
        tileConverter.setToOthers(isToOthers ? 1 : 0);
    }
}
