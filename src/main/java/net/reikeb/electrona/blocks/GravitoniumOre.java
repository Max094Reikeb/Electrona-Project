package net.reikeb.electrona.blocks;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;

import net.minecraftforge.common.ToolType;

import net.reikeb.electrona.init.ItemInit;

import java.util.Collections;
import java.util.List;

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
