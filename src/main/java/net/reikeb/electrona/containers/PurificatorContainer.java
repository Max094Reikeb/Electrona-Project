package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.CapabilityItemHandler;
import net.reikeb.electrona.blockentities.PurificatorBlockEntity;

import static net.reikeb.electrona.init.ContainerInit.PURIFICATOR_CONTAINER;

public class PurificatorContainer extends AbstractContainer {

    public PurificatorBlockEntity purificatorBlockEntity;

    public PurificatorContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(PURIFICATOR_CONTAINER.get(), id, 3);

        this.purificatorBlockEntity = (PurificatorBlockEntity) player.getCommandSenderWorld().getBlockEntity(pos);
        if (purificatorBlockEntity == null) return;

        purificatorBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new WaterBucketSlot(h, 0, 27, 27));
            addSlot(new BasicInputSlot(h, 1, 56, 40));
            addSlot(new PurificatorOutputSlot(h, 2, 136, 40));
        });

        this.layoutPlayerInventorySlots(inv);
        this.addSyncedInt(purificatorBlockEntity::setWaterLevel, purificatorBlockEntity::getWaterLevel);
        this.addSyncedInt(purificatorBlockEntity::setPurifyingTime, purificatorBlockEntity::getPurifyingTime);
        this.addSyncedInt(purificatorBlockEntity::setCurrentPurifyingTime, purificatorBlockEntity::getCurrentPurifyingTime);
    }

    public int getCurrentWater() {
        return purificatorBlockEntity.getWaterLevel();
    }

    public int getPurifyingTime() {
        return purificatorBlockEntity.getPurifyingTime();
    }

    public int getCurrentPurifyingTime() {
        return purificatorBlockEntity.getCurrentPurifyingTime();
    }
}
