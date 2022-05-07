package net.reikeb.electrona.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.reikeb.electrona.misc.vm.RadioactivityFunction;

import java.util.Collections;
import java.util.List;

public class RadioactiveDirt extends Block {

    public RadioactiveDirt() {
        super(Properties.of(Material.DIRT)
                .sound(SoundType.GRAVEL)
                .strength(5f, 2f)
                .lightLevel(s -> 0));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        RadioactivityFunction.stepOnRadioactiveBlock(entity);
    }
}
