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

public class TinOre extends Block {

    public TinOre() {
        super(Properties.of(Material.STONE)
                .sound(SoundType.STONE)
                .strength(3f, 6f)
                .lightLevel(s -> 0)
                .requiresCorrectToolForDrops());
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        int randomNum = 3 + (int) (Math.random() * ((6 - 3) + 1)); // Random number between 3 and 6
        return Collections.singletonList(new ItemStack(ItemInit.RAW_TIN.get(), randomNum));
    }
}
