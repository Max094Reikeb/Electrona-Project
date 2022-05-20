package net.reikeb.electrona.recipes.types;

import net.minecraft.world.item.crafting.RecipeType;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.electrona.recipes.CompressorRecipe;

public class RecipeTypeCompressor implements RecipeType<CompressorRecipe> {

    @Override
    public String toString() {
        return Keys.COMPRESSING.toString();
    }
}
