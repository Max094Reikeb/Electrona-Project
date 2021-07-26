package net.reikeb.electrona.blocks;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;

import net.minecraftforge.common.ToolType;

import java.util.Collections;
import java.util.List;

public class UraniumOre extends Block {

    public UraniumOre() {
        super(Properties.of(Material.STONE)
                .sound(SoundType.STONE)
                .strength(3f, 5f)
                .lightLevel(s -> 0)
                .harvestLevel(2)
                .harvestTool(ToolType.PICKAXE)
                .requiresCorrectToolForDrops()
                .hasPostProcess((bs, br, bp) -> true)
                .emissiveRendering((bs, br, bp) -> true));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }
}
