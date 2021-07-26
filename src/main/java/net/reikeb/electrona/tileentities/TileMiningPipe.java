package net.reikeb.electrona.tileentities;

import net.minecraft.tileentity.*;
import net.minecraft.core.BlockPos;

import net.reikeb.electrona.init.BlockInit;

import static net.reikeb.electrona.init.TileEntityInit.*;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;

public class TileMiningPipe extends BlockEntity implements TickableBlockEntity {

    public TileMiningPipe() {
        super(TILE_MINING_PIPE.get());
    }

    @Override
    public void tick() {
        BlockPos pos = new BlockPos(this.getBlockPos().getX(), (this.getBlockPos().getY() + 1), this.getBlockPos().getZ());

        if (this.level == null) return;

        if ((BlockInit.MINING_PIPE.get() != this.level.getBlockState(pos).getBlock())
                && (BlockInit.MINING_MACHINE.get() != this.level.getBlockState(pos).getBlock())) {
            this.level.destroyBlock(this.getBlockPos(), false);
        }
    }
}
