package net.reikeb.electrona.jei;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.electrona.recipes.PurificatorRecipe;
import net.reikeb.maxilib.Couple;

public class PurificatorWrapper extends PurificatorRecipe {

    private final Couple<ItemStack, ItemStack> inputs;
    private final ItemStack output;

    public PurificatorWrapper(Couple<Item, Item> inputs, Item output) {
        this(new Couple<>(new ItemStack(inputs.part1(), 1), new ItemStack(inputs.part2(), 1)), new ItemStack(output, 1));
    }

    public PurificatorWrapper(Couple<ItemStack, ItemStack> inputs, ItemStack output) {
        super(Keys.PURIFYING, inputs.part2(), output, false, 20, 20);
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
