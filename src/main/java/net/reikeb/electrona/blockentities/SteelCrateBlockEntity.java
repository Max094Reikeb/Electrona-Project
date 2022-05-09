package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.SteelCrateContainer;
import net.reikeb.maxilib.abs.AbstractBlockEntity;

import static net.reikeb.electrona.init.BlockEntityInit.STEEL_CRATE_BLOCK_ENTITY;

public class SteelCrateBlockEntity extends AbstractBlockEntity {

    public SteelCrateBlockEntity(BlockPos pos, BlockState state) {
        super(STEEL_CRATE_BLOCK_ENTITY.get(), pos, state, "steel_crate", Electrona.MODID, 27);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new SteelCrateContainer(id, this.getBlockPos(), playerInventory, player);
    }
}
