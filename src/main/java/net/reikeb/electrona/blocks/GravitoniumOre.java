package net.reikeb.electrona.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;

import net.minecraftforge.common.ToolType;

import net.reikeb.electrona.init.ItemInit;

import java.util.*;

public class GravitoniumOre extends Block {

    public GravitoniumOre() {
        super(Properties.of(Material.STONE)
                .sound(SoundType.STONE)
                .strength(3f, 10f)
                .harvestLevel(2)
                .harvestTool(ToolType.PICKAXE)
                .requiresCorrectToolForDrops());
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Collections.singletonList(new ItemStack(ItemInit.GRAVITONIUM.get(), 1));
    }
}
