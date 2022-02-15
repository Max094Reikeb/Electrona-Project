package net.reikeb.electrona.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.PurificatorContainer;
import net.reikeb.electrona.events.local.PurificationEvent;
import net.reikeb.electrona.init.ContainerInit;
import net.reikeb.electrona.init.SoundsInit;
import net.reikeb.electrona.misc.vm.FluidFunction;
import net.reikeb.electrona.recipes.PurificatorRecipe;
import net.reikeb.electrona.utils.FluidTankHandler;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static net.reikeb.electrona.init.TileEntityInit.TILE_PURIFICATOR;

public class TilePurificator extends AbstractTileEntity {

    public static final BlockEntityTicker<TilePurificator> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    public int purifyingTime;
    public int currentPurifyingTime;
    private int waterRequired;

    private boolean canPurify;

    public TilePurificator(BlockPos pos, BlockState state) {
        super(TILE_PURIFICATOR.get(), pos, state, 3);
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
    public AbstractContainerMenu createMenu(final int windowID, final Inventory playerInv, final Player playerIn) {
        return new PurificatorContainer(windowID, playerInv, this);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new PurificatorContainer(ContainerInit.PURIFICATOR_CONTAINER.get(), id);
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
        if (world == null) return;
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

        if (!world.isClientSide) {
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

        this.setChanged();
        world.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
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
    public void load(CompoundTag compound) {
        super.load(compound);
        this.purifyingTime = compound.getInt("PurifyingTime");
        this.currentPurifyingTime = compound.getInt("CurrentPurifyingTime");
        this.waterRequired = compound.getInt("WaterRequired");
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundTag) compound.get("Inventory"));
        }
        if (compound.get("fluidTank") != null) {
            fluidTank.readFromNBT((CompoundTag) compound.get("fluidTank"));
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        compound.putInt("PurifyingTime", this.purifyingTime);
        compound.putInt("CurrentPurifyingTime", this.currentPurifyingTime);
        compound.putInt("WaterRequired", this.waterRequired);
        compound.put("Inventory", inventory.serializeNBT());
        compound.put("fluidTank", fluidTank.serializeNBT());
        return compound;
    }

    private final FluidTankHandler fluidTank = new FluidTankHandler(10000, fs -> {
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
}
