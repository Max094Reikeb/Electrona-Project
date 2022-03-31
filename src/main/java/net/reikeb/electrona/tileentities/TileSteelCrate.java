package net.reikeb.electrona.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.containers.SteelCrateContainer;

import static net.reikeb.electrona.init.TileEntityInit.TILE_STEEL_CRATE;

public class TileSteelCrate extends AbstractTileEntity {

    public TileSteelCrate(BlockPos pos, BlockState state) {
        super(TILE_STEEL_CRATE.get(), pos, state, 27);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.electrona.steel_crate.name");
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("steel_crate");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new SteelCrateContainer(id, this.getBlockPos(), playerInventory, player);
    }
}
