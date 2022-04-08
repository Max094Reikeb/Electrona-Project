package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.containers.BiomassGeneratorContainer;
import net.reikeb.electrona.init.SoundsInit;
import net.reikeb.electrona.inventory.ItemHandler;
import net.reikeb.electrona.misc.Tags;
import net.reikeb.electrona.misc.vm.EnergyFunction;

import static net.reikeb.electrona.init.BlockEntityInit.BIOMASS_GENERATOR_BLOCK_ENTITY;

public class BiomassGeneratorBlockEntity extends AbstractBlockEntity implements AbstractEnergyBlockEntity {

    public static final BlockEntityTicker<BiomassGeneratorBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    public double electronicPower;
    public int maxStorage;
    private int wait;

    public BiomassGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(BIOMASS_GENERATOR_BLOCK_ENTITY.get(), pos, state, "biomass_generator", 1);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new BiomassGeneratorContainer(id, this.getBlockPos(), playerInventory, player);
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
        this.setMaxStorage(3000);
        if (world == null) return;

        // Handle slot
        if (Tags.BIOMASS.contains(this.inventory.getStackInSlot(0).getItem()) && this.electronicPower < 3000) {
            wait += 1;
            if (wait >= 20) {
                if (this.electronicPower <= 2990) {
                    this.electronicPower += 20;
                } else {
                    this.electronicPower = 300;
                }
                this.inventory.decrStackSize(0, 1);
                world.playSound(null, blockPos, SoundsInit.BIOMASS_GENERATOR_ACTIVE.get(),
                        SoundSource.BLOCKS, 0.6F, 1.0F);
                wait = 0;
            }
        } else {
            wait = 0;
        }

        // Transfer energy
        EnergyFunction.generatorTransferEnergy(world, blockPos, Direction.values(), this, 3, true);

        t.setChanged();
        world.sendBlockUpdated(blockPos, t.getBlockState(), t.getBlockState(), 3);
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

    public boolean getLogic() {
        return false;
    }

    public void setLogic(boolean logic) {
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        this.wait = compound.getInt("wait");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.putInt("wait", this.wait);
    }
}
