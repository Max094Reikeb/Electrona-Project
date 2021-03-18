package net.reikeb.electrona.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.*;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.blocks.Compressor;
import net.reikeb.electrona.containers.CompressorContainer;
import net.reikeb.electrona.recipes.CompressorRecipe;
import net.reikeb.electrona.utils.ItemHandler;

import static net.reikeb.electrona.init.TileEntityInit.*;

import javax.annotation.Nullable;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class TileCompressor extends TileEntity implements ITickableTileEntity {

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
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public void tick() {
        World world = this.level;
        BlockPos blockPos = this.getBlockPos();
        ItemStack stackInSlot0 = this.inventory.getStackInSlot(0);
        ItemStack stackInSlot1 = this.inventory.getStackInSlot(1);

        this.getTileData().putInt("MaxStorage", 5000);

        if ((world != null) && (!world.isClientSide)) {
            if ((this.electronicPower > 0) && (this.getRecipe(stackInSlot0, stackInSlot1) != null)) {
                if (this.canCompress) {
                    this.energyRequired = getEnergyRequired(stackInSlot0, stackInSlot1);
                    this.compressingTime = getCompressingTime(stackInSlot0, stackInSlot1);
                    ItemStack output = this.getRecipe(stackInSlot0, stackInSlot1).getResultItem();
                    double energyPerSecond = (double) this.energyRequired / this.compressingTime;

                    if (this.currentCompressingTime < (this.compressingTime * 20)) {
                        world.setBlockAndUpdate(blockPos,
                                this.getBlockState().setValue(Compressor.COMPRESSING, true));
                        this.currentCompressingTime += 1;
                        this.electronicPower = this.electronicPower - (energyPerSecond * 0.05);
                    } else {
                        world.setBlockAndUpdate(blockPos,
                                this.getBlockState().setValue(Compressor.COMPRESSING, false));
                        this.currentCompressingTime = 0;
                        this.inventory.insertItem(2, output.copy(), false);
                        this.inventory.decrStackSize(0, 1);
                        this.inventory.decrStackSize(1, 1);
                    }
                } else {
                    this.currentCompressingTime = 0;
                }
            } else {
                this.currentCompressingTime = 0;
            }
        }

        if (world != null) {
            this.setChanged();
            world.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(),
                    Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    protected void canCompress(@Nullable IRecipe<?> recipe) {
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

        Set<IRecipe<?>> recipes = findRecipesByType(Electrona.COMPRESSING, this.level);
        for (IRecipe<?> iRecipe : recipes) {
            CompressorRecipe recipe = (CompressorRecipe) iRecipe;
            if (recipe.matches(new RecipeWrapper(this.inventory), this.level)) {
                canCompress(recipe);
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
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        this.compressingTime = compound.getInt("CompressingTime");
        this.currentCompressingTime = compound.getInt("CurrentCompressingTime");
        this.energyRequired = compound.getInt("EnergyRequired");
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.putInt("CompressingTime", this.compressingTime);
        compound.putInt("CurrentCompressingTime", this.currentCompressingTime);
        compound.putInt("EnergyRequired", this.energyRequired);
        return compound;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
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
}
