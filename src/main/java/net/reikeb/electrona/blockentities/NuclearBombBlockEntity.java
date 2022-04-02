package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.containers.NuclearBombContainer;
import net.reikeb.electrona.init.ItemInit;

import static net.reikeb.electrona.init.BlockEntityInit.NUCLEAR_BOMB_BLOCK_ENTITY;

public class NuclearBombBlockEntity extends AbstractBlockEntity {

    public NuclearBombBlockEntity(BlockPos pos, BlockState state) {
        super(NUCLEAR_BOMB_BLOCK_ENTITY.get(), pos, state, 2);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.electrona.nuclear_bomb.name");
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("nuclear_bomb");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new NuclearBombContainer(id, this.getBlockPos(), playerInventory, player);
    }

    public boolean isCharged() {
        return (((this.inventory.getStackInSlot(0).getItem() == ItemInit.URANIUM_BAR.get())
                && (this.inventory.getStackInSlot(1).getItem() == ItemInit.URANIUM_DUAL_BAR.get()))
                || ((this.inventory.getStackInSlot(0).getItem() == ItemInit.URANIUM_DUAL_BAR.get())
                && (this.inventory.getStackInSlot(1).getItem() == ItemInit.URANIUM_QUAD_BAR.get())));
    }

    public int getNuclearCharge() {
        int returnValue = 0;
        if ((this.inventory.getStackInSlot(0).getItem() == ItemInit.URANIUM_BAR.get())
                && (this.inventory.getStackInSlot(1).getItem() == ItemInit.URANIUM_DUAL_BAR.get())) {
            returnValue = 50;
        } else if ((this.inventory.getStackInSlot(0).getItem() == ItemInit.URANIUM_DUAL_BAR.get())
                && (this.inventory.getStackInSlot(1).getItem() == ItemInit.URANIUM_QUAD_BAR.get())) {
            returnValue = 100;
        }
        return returnValue;
    }
}
