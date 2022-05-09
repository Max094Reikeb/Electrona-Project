package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.CapabilityItemHandler;
import net.reikeb.electrona.blockentities.WaterPumpBlockEntity;
import net.reikeb.electrona.misc.Slots;
import net.reikeb.maxilib.abs.AbstractContainer;

import static net.reikeb.electrona.init.ContainerInit.WATER_PUMP_CONTAINER;

public class WaterPumpContainer extends AbstractContainer {

    public WaterPumpBlockEntity waterPumpBlockEntity;

    public WaterPumpContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(WATER_PUMP_CONTAINER.get(), id, 2);

        this.waterPumpBlockEntity = (WaterPumpBlockEntity) player.getCommandSenderWorld().getBlockEntity(pos);
        if (waterPumpBlockEntity == null) return;

        waterPumpBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new Slots.BucketSlot(h, 0, 36, 35));
            addSlot(new Slots.BatterySlot(h, 1, 137, 29));
        });

        this.layoutPlayerInventorySlots(inv);
        this.addSyncedInt(waterPumpBlockEntity::setElectronicPowerTimesHundred, waterPumpBlockEntity::getElectronicPowerTimesHundred);
        this.addSyncedInt(waterPumpBlockEntity::setWaterLevel, waterPumpBlockEntity::getWaterLevel);
        this.addSyncedInt(waterPumpBlockEntity::setOn, waterPumpBlockEntity::isOn);
    }

    public double getElectronicPower() {
        return waterPumpBlockEntity.getElectronicPower();
    }

    public int getWaterLevel() {
        return waterPumpBlockEntity.getWaterLevel();
    }

    public boolean isOn() {
        return waterPumpBlockEntity.isOn() == 1;
    }

    public void setOn(boolean isOn) {
        waterPumpBlockEntity.setOn(isOn ? 1 : 0);
    }
}
