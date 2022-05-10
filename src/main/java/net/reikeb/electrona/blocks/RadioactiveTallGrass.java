package net.reikeb.electrona.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.reikeb.electrona.init.BlockInit;

import java.util.Collections;
import java.util.List;

public class RadioactiveTallGrass extends DoublePlantBlock {

    public RadioactiveTallGrass() {
        super(Properties.of(Material.PLANT)
                .noCollission()
                .sound(SoundType.GRASS)
                .strength(0f, 0f));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        if (state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) != DoubleBlockHalf.LOWER)
            return Collections.emptyList();
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }

    @Override
    public boolean mayPlaceOn(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        Block block = state.getBlock();
        return (block == BlockInit.RADIOACTIVE_DIRT.get());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = levelReader.getBlockState(blockpos);
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER)
            return blockstate.is(this) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER;
        else
            return this.mayPlaceOn(blockstate, levelReader, blockpos);
    }
}