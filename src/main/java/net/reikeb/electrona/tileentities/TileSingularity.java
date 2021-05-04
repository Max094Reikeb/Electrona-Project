package net.reikeb.electrona.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.*;

import net.reikeb.electrona.misc.vm.BlackHoleFunction;

import static net.reikeb.electrona.init.TileEntityInit.*;

public class TileSingularity extends TileEntity implements ITickableTileEntity {

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
    public void load(BlockState blockState, CompoundNBT compound) {
        super.load(blockState, compound);
        this.wait = compound.getInt("wait");
        this.delay = compound.getInt("delay");
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putInt("wait", this.wait);
        compound.putInt("delay", this.delay);
        return compound;
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
