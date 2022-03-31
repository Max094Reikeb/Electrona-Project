package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

import net.minecraftforge.items.CapabilityItemHandler;

import net.reikeb.electrona.tileentities.TileSteelCrate;

import static net.reikeb.electrona.init.ContainerInit.STEEL_CRATE_CONTAINER;

public class SteelCrateContainer extends AbstractContainer {

    public TileSteelCrate tileSteelCrate;

    public SteelCrateContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(STEEL_CRATE_CONTAINER.get(), id, 27);

        this.tileSteelCrate = (TileSteelCrate) player.getCommandSenderWorld().getBlockEntity(pos);
        if (tileSteelCrate == null) return;

        tileSteelCrate.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new BasicInputSlot(h, 0, 8, 18));
            addSlot(new BasicInputSlot(h, 1, 26, 18));
            addSlot(new BasicInputSlot(h, 2, 44, 18));
            addSlot(new BasicInputSlot(h, 3, 62, 18));
            addSlot(new BasicInputSlot(h, 4, 80, 18));
            addSlot(new BasicInputSlot(h, 5, 98, 18));
            addSlot(new BasicInputSlot(h, 6, 116, 18));
            addSlot(new BasicInputSlot(h, 7, 134, 18));
            addSlot(new BasicInputSlot(h, 8, 152, 18));
            addSlot(new BasicInputSlot(h, 9, 8, 36));
            addSlot(new BasicInputSlot(h, 10, 26, 36));
            addSlot(new BasicInputSlot(h, 11, 44, 36));
            addSlot(new BasicInputSlot(h, 12, 62, 36));
            addSlot(new BasicInputSlot(h, 13, 80, 36));
            addSlot(new BasicInputSlot(h, 14, 98, 36));
            addSlot(new BasicInputSlot(h, 15, 116, 36));
            addSlot(new BasicInputSlot(h, 16, 134, 36));
            addSlot(new BasicInputSlot(h, 17, 152, 36));
            addSlot(new BasicInputSlot(h, 18, 8, 54));
            addSlot(new BasicInputSlot(h, 19, 26, 54));
            addSlot(new BasicInputSlot(h, 20, 44, 54));
            addSlot(new BasicInputSlot(h, 21, 62, 54));
            addSlot(new BasicInputSlot(h, 22, 80, 54));
            addSlot(new BasicInputSlot(h, 23, 98, 54));
            addSlot(new BasicInputSlot(h, 24, 116, 54));
            addSlot(new BasicInputSlot(h, 25, 134, 54));
            addSlot(new BasicInputSlot(h, 26, 152, 54));
        });

        this.layoutPlayerInventorySlots(inv);
    }
}
