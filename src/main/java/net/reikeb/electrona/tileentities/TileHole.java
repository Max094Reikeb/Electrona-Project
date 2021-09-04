package net.reikeb.electrona.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.misc.vm.BlackHoleFunction;

import static net.reikeb.electrona.init.TileEntityInit.TILE_HOLE;

public class TileHole extends BlockEntity {

    private int wait;

    public TileHole(BlockPos pos, BlockState state) {
        super(TILE_HOLE.get(), pos, state);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    public void tick() {
        if (this.level == null) return; // Avoid NullPointerExceptions
        int wait = this.getTileData().getInt("wait");
        wait += 1;
        if (wait >= 100) {
            BlackHoleFunction.HoleProcedure(this.level, this.getBlockPos(), Direction.values());
            wait = 0;
        }
        this.getTileData().putInt("wait", wait);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.wait = compound.getInt("wait");
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        compound.putInt("wait", this.wait);
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
        this.load(pkt.getTag());
    }
}
