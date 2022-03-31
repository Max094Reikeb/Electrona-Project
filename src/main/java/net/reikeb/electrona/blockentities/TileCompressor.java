package net.reikeb.electrona.blockentities;

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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.common.MinecraftForge;

import net.reikeb.electrona.blocks.Compressor;
import net.reikeb.electrona.containers.CompressorContainer;
import net.reikeb.electrona.events.local.CompressionEvent;
import net.reikeb.electrona.init.SoundsInit;
import net.reikeb.electrona.recipes.Recipes;

import static net.reikeb.electrona.init.TileEntityInit.TILE_COMPRESSOR;

public class TileCompressor extends AbstractTileEntity {

    public static final BlockEntityTicker<TileCompressor> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    public double electronicPower;
    public int compressingTime;
    public int currentCompressingTime;
    private int maxStorage;
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
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new CompressorContainer(id, this.getBlockPos(), playerInventory, player);
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
        ItemStack stackInSlot0 = this.inventory.getStackInSlot(0);
        ItemStack stackInSlot1 = this.inventory.getStackInSlot(1);

        double electronicPower = t.getTileData().getDouble("ElectronicPower");
        t.getTileData().putInt("MaxStorage", 5000);

        if ((world == null) || (world.isClientSide)) return;

        if ((electronicPower > 0) && (Recipes.getRecipe(this, stackInSlot0, stackInSlot1) != null)) {
            if (this.canCompress) {
                this.energyRequired = getEnergyRequired(stackInSlot0, stackInSlot1);
                this.compressingTime = getCompressingTime(stackInSlot0, stackInSlot1);
                ItemStack output = Recipes.getRecipe(this, stackInSlot0, stackInSlot1).getResultItem();
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
                t.getTileData().putDouble("ElectronicPower", electronicPower);
            } else {
                this.currentCompressingTime = 0;
            }
        } else {
            this.currentCompressingTime = 0;
        }
        world.setBlockAndUpdate(blockPos, state
                .setValue(Compressor.COMPRESSING, this.currentCompressingTime > 0));

        t.getTileData().putInt("CurrentCompressingTime", this.currentCompressingTime);
        t.getTileData().putInt("CompressingTime", this.compressingTime);

        t.setChanged();
        world.sendBlockUpdated(blockPos, state, state, 3);
    }

    public int getElectronicPower() {
        return (int) this.electronicPower;
    }

    public void setElectronicPower(double electronicPower) {
        this.electronicPower = electronicPower;
    }

    public int getCompressingTime() {
        return this.compressingTime;
    }

    public void setCompressingTime(int compressingTime) {
        this.compressingTime = compressingTime;
    }

    public int getCurrentCompressingTime() {
        return this.currentCompressingTime;
    }

    public void setCurrentCompressingTime(int currentCompressingTime) {
        this.currentCompressingTime = currentCompressingTime;
    }

    public void setCompress(boolean canCompress) {
        this.canCompress = canCompress;
    }

    public int getCompressingTime(ItemStack stack, ItemStack stack1) {
        if (ItemStack.EMPTY == stack || ItemStack.EMPTY == stack1) return 0;
        return Recipes.getRecipe(this, stack, stack1).getCompressingTime();
    }

    public int getEnergyRequired(ItemStack stack, ItemStack stack1) {
        if (ItemStack.EMPTY == stack || ItemStack.EMPTY == stack1) return 0;
        return Recipes.getRecipe(this, stack, stack1).getEnergyRequired();
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
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.putInt("CompressingTime", this.compressingTime);
        compound.putInt("CurrentCompressingTime", this.currentCompressingTime);
        compound.putInt("EnergyRequired", this.energyRequired);
        compound.put("Inventory", inventory.serializeNBT());
    }
}
