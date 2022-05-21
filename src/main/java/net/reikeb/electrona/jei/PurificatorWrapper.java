package net.reikeb.electrona.jei;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.electrona.recipes.PurificatorRecipe;
import net.reikeb.maxilib.Couple;

public class PurificatorWrapper extends PurificatorRecipe {

    private final Couple<Couple<ItemStack, ItemStack>, ItemStack> wrapped;

    public PurificatorWrapper(Couple<Item, Item> inputs, Item output) {
        this(new Couple<>(new Couple<>(new ItemStack(inputs.part1(), 1), new ItemStack(inputs.part2(), 1)), new ItemStack(output, 1)));
    }

    public PurificatorWrapper(Couple<Couple<ItemStack, ItemStack>, ItemStack> wrapped) {
        super(Keys.PURIFYING, wrapped.part1().part2(), wrapped.part2(), false, 20, 20);
        this.wrapped = wrapped;
    }

    public Couple<ItemStack, ItemStack> getInput() {
        return this.wrapped.part1();
    }

    public ItemStack getOutput() {
        return this.wrapped.part2();
    }
}
