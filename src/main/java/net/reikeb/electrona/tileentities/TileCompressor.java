package net.reikeb.electrona.tileentities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.entity.player.*;
import net.minecraft.world.Containers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.*;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.level.Level;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.*;
import net.minecraftforge.items.*;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.blocks.Compressor;
import net.reikeb.electrona.containers.CompressorContainer;
import net.reikeb.electrona.events.local.CompressionEvent;
import net.reikeb.electrona.init.*;
import net.reikeb.electrona.recipes.CompressorRecipe;
import net.reikeb.electrona.utils.ItemHandler;

import static net.reikeb.electrona.init.TileEntityInit.*;

import javax.annotation.Nullable;

import java.util.*;
import java.util.stream.Collectors;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;

public class TileCompressor extends RandomizableContainerBlockEntity implements TickableBlockEntity {

    private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
    private final ItemHandler inventory;

    public double electronicPower;
    private int maxStorage;

    public int compressingTime;
    public int currentCompressingTime;
    private int energyRequired;

    private boolean canCompress;

    public TileCompressor() {
        super(TILE_COMPRESSOR.get());

        this.inventory = new ItemHandler(3);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.electrona.compressor.name");
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("compressor");
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
        return new CompressorContainer(windowID, playerInv, this);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new CompressorContainer(ContainerInit.COMPRESSOR_CONTAINER.get(), id);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public void tick() {
        Level world = this.level;
        BlockPos blockPos = this.getBlockPos();
        ItemStack stackInSlot0 = this.inventory.getStackInSlot(0);
        ItemStack stackInSlot1 = this.inventory.getStackInSlot(1);

        double electronicPower = this.getTileData().getDouble("ElectronicPower");
        this.getTileData().putInt("MaxStorage", 5000);

        if ((world != null) && (!world.isClientSide)) {
            if ((electronicPower > 0) && (this.getRecipe(stackInSlot0, stackInSlot1) != null)) {
                if (this.canCompress) {
                    this.energyRequired = getEnergyRequired(stackInSlot0, stackInSlot1);
                    this.compressingTime = getCompressingTime(stackInSlot0, stackInSlot1);
                    ItemStack output = this.getRecipe(stackInSlot0, stackInSlot1).getResultItem();
                    double energyPerSecond = (double) this.energyRequired / this.compressingTime;

                    if (this.currentCompressingTime < (this.compressingTime * 20)) {
                        this.currentCompressingTime += 1;
                        electronicPower = electronicPower - (energyPerSecond * 0.05);

                    } else {
                        if (!MinecraftForge.EVENT_BUS.post(new CompressionEvent(world, blockPos, stackInSlot0, stackInSlot1, output.copy(), this.compressingTime, this.energyRequired))) {
                            this.currentCompressingTime = 0;
                            this.inventory.insertItem(2, output.copy(), false);
                            this.inventory.decrStackSize(0, 1);
                            this.inventory.decrStackSize(1, 1);
                            world.playSound(null, blockPos, SoundsInit.COMPRESSOR_END_COMPRESSION.get(),
                                    SoundSource.BLOCKS, 0.6F, 1.0F);
                        }
                    }
                    this.getTileData().putDouble("ElectronicPower", electronicPower);
                } else {
                    this.currentCompressingTime = 0;
                }
            } else {
                this.currentCompressingTime = 0;
            }
            world.setBlockAndUpdate(blockPos, this.getBlockState()
                    .setValue(Compressor.COMPRESSING, this.currentCompressingTime > 0));

            this.getTileData().putInt("CurrentCompressingTime", this.currentCompressingTime);
            this.getTileData().putInt("CompressingTime", this.compressingTime);
        }

        if (world != null) {
            this.setChanged();
            world.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(),
                    Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    protected void canCompress(@Nullable Recipe<?> recipe) {
        if (!this.inventory.getStackInSlot(0).isEmpty() && recipe != null) {
            ItemStack resultItem = recipe.getResultItem();
            if (resultItem.isEmpty()) {
                this.canCompress = false;
            } else {
                ItemStack stackInSlot = this.inventory.getStackInSlot(2);
                if (stackInSlot.isEmpty()) {
                    this.canCompress = true;
                } else if (!stackInSlot.sameItem(resultItem)) {
                    this.canCompress = false;
                } else if (stackInSlot.getCount() + resultItem.getCount() <= 64 && stackInSlot.getCount() + resultItem.getCount() <= stackInSlot.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    this.canCompress = true;
                } else {
                    this.canCompress = stackInSlot.getCount() + resultItem.getCount() <= resultItem.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            this.canCompress = false;
        }
    }

    public int getCompressingTime(ItemStack stack, ItemStack stack1) {
        if (ItemStack.EMPTY == stack || ItemStack.EMPTY == stack1) return 0;
        return this.getRecipe(stack, stack1).getCompressingTime();
    }

    public int getEnergyRequired(ItemStack stack, ItemStack stack1) {
        if (ItemStack.EMPTY == stack || ItemStack.EMPTY == stack1) return 0;
        return this.getRecipe(stack, stack1).getEnergyRequired();
    }

    public final IItemHandlerModifiable getInventory() {
        return this.inventory;
    }

    @Nullable
    private CompressorRecipe getRecipe(ItemStack stack, ItemStack stack2) {
        if (stack == null || stack2 == null) return null;

        Set<Recipe<?>> recipes = findRecipesByType(Electrona.COMPRESSING, this.level);
        for (Recipe<?> iRecipe : recipes) {
            CompressorRecipe recipe = (CompressorRecipe) iRecipe;
            if (recipe.matches(new RecipeWrapper(this.inventory), this.level)) {
                canCompress(recipe);
                return recipe;
            }
        }
        return null;
    }

    public static Set<Recipe<?>> findRecipesByType(RecipeType<?> typeIn, Level world) {
        return world != null ? world.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Collections.emptySet();
    }

    @Override
    public void load(BlockState blockState, CompoundTag compound) {
        super.load(blockState, compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        this.compressingTime = compound.getInt("CompressingTime");
        this.currentCompressingTime = compound.getInt("CurrentCompressingTime");
        this.energyRequired = compound.getInt("EnergyRequired");
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundTag) compound.get("Inventory"));
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.putInt("CompressingTime", this.compressingTime);
        compound.putInt("CurrentCompressingTime", this.currentCompressingTime);
        compound.putInt("EnergyRequired", this.energyRequired);
        compound.put("Inventory", inventory.serializeNBT());
        return compound;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this.inventory));
    }

    public void dropItems(Level world, BlockPos pos) {
        for (int i = 0; i < 3; i++)
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
        return 3;
    }
}
