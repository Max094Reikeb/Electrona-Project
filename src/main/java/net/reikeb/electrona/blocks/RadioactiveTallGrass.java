package net.reikeb.electrona.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.properties.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;

import net.reikeb.electrona.init.BlockInit;

import java.util.*;

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
    public boolean mayPlaceOn(BlockState state, IBlockReader world, BlockPos pos) {
        Block block = state.getBlock();
        return (block == BlockInit.RADIOACTIVE_DIRT.get());
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = world.getBlockState(blockpos);
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER)
            return blockstate.is(this) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER;
        else
            return this.mayPlaceOn(blockstate, world, blockpos);
    }
}