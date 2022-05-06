package net.reikeb.electrona.blocks;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;

import net.reikeb.electrona.init.ItemInit;

import java.util.Collections;
import java.util.List;

public class DeepslateTinOre extends Block {

    public DeepslateTinOre() {
        super(Properties.of(Material.STONE)
                .sound(SoundType.DEEPSLATE)
                .strength(4.5f, 6f)
                .lightLevel(s -> 0)
                .requiresCorrectToolForDrops());
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Collections.singletonList(new ItemStack(ItemInit.RAW_TIN.get(), 1));
    }
}
