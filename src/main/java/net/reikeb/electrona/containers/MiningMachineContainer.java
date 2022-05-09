package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.CapabilityItemHandler;
import net.reikeb.electrona.blockentities.MiningMachineBlockEntity;
import net.reikeb.electrona.misc.Slots;
import net.reikeb.maxilib.abs.AbstractContainer;

import static net.reikeb.electrona.init.ContainerInit.MINING_MACHINE_CONTAINER;

public class MiningMachineContainer extends AbstractContainer {

    public MiningMachineBlockEntity miningMachineBlockEntity;

    public MiningMachineContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(MINING_MACHINE_CONTAINER.get(), id, 3);

        this.miningMachineBlockEntity = (MiningMachineBlockEntity) player.getCommandSenderWorld().getBlockEntity(pos);
        if (miningMachineBlockEntity == null) return;

        miningMachineBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new Slots.BasicInputSlot(h, 0, 91, 12));
            addSlot(new Slots.BucketSlot(h, 1, 74, 51));
            addSlot(new Slots.BucketSlot(h, 2, 108, 51));
        });

        this.layoutPlayerInventorySlots(inv);
        this.addSyncedInt(miningMachineBlockEntity::setElectronicPowerTimesHundred, miningMachineBlockEntity::getElectronicPowerTimesHundred);
    }

    public double getElectronicPower() {
        return miningMachineBlockEntity.getElectronicPower();
    }
}
