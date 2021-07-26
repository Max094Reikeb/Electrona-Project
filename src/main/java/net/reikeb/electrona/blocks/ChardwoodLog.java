package net.reikeb.electrona.blocks;

import net.minecraft.core.Direction;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.storage.loot.LootContext;

import net.minecraftforge.common.ToolType;

import java.util.*;

public class ChardwoodLog extends RotatedPillarBlock {

    public ChardwoodLog() {
        super(Properties.of(Material.WOOD,
                s -> s.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ?
                        MaterialColor.COLOR_PINK : MaterialColor.COLOR_RED)
                .sound(SoundType.WOOD)
                .strength(2.0F, 1.0F)
                .harvestLevel(0)
                .harvestTool(ToolType.AXE));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }
}
