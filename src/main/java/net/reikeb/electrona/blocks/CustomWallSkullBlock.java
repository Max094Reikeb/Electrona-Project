package net.reikeb.electrona.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.reikeb.electrona.blockentities.CustomSkullBlockEntity;

public class CustomWallSkullBlock extends WallSkullBlock {

    public CustomWallSkullBlock(SkullBlock.Type type, Properties props) {
        super(type, props);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CustomSkullBlockEntity(pos, state);
    }
}
