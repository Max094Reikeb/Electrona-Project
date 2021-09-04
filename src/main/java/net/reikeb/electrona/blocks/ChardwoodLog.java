package net.reikeb.electrona.blocks;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Collections;
import java.util.List;

public class ChardwoodLog extends RotatedPillarBlock {

    public ChardwoodLog() {
        super(Properties.of(Material.WOOD,
                        s -> s.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ?
                                MaterialColor.COLOR_PINK : MaterialColor.COLOR_RED)
                .sound(SoundType.WOOD)
                .strength(2.0F, 1.0F));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }
}
