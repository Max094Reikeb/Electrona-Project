package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;

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
        this.trackData();
    }

    public int getElectronicPower() {
        return (int) tileBattery.getElectronicPower();
    }

    private void trackData() {
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return getElectronicPower() & 0xffff;
            }

            @Override
            public void set(int value) {
                int energyStored = getElectronicPower() & 0xffff0000;
                tileBattery.setElectronicPower(energyStored + (value & 0xffff));
            }
        });
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return (getElectronicPower() >> 16) & 0xffff;
            }

            @Override
            public void set(int value) {
                int energyStored = getElectronicPower() & 0x0000ffff;
                tileBattery.setElectronicPower(energyStored | (value << 16));
            }
        });
    }
}
