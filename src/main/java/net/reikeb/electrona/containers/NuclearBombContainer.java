package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.CapabilityItemHandler;
import net.reikeb.electrona.blockentities.NuclearBombBlockEntity;
import net.reikeb.maxilib.abs.AbstractContainer;
import net.reikeb.maxilib.inventory.Slots;

import static net.reikeb.electrona.init.ContainerInit.NUCLEAR_BOMB_CONTAINER;

public class NuclearBombContainer extends AbstractContainer {

    public NuclearBombBlockEntity nuclearBombBlockEntity;

    public NuclearBombContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(NUCLEAR_BOMB_CONTAINER.get(), id, 2);

        this.nuclearBombBlockEntity = (NuclearBombBlockEntity) player.getCommandSenderWorld().getBlockEntity(pos);
        if (nuclearBombBlockEntity == null) return;

        nuclearBombBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new Slots(h, 0, 53, 36));
            addSlot(new Slots(h, 1, 96, 36));
        });

        this.layoutPlayerInventorySlots(inv);
    }
}
