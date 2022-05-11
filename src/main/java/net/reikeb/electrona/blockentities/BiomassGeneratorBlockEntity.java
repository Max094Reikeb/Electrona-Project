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
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.BiomassGeneratorContainer;
import net.reikeb.electrona.init.SoundsInit;
import net.reikeb.electrona.misc.Tags;
import net.reikeb.electrona.misc.vm.EnergyFunction;
import net.reikeb.maxilib.abs.AbstractEnergyBlockEntity;
import net.reikeb.maxilib.intface.IEnergy;

import static net.reikeb.electrona.init.BlockEntityInit.BIOMASS_GENERATOR_BLOCK_ENTITY;

public class BiomassGeneratorBlockEntity extends AbstractEnergyBlockEntity {

    public static final BlockEntityTicker<BiomassGeneratorBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    private int wait;

    public BiomassGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(BIOMASS_GENERATOR_BLOCK_ENTITY.get(), pos, state, "biomass_generator", Electrona.MODID, 1);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new BiomassGeneratorContainer(id, this.getBlockPos(), playerInventory, player);
    }

    public <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState state, T t) {
        this.setMaxStorage(3000);
        if (level == null) return;

        // Handle slot
        if (this.inventory.getStackInSlot(0).is(Tags.BIOMASS) && this.getElectronicPower() < 3000) {
            wait += 1;
            if (wait >= 20) {
                if (this.getElectronicPower() <= 2990) {
                    IEnergy.fillEnergy(this, 20);
                } else {
                    IEnergy.setEnergy(this, 300);
                }
                this.inventory.decrStackSize(0, 1);
                level.playSound(null, blockPos, SoundsInit.BIOMASS_GENERATOR_ACTIVE.get(),
                        SoundSource.BLOCKS, 0.6F, 1.0F);
                wait = 0;
            }
        } else {
            wait = 0;
        }

        // Transfer energy
        EnergyFunction.generatorTransferEnergy(level, blockPos, Direction.values(), this, 3, true);

        t.setChanged();
        level.sendBlockUpdated(blockPos, t.getBlockState(), t.getBlockState(), 3);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.wait = compound.getInt("wait");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("wait", this.wait);
    }
}
