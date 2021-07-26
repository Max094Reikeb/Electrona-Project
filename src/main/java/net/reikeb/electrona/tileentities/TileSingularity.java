package net.reikeb.electrona.tileentities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tileentity.*;

import net.reikeb.electrona.misc.vm.BlackHoleFunction;

import static net.reikeb.electrona.init.TileEntityInit.*;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;

public class TileSingularity extends BlockEntity implements TickableBlockEntity {

    private int wait;
    private int delay;

    public TileSingularity() {
        super(TILE_SINGULARITY.get());
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public void tick() {
        if (this.level == null) return; // Avoid NullPointerExceptions
        int wait = this.getTileData().getInt("wait");
        int delay = this.getTileData().getInt("delay");
        wait += 1;
        delay += 1;
        if (wait >= 100) {
            BlackHoleFunction.singularityParticles(this.level, this.getBlockPos());
            wait = 0;
        }
        if (delay >= 1000) {
            BlackHoleFunction.singularityDelay(this.level, this.getBlockPos());
            delay = 0;
        }
        this.getTileData().putInt("wait", wait);
        this.getTileData().putInt("delay", delay);
    }

    @Override
    public void load(BlockState blockState, CompoundTag compound) {
        super.load(blockState, compound);
        this.wait = compound.getInt("wait");
        this.delay = compound.getInt("delay");
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        compound.putInt("wait", this.wait);
        compound.putInt("delay", this.delay);
        return compound;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 0, this.getUpdateTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(this.getBlockState(), pkt.getTag());
    }
}
