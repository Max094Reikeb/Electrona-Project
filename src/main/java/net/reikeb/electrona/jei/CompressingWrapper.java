package net.reikeb.electrona.jei;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.electrona.recipes.CompressorRecipe;
import net.reikeb.maxilib.Couple;

public class CompressingWrapper extends CompressorRecipe {

    private final Couple<ItemStack, ItemStack> inputs;
    private final ItemStack output;

    public CompressingWrapper(Couple<Block, Block> inputs, Block output) {
        this(new Couple<>(inputs.part1().asItem(), inputs.part2().asItem()), output.asItem());
    }

    public CompressingWrapper(Couple<Item, Item> inputs, Item output) {
        this(new Couple<>(new ItemStack(inputs.part1(), 1), new ItemStack(inputs.part2(), 1)), new ItemStack(output, 1));
    }

    public CompressingWrapper(Couple<ItemStack, ItemStack> inputs, ItemStack output) {
        super(Keys.COMPRESSING, Ingredient.of(inputs.part1()), Ingredient.of(inputs.part2()), output, 20, 20);
        this.inputs = inputs;
        this.output = output;
    }

    public Couple<ItemStack, ItemStack> getInput() {
        return this.inputs;
    }

    public ItemStack getOutput() {
        return this.output;
    }
}
