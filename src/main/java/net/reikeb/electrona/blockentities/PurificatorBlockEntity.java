package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import net.reikeb.electrona.containers.PurificatorContainer;
import net.reikeb.electrona.events.local.PurificationEvent;
import net.reikeb.electrona.init.SoundsInit;
import net.reikeb.electrona.misc.vm.FluidFunction;
import net.reikeb.electrona.recipes.Recipes;

import java.util.concurrent.atomic.AtomicInteger;

import static net.reikeb.electrona.init.BlockEntityInit.PURIFICATOR_BLOCK_ENTITY;

public class PurificatorBlockEntity extends AbstractFluidBlockEntity {

    public static final BlockEntityTicker<PurificatorBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    public int purifyingTime;
    public int currentPurifyingTime;
    private int waterRequired;
    private boolean canPurify;

    public PurificatorBlockEntity(BlockPos pos, BlockState state) {
        super(PURIFICATOR_BLOCK_ENTITY.get(), pos, state, "purificator", 3, 10000);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new PurificatorContainer(id, this.getBlockPos(), playerInventory, player);
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

        this.setChanged();
        world.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(), 3);
    }

    public int getWaterLevel() {
        return FluidFunction.getFluidAmount(this).get();
    }

    public void setWaterLevel(int amount) {
        AtomicInteger waterLevel = FluidFunction.getFluidAmount(this);
        FluidFunction.drainWater(this, waterLevel.get());
        FluidFunction.fillWater(this, amount);
    }

    public int getPurifyingTime() {
        return this.purifyingTime;
    }

    public void setPurifyingTime(int purifyingTime) {
        this.purifyingTime = purifyingTime;
    }

    public int getCurrentPurifyingTime() {
        return this.currentPurifyingTime;
    }

    public void setCurrentPurifyingTime(int currentPurifyingTime) {
        this.currentPurifyingTime = currentPurifyingTime;
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
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("PurifyingTime", this.purifyingTime);
        compound.putInt("CurrentPurifyingTime", this.currentPurifyingTime);
        compound.putInt("WaterRequired", this.waterRequired);
    }
}
