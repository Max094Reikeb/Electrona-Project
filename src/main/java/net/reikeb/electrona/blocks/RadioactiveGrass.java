package net.reikeb.electrona.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import net.reikeb.electrona.init.BlockInit;

public class RadioactiveGrass extends FlowerBlock {

    public RadioactiveGrass() {
        super(MobEffects.SATURATION, 0, Properties.of(Material.PLANT)
                .noCollission()
                .sound(SoundType.GRASS)
                .strength(0f, 0f));
    }

    @Override
    public boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
        Block block = state.getBlock();
        return (block == BlockInit.RADIOACTIVE_DIRT.get());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = world.getBlockState(blockpos);
        return this.mayPlaceOn(blockstate, world, blockpos);
    }
}
