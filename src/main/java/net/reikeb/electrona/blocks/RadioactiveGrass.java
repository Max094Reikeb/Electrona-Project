package net.reikeb.electrona.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;

import net.reikeb.electrona.init.BlockInit;

public class RadioactiveGrass extends FlowerBlock {

    public RadioactiveGrass() {
        super(Effects.SATURATION, 0, Properties.of(Material.PLANT)
                .noCollission()
                .sound(SoundType.GRASS)
                .strength(0f, 0f));
    }

    @Override
    public boolean mayPlaceOn(BlockState state, IBlockReader worldIn, BlockPos pos) {
        Block block = state.getBlock();
        return (block == BlockInit.RADIOACTIVE_DIRT.get());
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = world.getBlockState(blockpos);
        return this.mayPlaceOn(blockstate, world, blockpos);
    }
}
