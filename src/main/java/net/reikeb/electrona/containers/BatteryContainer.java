package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.CapabilityItemHandler;
import net.reikeb.electrona.blockentities.BatteryBlockEntity;

import static net.reikeb.electrona.init.ContainerInit.BATTERY_CONTAINER;

public class BatteryContainer extends AbstractContainer {

    public BatteryBlockEntity batteryBlockEntity;

    public BatteryContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(BATTERY_CONTAINER.get(), id, 2);

        this.batteryBlockEntity = (BatteryBlockEntity) player.getCommandSenderWorld().getBlockEntity(pos);
        if (batteryBlockEntity == null) return;

        batteryBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new BatterySlot(h, 0, 45, 33));
            addSlot(new BatterySlot(h, 1, 117, 33));
        });

        this.layoutPlayerInventorySlots(inv);
        this.addSyncedInt(batteryBlockEntity::setElectronicPowerTimesHundred, batteryBlockEntity::getElectronicPowerTimesHundred);
    }

    public double getElectronicPower() {
        return batteryBlockEntity.getElectronicPower();
    }
}
