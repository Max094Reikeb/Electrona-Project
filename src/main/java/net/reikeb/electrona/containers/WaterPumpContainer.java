package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.CapabilityItemHandler;
import net.reikeb.electrona.blockentities.WaterPumpBlockEntity;
import net.reikeb.maxilib.abs.AbstractContainer;
import net.reikeb.maxilib.inventory.Slots;

import static net.reikeb.electrona.init.ContainerInit.WATER_PUMP_CONTAINER;

public class WaterPumpContainer extends AbstractContainer {

    public WaterPumpBlockEntity waterPumpBlockEntity;

    public WaterPumpContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(WATER_PUMP_CONTAINER.get(), id, 2);

        this.waterPumpBlockEntity = (WaterPumpBlockEntity) player.getCommandSenderWorld().getBlockEntity(pos);
        if (waterPumpBlockEntity == null) return;

        waterPumpBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new Slots(h, 0, 36, 35, c -> c.getItem() == Items.BUCKET, 1));
            addSlot(new Slots(h, 1, 137, 29, 1));
        });

        this.layoutPlayerInventorySlots(inv);
        this.addSyncedInt(waterPumpBlockEntity::setHundredEnergy, waterPumpBlockEntity::getHundredEnergy);
        this.addSyncedInt(waterPumpBlockEntity::setWaterLevel, waterPumpBlockEntity::getWaterLevel);
        this.addSyncedInt(waterPumpBlockEntity::setOn, waterPumpBlockEntity::isOn);
    }

    public double getEnergy() {
        return waterPumpBlockEntity.getEnergy();
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
