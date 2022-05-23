package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.SprayerContainer;
import net.reikeb.electrona.misc.vm.SprayerFunction;
import net.reikeb.maxilib.abs.AbstractEnergyBlockEntity;

import static net.reikeb.electrona.init.BlockEntityInit.SPRAYER_BLOCK_ENTITY;

public class SprayerBlockEntity extends AbstractEnergyBlockEntity {

    public static final BlockEntityTicker<SprayerBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    private int radius;
    private int wait;

    public SprayerBlockEntity(BlockPos pos, BlockState state) {
        super(SPRAYER_BLOCK_ENTITY.get(), pos, state, "sprayer", Electrona.MODID, 4);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new SprayerContainer(id, this.getBlockPos(), playerInventory, player);
    }

    public <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState state, T t) {
        this.setMaxEnergy(3000);

        if (level == null) return;
        SprayerFunction.mainSprayer(this);
        wait++;
        if (wait >= 30) {
            wait = 0;
            SprayerFunction.sprayerParticles(this);
        }

        this.setChanged();
        level.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(), 3);
    }

    public int getRadius() {
        return this.radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.radius = compound.getInt("radius");
        this.wait = compound.getInt("wait");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("radius", this.radius);
        compound.putInt("wait", this.wait);
    }
}
