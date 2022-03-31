package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.init.BlockInit;

import static net.reikeb.electrona.init.TileEntityInit.TILE_MINING_PIPE;

public class TileMiningPipe extends BlockEntity {

    public static final BlockEntityTicker<TileMiningPipe> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);

    public TileMiningPipe(BlockPos pos, BlockState state) {
        super(TILE_MINING_PIPE.get(), pos, state);
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
