package net.reikeb.electrona.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.*;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.*;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.PurificatorContainer;
import net.reikeb.electrona.init.*;
import net.reikeb.electrona.misc.vm.FluidFunction;
import net.reikeb.electrona.recipes.PurificatorRecipe;
import net.reikeb.electrona.utils.*;

import static net.reikeb.electrona.init.TileEntityInit.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TilePurificator extends LockableLootTileEntity implements ITickableTileEntity {

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
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("electrona.purificator_gui.name");
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new StringTextComponent("purificator");
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
        return new PurificatorContainer(windowID, playerInv, this);
    }

    @Override
    public Container createMenu(int id, PlayerInventory player) {
        return new PurificatorContainer(ContainerInit.COMPRESSOR_CONTAINER.get(), id);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public void tick() {
        World world = this.level;
        BlockPos blockPos = this.getBlockPos();
        ItemStack stackInSlot1 = this.inventory.getStackInSlot(1);

        AtomicInteger waterLevel = new AtomicInteger();
        this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).ifPresent(cap -> waterLevel.set(cap.getFluidInTank(1).getAmount()));
        AtomicInteger tankCapacity = new AtomicInteger();
        this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).ifPresent(cap -> tankCapacity.set(cap.getTankCapacity(1)));

        // Input slot - Handling slots
        if ((this.inventory.getStackInSlot(0).getItem() == Items.WATER_BUCKET)
                && (waterLevel.get() < (tankCapacity.get() - 1000))) {
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
                        ElectronaUtils.playSound(world, blockPos, SoundsInit.PURIFICATOR_PURIFICATION.get());

                    } else {
                        this.currentPurifyingTime = 0;
                        this.inventory.insertItem(2, new ItemStack(output.copy().getItem(), this.getRecipe(stackInSlot1).getCountOutput()), false);
                        this.inventory.decrStackSize(1, this.getRecipe(stackInSlot1).getCountInput());
                        ElectronaUtils.playSound(world, blockPos, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.brewing_stand.brew")));
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

    protected void canPurify(@Nullable IRecipe<?> recipe) {
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

        Set<IRecipe<?>> recipes = findRecipesByType(Electrona.PURIFYING, this.level);
        for (IRecipe<?> iRecipe : recipes) {
            PurificatorRecipe recipe = (PurificatorRecipe) iRecipe;
            if (recipe.matches(new RecipeWrapper(this.inventory), this.level)) {
                canPurify(recipe);
                return recipe;
            }
        }
        return null;
    }

    public static Set<IRecipe<?>> findRecipesByType(IRecipeType<?> typeIn, World world) {
        return world != null ? world.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Collections.emptySet();
    }

    @Override
    public void load(BlockState blockState, CompoundNBT compound) {
        super.load(blockState, compound);
        this.purifyingTime = compound.getInt("PurifyingTime");
        this.currentPurifyingTime = compound.getInt("CurrentPurifyingTime");
        this.waterRequired = compound.getInt("WaterRequired");
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundNBT) compound.get("Inventory"));
        }
        if (compound.get("fluidTank") != null)
            CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(fluidTank, null, compound.get("fluidTank"));
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
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

    public void dropItems(World world, BlockPos pos) {
        for (int i = 0; i < 3; i++)
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
        return 3;
    }
}
