package net.reikeb.electrona.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;

import net.minecraftforge.common.ToolType;

import java.util.*;

public class MachineCasing extends Block {

    public MachineCasing() {
        super(Properties.of(Material.METAL)
                .sound(SoundType.METAL)
                .strength(5f, 6f)
                .lightLevel(s -> 0)
                .harvestLevel(1)
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
