package fr.firstmegagame4.electrona.screenhandlers;

import fr.firstmegagame4.electrona.init.ScreenHandlers;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;

public class SteelCrateScreenHandler extends CrateScreenHandler {

    public SteelCrateScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(27));
    }

    public SteelCrateScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ScreenHandlers.STEEL_CRATE_SCREEN_HANDLER, syncId);
        this.setupInventory(playerInventory, inventory);
    }

}
