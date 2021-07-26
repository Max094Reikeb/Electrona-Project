package net.reikeb.electrona.tileentities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.entity.player.*;
import net.minecraft.world.Containers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.level.Level;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.*;

import net.reikeb.electrona.containers.LeadCrateContainer;
import net.reikeb.electrona.init.ContainerInit;
import net.reikeb.electrona.utils.ItemHandler;

import static net.reikeb.electrona.init.TileEntityInit.*;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class TileLeadCrate extends RandomizableContainerBlockEntity {

    private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(27, ItemStack.EMPTY);
    private final ItemHandler inventory;

    public TileLeadCrate() {
        super(TILE_LEAD_CRATE.get());

        this.inventory = new ItemHandler(27);
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
    protected NonNullList<ItemStack> getItems() {
        return this.stacks;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> stacks) {
        this.stacks = stacks;
    }

    @Override
    public AbstractContainerMenu createMenu(final int windowID, final Inventory playerInv, final Player playerIn) {
        return new LeadCrateContainer(windowID, playerInv, this);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new LeadCrateContainer(ContainerInit.LEAD_CRATE_CONTAINER.get(), id);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    public final IItemHandlerModifiable getInventory() {
        return this.inventory;
    }

    @Override
    public void load(BlockState blockState, CompoundTag compound) {
        super.load(blockState, compound);
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundTag) compound.get("Inventory"));
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        compound.put("Inventory", inventory.serializeNBT());
        return compound;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this.inventory));
    }

    public void dropItems(Level world, BlockPos pos) {
        for (int i = 0; i < 27; i++)
            if (!inventory.getStackInSlot(i).isEmpty()) {
                Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(i));
            }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 0, this.getUpdateTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(this.getBlockState(), pkt.getTag());
    }

    @Override
    public int getContainerSize() {
        return 27;
    }
}
