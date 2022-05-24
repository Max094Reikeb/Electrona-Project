package net.reikeb.electrona.jei;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.electrona.recipes.CompressorRecipe;
import net.reikeb.maxilib.Couple;

public class CompressingWrapper extends CompressorRecipe {

    private final Couple<Couple<ItemStack, ItemStack>, ItemStack> wrapped;

    public CompressingWrapper(Couple<Block, Block> inputs, Block output) {
        this(new Couple<>(inputs.part1().asItem(), inputs.part2().asItem()), output.asItem());
    }

    public CompressingWrapper(Couple<Item, Item> inputs, Item output) {
        this(new Couple<>(new Couple<>(new ItemStack(inputs.part1(), 1), new ItemStack(inputs.part2(), 1)), new ItemStack(output, 1)));
    }

    public CompressingWrapper(Couple<Couple<ItemStack, ItemStack>, ItemStack> wrapped) {
        super(Keys.COMPRESSING, new Couple<>(Ingredient.of(wrapped.part1().part1()), Ingredient.of(wrapped.part1().part2())), wrapped.part2(), 20, 20);
        this.wrapped = wrapped;
    }

    public Couple<ItemStack, ItemStack> getInput() {
        return this.wrapped.part1();
    }

    public ItemStack getOutput() {
        return this.wrapped.part2();
    }
}
