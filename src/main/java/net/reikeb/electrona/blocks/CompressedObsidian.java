package net.reikeb.electrona.blocks;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.storage.loot.LootContext;

import net.minecraftforge.common.ToolType;

import java.util.*;

public class CompressedObsidian extends Block {

    public CompressedObsidian() {
        super(Properties.of(Material.DECORATION)
                .sound(SoundType.STONE)
                .strength(50f, 2400f)
                .lightLevel(s -> 0)
                .harvestLevel(3)
                .harvestTool(ToolType.PICKAXE)
                .requiresCorrectToolForDrops());
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.IGNORE;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }
}
