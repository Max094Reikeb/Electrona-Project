package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

import net.minecraftforge.items.CapabilityItemHandler;

import net.reikeb.electrona.blockentities.TileMiningMachine;

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
        this.addSyncedInt(tileMiningMachine::setElectronicPower, tileMiningMachine::getElectronicPower);
    }

    public int getElectronicPower() {
        return tileMiningMachine.getElectronicPower();
    }
}
