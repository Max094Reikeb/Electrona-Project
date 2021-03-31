package net.reikeb.electrona.recipes.types;

import net.minecraft.item.crafting.IRecipeType;

import net.reikeb.electrona.recipes.CompressorRecipe;

public class RecipeTypeCompressor implements IRecipeType<CompressorRecipe> {

    @Override
    public String toString() {

        // Source: https://github.com/Minecraft-Forge-Tutorials/Custom-Json-Recipes/blob/master/src/main/java/net/darkhax/customrecipeexample/RecipeTypeClickBlock.java
        // Credits: Darkhax
        return "electrona:compressing";
    }
}
