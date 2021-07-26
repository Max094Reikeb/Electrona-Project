package net.reikeb.electrona.tileentities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.entity.player.*;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.Containers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.item.*;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.*;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.PurificatorContainer;
import net.reikeb.electrona.events.local.PurificationEvent;
import net.reikeb.electrona.init.*;
import net.reikeb.electrona.misc.vm.FluidFunction;
import net.reikeb.electrona.recipes.PurificatorRecipe;
import net.reikeb.electrona.utils.ItemHandler;

import static net.reikeb.electrona.init.TileEntityInit.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;

public class TilePurificator extends RandomizableContainerBlockEntity implements TickableBlockEntity {

    private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
    private final ItemHandler inventory;

    public int purifyingTime;
    public int currentPurifyingTime;
    private int waterRequired;

    private boolean canPurify;

    public TilePurificator() {
        super(TILE_PURIFICATOR.get());

        this.inventory = new ItemHandler(3);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.electrona.purificator.name");
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("purificator");
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
        return new PurificatorContainer(windowID, playerInv, this);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new PurificatorContainer(ContainerInit.COMPRESSOR_CONTAINER.get(), id);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public void tick() {
        Level world = this.level;
        BlockPos blockPos = this.getBlockPos();
        ItemStack stackInSlot1 = this.inventory.getStackInSlot(1);

        AtomicInteger waterLevel = new AtomicInteger();
        this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).ifPresent(cap -> waterLevel.set(cap.getFluidInTank(1).getAmount()));
        AtomicInteger tankCapacity = new AtomicInteger();
        this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).ifPresent(cap -> tankCapacity.set(cap.getTankCapacity(1)));

        // Input slot - Handling slots
        if ((this.inventory.getStackInSlot(0).getItem() == Items.WATER_BUCKET)
                && (waterLevel.get() <= (tankCapacity.get() - 1000))) {
            this.inventory.decrStackSize(0, 1);
            this.inventory.insertItem(0, new ItemStack(Items.BUCKET, 1), false);
            FluidFunction.fillWater(this, 1000);
        }

        if ((world != null) && (!world.isClientSide)) {
            if ((waterLevel.get() > 0) && (this.getRecipe(stackInSlot1) != null)) {
                if (this.canPurify) {
                    this.waterRequired = getWaterRequired(stackInSlot1);
                    this.purifyingTime = getPurifyingTime(stackInSlot1);
                    ItemStack output = this.getRecipe(stackInSlot1).getResultItem();
                    double waterPerSecond = (double) this.waterRequired / this.purifyingTime;

                    if (this.currentPurifyingTime < (this.purifyingTime * 20)) {
                        this.currentPurifyingTime += 1;
                        FluidFunction.drainWater(this, (int) (waterPerSecond * 0.05));
                        world.playSound(null, blockPos, SoundsInit.PURIFICATOR_PURIFICATION.get(),
                                SoundSource.BLOCKS, 0.6F, 1.0F);

                    } else {
                        if (!MinecraftForge.EVENT_BUS.post(new PurificationEvent(world, blockPos, stackInSlot1, new ItemStack(output.copy().getItem(), this.getRecipe(stackInSlot1).getCountOutput()), this.purifyingTime, this.waterRequired))) {
                            this.currentPurifyingTime = 0;
                            this.inventory.insertItem(2, new ItemStack(output.copy().getItem(), this.getRecipe(stackInSlot1).getCountOutput()), false);
                            this.inventory.decrStackSize(1, this.getRecipe(stackInSlot1).getCountInput());
                            world.playSound(null, blockPos, ForgeRegistries.SOUND_EVENTS
                                            .getValue(new ResourceLocation("block.brewing_stand.brew")),
                                    SoundSource.BLOCKS, 0.6F, 1.0F);
                        }
                    }
                } else {
                    this.currentPurifyingTime = 0;
                }
            } else {
                this.currentPurifyingTime = 0;
            }
            this.getTileData().putInt("CurrentPurifyingTime", this.currentPurifyingTime);
            this.getTileData().putInt("PurifyingTime", this.purifyingTime);
        }

        if (world != null) {
            this.setChanged();
            world.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(),
                    Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    protected void canPurify(@Nullable Recipe<?> recipe) {
        if (!this.inventory.getStackInSlot(1).isEmpty() && recipe != null) {
            ItemStack resultItem = recipe.getResultItem();
            if (resultItem.isEmpty()) {
                this.canPurify = false;
            } else {
                ItemStack stackInSlot = this.inventory.getStackInSlot(2);
                if (stackInSlot.isEmpty()) {
                    this.canPurify = true;
                } else if (!stackInSlot.sameItem(resultItem)) {
                    this.canPurify = false;
                } else if (stackInSlot.getCount() + resultItem.getCount() <= 64 && stackInSlot.getCount() + resultItem.getCount() <= stackInSlot.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    this.canPurify = true;
                } else {
                    this.canPurify = stackInSlot.getCount() + resultItem.getCount() <= resultItem.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            this.canPurify = false;
        }
    }

    public int getPurifyingTime(ItemStack stack) {
        if (ItemStack.EMPTY == stack) return 0;
        return this.getRecipe(stack).getPurifyingTime();
    }

    public int getWaterRequired(ItemStack stack) {
        if (ItemStack.EMPTY == stack) return 0;
        return this.getRecipe(stack).getWaterRequired();
    }

    public final IItemHandlerModifiable getInventory() {
        return this.inventory;
    }

    @Nullable
    private PurificatorRecipe getRecipe(ItemStack stack) {
        if (stack == null) return null;

        Set<Recipe<?>> recipes = findRecipesByType(Electrona.PURIFYING, this.level);
        for (Recipe<?> iRecipe : recipes) {
            PurificatorRecipe recipe = (PurificatorRecipe) iRecipe;
            if (recipe.matches(new RecipeWrapper(this.inventory), this.level)) {
                canPurify(recipe);
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
        this.purifyingTime = compound.getInt("PurifyingTime");
        this.currentPurifyingTime = compound.getInt("CurrentPurifyingTime");
        this.waterRequired = compound.getInt("WaterRequired");
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundTag) compound.get("Inventory"));
        }
        if (compound.get("fluidTank") != null)
            CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(fluidTank, null, compound.get("fluidTank"));
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        compound.putInt("PurifyingTime", this.purifyingTime);
        compound.putInt("CurrentPurifyingTime", this.currentPurifyingTime);
        compound.putInt("WaterRequired", this.waterRequired);
        compound.put("Inventory", inventory.serializeNBT());
        compound.put("fluidTank", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(fluidTank, null));
        return compound;
    }

    private final FluidTank fluidTank = new FluidTank(10000, fs -> {
        return fs.getFluid() == Fluids.WATER;
    }) {
        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            setChanged();
            level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 2);
        }
    };

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> fluidTank).cast();
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
