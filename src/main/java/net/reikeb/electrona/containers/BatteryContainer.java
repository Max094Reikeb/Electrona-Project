package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

import net.minecraftforge.items.CapabilityItemHandler;

import net.reikeb.electrona.tileentities.TileBattery;

import static net.reikeb.electrona.init.ContainerInit.BATTERY_CONTAINER;

public class BatteryContainer extends AbstractContainer {

    public TileBattery tileBattery;

    public BatteryContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(BATTERY_CONTAINER.get(), id, 2);

        this.tileBattery = (TileBattery) player.getCommandSenderWorld().getBlockEntity(pos);
        if (tileBattery == null) return;

        tileBattery.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new BatterySlot(h, 0, 45, 33));
            addSlot(new BatterySlot(h, 1, 117, 33));
        });

        this.layoutPlayerInventorySlots(inv);
        this.addSyncedInt(tileBattery::setElectronicPower, tileBattery::getElectronicPower);
    }

    public int getElectronicPower() {
        return tileBattery.getElectronicPower();
    }
}
