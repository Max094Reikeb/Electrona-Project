package net.reikeb.electrona.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.common.ToolType;

import net.reikeb.electrona.misc.vm.RadioactivityFunction;

import java.util.*;

public class RadioactiveDirt extends Block {

    public RadioactiveDirt() {
        super(Properties.of(Material.DIRT)
                .sound(SoundType.GRASS)
                .strength(5f, 2f)
                .lightLevel(s -> 0)
                .harvestLevel(0)
                .harvestTool(ToolType.SHOVEL));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }

    @Override
    public void stepOn(World world, BlockPos pos, Entity entity) {
        RadioactivityFunction.stepOnRadioactiveBlock(entity);
    }
}
