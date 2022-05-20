package net.reikeb.electrona.recipes.types;

import net.minecraft.world.item.crafting.RecipeType;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.electrona.recipes.PurificatorRecipe;

public class RecipeTypePurificator implements RecipeType<PurificatorRecipe> {

    @Override
    public String toString() {
        return Keys.PURIFYING.toString();
    }
}
