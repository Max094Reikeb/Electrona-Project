package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
import net.reikeb.electrona.inventory.ItemHandler;
import net.reikeb.electrona.recipes.Recipes;

import static net.reikeb.electrona.init.BlockEntityInit.COMPRESSOR_BLOCK_ENTITY;

public class CompressorBlockEntity extends AbstractBlockEntity implements AbstractEnergyBlockEntity {

    public static final BlockEntityTicker<CompressorBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    public int compressingTime;
    public int currentCompressingTime;
    public double electronicPower;
    public int maxStorage;
    private int energyRequired;
    private boolean canCompress;

    public CompressorBlockEntity(BlockPos pos, BlockState state) {
        super(COMPRESSOR_BLOCK_ENTITY.get(), pos, state, "compressor", 3);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new CompressorContainer(id, this.getBlockPos(), playerInventory, player);
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
        ItemStack stackInSlot0 = this.inventory.getStackInSlot(0);
        ItemStack stackInSlot1 = this.inventory.getStackInSlot(1);

        this.setMaxStorage(5000);

        if ((world == null) || (world.isClientSide)) return;

        if ((this.electronicPower > 0) && (Recipes.getRecipe(this, stackInSlot0, stackInSlot1) != null)) {
            if (this.canCompress) {
                this.energyRequired = getEnergyRequired(stackInSlot0, stackInSlot1);
                this.compressingTime = getCompressingTime(stackInSlot0, stackInSlot1);
                ItemStack output = Recipes.getRecipe(this, stackInSlot0, stackInSlot1).getResultItem();
                double energyPerSecond = (double) this.energyRequired / this.compressingTime;

                if (this.currentCompressingTime < (this.compressingTime * 20)) {
                    this.currentCompressingTime += 1;
                    this.electronicPower = this.electronicPower - (energyPerSecond * 0.05);

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
            } else {
                this.currentCompressingTime = 0;
            }
        } else {
            this.currentCompressingTime = 0;
        }
        world.setBlockAndUpdate(blockPos, state
                .setValue(Compressor.COMPRESSING, this.currentCompressingTime > 0));

        t.setChanged();
        world.sendBlockUpdated(blockPos, state, state, 3);
    }

    public ItemHandler getItemInventory() {
        return this.inventory;
    }

    public int getElectronicPowerTimesHundred() {
        return (int) (this.electronicPower * 100);
    }

    public void setElectronicPowerTimesHundred(int electronicPowerTimesHundred) {
        this.electronicPower = electronicPowerTimesHundred / 100.0;
    }

    public double getElectronicPower() {
        return this.electronicPower;
    }

    public void setElectronicPower(double electronicPower) {
        this.electronicPower = electronicPower;
    }

    public int getMaxStorage() {
        return this.maxStorage;
    }

    public void setMaxStorage(int maxStorage) {
        this.maxStorage = maxStorage;
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

    public boolean getLogic() {
        return false;
    }

    public void setLogic(boolean logic) {
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
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.putInt("CompressingTime", this.compressingTime);
        compound.putInt("CurrentCompressingTime", this.currentCompressingTime);
        compound.putInt("EnergyRequired", this.energyRequired);
    }
}
