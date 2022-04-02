package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.containers.LeadCrateContainer;

import static net.reikeb.electrona.init.BlockEntityInit.LEAD_CRATE_BLOCK_ENTITY;

public class LeadCrateBlockEntity extends AbstractBlockEntity {

    public LeadCrateBlockEntity(BlockPos pos, BlockState state) {
        super(LEAD_CRATE_BLOCK_ENTITY.get(), pos, state, 27);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.electrona.lead_crate.name");
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("lead_crate");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new LeadCrateContainer(id, this.getBlockPos(), playerInventory, player);
    }
}
