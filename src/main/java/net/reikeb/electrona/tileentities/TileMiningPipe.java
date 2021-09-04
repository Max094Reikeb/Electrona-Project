package net.reikeb.electrona.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.init.BlockInit;

import static net.reikeb.electrona.init.TileEntityInit.TILE_MINING_PIPE;

public class TileMiningPipe extends BlockEntity {

    public TileMiningPipe(BlockPos pos, BlockState state) {
        super(TILE_MINING_PIPE.get(), pos, state);
    }

    public void tick() {
        BlockPos pos = new BlockPos(this.getBlockPos().getX(), (this.getBlockPos().getY() + 1), this.getBlockPos().getZ());

        if (this.level == null) return;

        if ((BlockInit.MINING_PIPE.get() != this.level.getBlockState(pos).getBlock())
                && (BlockInit.MINING_MACHINE.get() != this.level.getBlockState(pos).getBlock())) {
            this.level.destroyBlock(this.getBlockPos(), false);
        }
    }
}
