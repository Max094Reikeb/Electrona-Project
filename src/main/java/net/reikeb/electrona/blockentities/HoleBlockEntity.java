package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.misc.vm.BlackHoleFunction;

import javax.annotation.Nullable;
import java.util.Objects;

import static net.reikeb.electrona.init.BlockEntityInit.HOLE_BLOCK_ENTITY;

public class HoleBlockEntity extends BlockEntity {

    public static final BlockEntityTicker<HoleBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    private int wait;

    public HoleBlockEntity(BlockPos pos, BlockState state) {
        super(HOLE_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
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
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("wait", this.wait);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        CompoundTag nbt = new CompoundTag();
        saveAdditional(nbt);
        return ClientboundBlockEntityDataPacket.create(this, blockEntity -> nbt);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(Objects.requireNonNull(pkt.getTag()));
    }
}
