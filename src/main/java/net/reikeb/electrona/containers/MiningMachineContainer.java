package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;

import net.minecraftforge.items.CapabilityItemHandler;

import net.reikeb.electrona.tileentities.TileMiningMachine;

import static net.reikeb.electrona.init.ContainerInit.MINING_MACHINE_CONTAINER;

public class MiningMachineContainer extends AbstractContainer {

    public TileMiningMachine tileMiningMachine;

    public MiningMachineContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(MINING_MACHINE_CONTAINER.get(), id, 3);

        this.tileMiningMachine = (TileMiningMachine) player.getCommandSenderWorld().getBlockEntity(pos);
        if (tileMiningMachine == null) return;

        tileMiningMachine.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new BasicInputSlot(h, 0, 91, 12));
            addSlot(new BucketSlot(h, 1, 74, 51));
            addSlot(new BucketSlot(h, 2, 108, 51));
        });

        this.layoutPlayerInventorySlots(inv);
        this.trackData();
    }

    public int getElectronicPower() {
        return (int) tileMiningMachine.getElectronicPower();
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
                tileMiningMachine.setElectronicPower(energyStored + (value & 0xffff));
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
                tileMiningMachine.setElectronicPower(energyStored | (value << 16));
            }
        });
    }
}
