package net.reikeb.electrona.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import net.reikeb.electrona.containers.PurificatorContainer;
import net.reikeb.electrona.events.local.PurificationEvent;
import net.reikeb.electrona.init.ContainerInit;
import net.reikeb.electrona.init.SoundsInit;
import net.reikeb.electrona.misc.vm.FluidFunction;
import net.reikeb.electrona.recipes.Recipes;
import net.reikeb.electrona.utils.FluidTankHandler;

import java.util.concurrent.atomic.AtomicInteger;

import static net.reikeb.electrona.init.TileEntityInit.TILE_PURIFICATOR;

public class TilePurificator extends AbstractTileEntity {

    public static final BlockEntityTicker<TilePurificator> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
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

        if (world.isClientSide) return;

        if ((waterLevel.get() > 0) && (Recipes.getRecipe(this, stackInSlot1) != null)) {
            if (this.canPurify) {
                this.waterRequired = getWaterRequired(stackInSlot1);
                this.purifyingTime = getPurifyingTime(stackInSlot1);
                ItemStack output = Recipes.getRecipe(this, stackInSlot1).getResultItem();
                double waterPerSecond = (double) this.waterRequired / this.purifyingTime;

                if (this.currentPurifyingTime < (this.purifyingTime * 20)) {
                    this.currentPurifyingTime += 1;
                    FluidFunction.drainWater(this, (int) (waterPerSecond * 0.05));
                    world.playSound(null, blockPos, SoundsInit.PURIFICATOR_PURIFICATION.get(),
                            SoundSource.BLOCKS, 0.6F, 1.0F);

                } else {
                    if (!MinecraftForge.EVENT_BUS.post(new PurificationEvent(world, blockPos, stackInSlot1, new ItemStack(output.copy().getItem(), Recipes.getRecipe(this, stackInSlot1).getCountOutput()), this.purifyingTime, this.waterRequired))) {
                        this.currentPurifyingTime = 0;
                        this.inventory.insertItem(2, new ItemStack(output.copy().getItem(), Recipes.getRecipe(this, stackInSlot1).getCountOutput()), false);
                        this.inventory.decrStackSize(1, Recipes.getRecipe(this, stackInSlot1).getCountInput());
                        world.playSound(null, blockPos, SoundEvents.BREWING_STAND_BREW,
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

        this.setChanged();
        world.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(), 3);
    }

    public void setPurify(boolean canPurify) {
        this.canPurify = canPurify;
    }

    public int getPurifyingTime(ItemStack stack) {
        if (ItemStack.EMPTY == stack) return 0;
        return Recipes.getRecipe(this, stack).getPurifyingTime();
    }

    public int getWaterRequired(ItemStack stack) {
        if (ItemStack.EMPTY == stack) return 0;
        return Recipes.getRecipe(this, stack).getWaterRequired();
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
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("PurifyingTime", this.purifyingTime);
        compound.putInt("CurrentPurifyingTime", this.currentPurifyingTime);
        compound.putInt("WaterRequired", this.waterRequired);
        compound.put("Inventory", inventory.serializeNBT());
        compound.put("fluidTank", fluidTank.serializeNBT());
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> fluidTank).cast();
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this.inventory));
    }
}
