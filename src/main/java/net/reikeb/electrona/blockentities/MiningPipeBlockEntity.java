package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.reikeb.electrona.init.BlockInit;

import static net.reikeb.electrona.init.BlockEntityInit.MINING_PIPE_BLOCK_ENTITY;

public class MiningPipeBlockEntity extends BlockEntity {

    public static final BlockEntityTicker<MiningPipeBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);

    public MiningPipeBlockEntity(BlockPos pos, BlockState state) {
        super(MINING_PIPE_BLOCK_ENTITY.get(), pos, state);
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
        BlockPos pos = blockPos.above();

        if (this.level == null) return;

        if ((BlockInit.MINING_PIPE.get() != this.level.getBlockState(pos).getBlock())
                && (BlockInit.MINING_MACHINE.get() != this.level.getBlockState(pos).getBlock())) {
            this.level.destroyBlock(this.getBlockPos(), false);
        }
    }
}
