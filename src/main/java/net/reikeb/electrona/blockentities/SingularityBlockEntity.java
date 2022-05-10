package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
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

import static net.reikeb.electrona.init.BlockEntityInit.SINGULARITY_BLOCK_ENTITY;

public class SingularityBlockEntity extends BlockEntity {

    public static final BlockEntityTicker<SingularityBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    private int wait;
    private int delay;

    public SingularityBlockEntity(BlockPos pos, BlockState state) {
        super(SINGULARITY_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    public <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState state, T t) {
        if (this.level == null) return;

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
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.wait = compound.getInt("wait");
        this.delay = compound.getInt("delay");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("wait", this.wait);
        compound.putInt("delay", this.delay);
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
