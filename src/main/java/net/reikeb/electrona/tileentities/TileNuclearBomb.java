package net.reikeb.electrona.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.*;

import net.reikeb.electrona.containers.NuclearBombContainer;
import net.reikeb.electrona.init.*;
import net.reikeb.electrona.utils.ItemHandler;

import static net.reikeb.electrona.init.TileEntityInit.*;

public class TileNuclearBomb extends LockableLootTileEntity {

    private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY);
    private final ItemHandler inventory;

    public TileNuclearBomb() {
        super(TILE_NUCLEAR_BOMB.get());

        this.inventory = new ItemHandler(2);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gui.electrona.nuclear_bomb.name");
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new StringTextComponent("nuclear_bomb");
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
    public Container createMenu(final int windowID, final PlayerInventory playerInv, final PlayerEntity playerIn) {
        return new NuclearBombContainer(windowID, playerInv, this);
    }

    @Override
    public Container createMenu(int id, PlayerInventory player) {
        return new NuclearBombContainer(ContainerInit.NUCLEAR_BOMB_CONTAINER.get(), id);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    public final IItemHandlerModifiable getInventory() {
        return this.inventory;
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
            returnValue = 63;
        } else if ((this.inventory.getStackInSlot(0).getItem() == ItemInit.URANIUM_DUAL_BAR.get())
                && (this.inventory.getStackInSlot(1).getItem() == ItemInit.URANIUM_QUAD_BAR.get())) {
            returnValue = 84;
        }
        return returnValue;
    }

    @Override
    public void load(BlockState blockState, CompoundNBT compound) {
        super.load(blockState, compound);
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundNBT) compound.get("Inventory"));
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.put("Inventory", inventory.serializeNBT());
        return compound;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this.inventory));
    }

    public void dropItems(World world, BlockPos pos) {
        for (int i = 0; i < 2; i++)
            if (!inventory.getStackInSlot(i).isEmpty()) {
                InventoryHelper.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(i));
            }
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 0, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.load(this.getBlockState(), pkt.getTag());
    }

    @Override
    public int getContainerSize() {
        return 2;
    }
}
