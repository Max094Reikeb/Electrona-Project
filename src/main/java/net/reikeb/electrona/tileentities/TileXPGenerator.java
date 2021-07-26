package net.reikeb.electrona.tileentities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.entity.player.*;
import net.minecraft.world.Containers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.level.Level;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.*;
import net.minecraftforge.items.*;

import net.reikeb.electrona.blocks.XPGenerator;
import net.reikeb.electrona.containers.XPGeneratorContainer;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;

public class TileXPGenerator extends RandomizableContainerBlockEntity implements TickableBlockEntity {

    private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
    private final ItemHandler inventory;

    public double electronicPower;
    private int maxStorage;
    private int wait;
    private int xpLevels;

    public TileXPGenerator() {
        super(TILE_XP_GENERATOR.get());

        this.inventory = new ItemHandler(1);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.electrona.xp_generator.name");
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("xp_generator");
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
        return new XPGeneratorContainer(windowID, playerInv, this);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new XPGeneratorContainer(ContainerInit.XP_GENERATOR_CONTAINER.get(), id);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public void tick() {
        // We get the variables
        Level world = this.level;
        BlockPos blockPos = this.getBlockPos();

        // We get the NBT Tags
        this.getTileData().putInt("MaxStorage", 10000);
        double electronicPower = this.getTileData().getDouble("ElectronicPower");
        int xpLevel = this.getTileData().getInt("XPLevels");
        int xp = this.getTileData().getInt("wait");

        if (world != null) { // Avoid NullPointerExceptions

            // Handle slot
            if ((electronicPower >= 0.8) && (this.inventory.getStackInSlot(0).getItem() == Items.EMERALD)) {
                xp += 1;
                this.getTileData().putInt("wait", xp);
                this.getTileData().putDouble("ElectronicPower", electronicPower - 0.8);
                if (xp >= 4800) {
                    this.inventory.decrStackSize(0, 1);
                    this.getTileData().putInt("XPLevels", xpLevel + 1);
                    this.getTileData().putInt("wait", 0);
                }
            } else {
                this.getTileData().putInt("wait", 0);
            }
            world.setBlockAndUpdate(blockPos, this.getBlockState()
                    .setValue(XPGenerator.ACTIVATED, (xpLevel > 0 || xp > 0)));

            this.setChanged();
            world.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(),
                    Constants.BlockFlags.NOTIFY_NEIGHBORS);
        }
    }

    public final IItemHandlerModifiable getInventory() {
        return this.inventory;
    }

    @Override
    public void load(BlockState blockState, CompoundTag compound) {
        super.load(blockState, compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        this.xpLevels = compound.getInt("XPLevels");
        this.wait = compound.getInt("wait");
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundTag) compound.get("Inventory"));
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.putInt("XPLevels", this.xpLevels);
        compound.putInt("wait", this.wait);
        compound.put("Inventory", inventory.serializeNBT());
        return compound;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this.inventory));
    }

    public void dropItems(Level world, BlockPos pos) {
        for (int i = 0; i < 1; i++)
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
        return 1;
    }
}
