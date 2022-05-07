package net.reikeb.electrona.jei;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.reikeb.maxilib.RInputs;

public record PurificatorWrapper(RInputs input, Item output) {

    public RInputs getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return new ItemStack(output, 1);
    }
}
