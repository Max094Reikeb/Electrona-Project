package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.CapabilityItemHandler;
import net.reikeb.electrona.blockentities.MiningMachineBlockEntity;
import net.reikeb.maxilib.abs.AbstractContainer;
import net.reikeb.maxilib.inventory.Slots;

import static net.reikeb.electrona.init.ContainerInit.MINING_MACHINE_CONTAINER;

public class MiningMachineContainer extends AbstractContainer {

    public MiningMachineBlockEntity miningMachineBlockEntity;

    public MiningMachineContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(MINING_MACHINE_CONTAINER.get(), id, 3);

        this.miningMachineBlockEntity = (MiningMachineBlockEntity) player.getCommandSenderWorld().getBlockEntity(pos);
        if (miningMachineBlockEntity == null) return;

        miningMachineBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new Slots(h, 0, 91, 12));
            addSlot(new Slots(h, 1, 74, 51, c -> c.getItem() == Items.BUCKET, 1));
            addSlot(new Slots(h, 2, 108, 51, c -> c.getItem() == Items.BUCKET, 1));
        });

        this.layoutPlayerInventorySlots(inv);
        this.addSyncedInt(miningMachineBlockEntity::setElectronicPowerTimesHundred, miningMachineBlockEntity::getElectronicPowerTimesHundred);
    }

    public double getElectronicPower() {
        return miningMachineBlockEntity.getElectronicPower();
    }
}
