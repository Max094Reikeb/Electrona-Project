package net.reikeb.electrona.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.blocks.Compressor;
import net.reikeb.electrona.containers.CompressorContainer;
import net.reikeb.electrona.events.local.CompressionEvent;
import net.reikeb.electrona.init.ContainerInit;
import net.reikeb.electrona.init.SoundsInit;
import net.reikeb.electrona.recipes.CompressorRecipe;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static net.reikeb.electrona.init.TileEntityInit.TILE_COMPRESSOR;

public class TileCompressor extends AbstractTileEntity {

    public double electronicPower;
    private int maxStorage;

    public int compressingTime;
    public int currentCompressingTime;
    private int energyRequired;

    private boolean canCompress;

    public TileCompressor(BlockPos pos, BlockState state) {
        super(TILE_COMPRESSOR.get(), pos, state, 3);
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
    public AbstractContainerMenu createMenu(final int windowID, final Inventory playerInv, final Player playerIn) {
        return new CompressorContainer(windowID, playerInv, this);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new CompressorContainer(ContainerInit.COMPRESSOR_CONTAINER.get(), id);
    }

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
    public void load(CompoundTag compound) {
        super.load(compound);
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
}
