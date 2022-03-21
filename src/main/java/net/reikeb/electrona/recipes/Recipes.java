package net.reikeb.electrona.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import net.minecraftforge.items.wrapper.RecipeWrapper;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.tileentities.TileCompressor;
import net.reikeb.electrona.tileentities.TilePurificator;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class Recipes {

    private static Set<Recipe<?>> findRecipesByType(RecipeType<?> typeIn, Level world) {
        return world != null ? world.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Collections.emptySet();
    }

    @Nullable
    public static CompressorRecipe getRecipe(TileCompressor tileCompressor, ItemStack stack, ItemStack stack2) {
        if (stack == null || stack2 == null) return null;

        Set<Recipe<?>> recipes = findRecipesByType(Electrona.COMPRESSING, tileCompressor.getLevel());
        for (Recipe<?> iRecipe : recipes) {
            CompressorRecipe recipe = (CompressorRecipe) iRecipe;
            if (recipe.matches(new RecipeWrapper(tileCompressor.inventory), tileCompressor.getLevel())) {
                canCompress(tileCompressor, recipe);
                return recipe;
            }
        }
        return null;
    }

    @Nullable
    public static PurificatorRecipe getRecipe(TilePurificator tilePurificator, ItemStack stack) {
        if (stack == null) return null;

        Set<Recipe<?>> recipes = findRecipesByType(Electrona.PURIFYING, tilePurificator.getLevel());
        for (Recipe<?> iRecipe : recipes) {
            PurificatorRecipe recipe = (PurificatorRecipe) iRecipe;
            if (recipe.matches(new RecipeWrapper(tilePurificator.inventory), tilePurificator.getLevel())) {
                canPurify(tilePurificator, recipe);
                return recipe;
            }
        }
        return null;
    }

    private static void canCompress(TileCompressor tileCompressor, @Nullable Recipe<?> recipe) {
        if (!tileCompressor.inventory.getStackInSlot(0).isEmpty() && recipe != null) {
            ItemStack resultItem = recipe.getResultItem();
            if (resultItem.isEmpty()) {
                tileCompressor.setCompress(false);
            } else {
                ItemStack stackInSlot = tileCompressor.inventory.getStackInSlot(2);
                if (stackInSlot.isEmpty()) {
                    tileCompressor.setCompress(true);
                } else if (!stackInSlot.sameItem(resultItem)) {
                    tileCompressor.setCompress(false);
                } else if (stackInSlot.getCount() + resultItem.getCount() <= 64 && stackInSlot.getCount() + resultItem.getCount() <= stackInSlot.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    tileCompressor.setCompress(true);
                } else {
                    tileCompressor.setCompress(stackInSlot.getCount() + resultItem.getCount() <= resultItem.getMaxStackSize()); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            tileCompressor.setCompress(false);
        }
    }

    private static void canPurify(TilePurificator tilePurificator, @Nullable Recipe<?> recipe) {
        if (!tilePurificator.inventory.getStackInSlot(1).isEmpty() && recipe != null) {
            ItemStack resultItem = recipe.getResultItem();
            if (resultItem.isEmpty()) {
                tilePurificator.setPurify(false);
            } else {
                ItemStack stackInSlot = tilePurificator.inventory.getStackInSlot(2);
                if (stackInSlot.isEmpty()) {
                    tilePurificator.setPurify(true);
                } else if (!stackInSlot.sameItem(resultItem)) {
                    tilePurificator.setPurify(false);
                } else if (stackInSlot.getCount() + resultItem.getCount() <= 64 && stackInSlot.getCount() + resultItem.getCount() <= stackInSlot.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    tilePurificator.setPurify(true);
                } else {
                    tilePurificator.setPurify(stackInSlot.getCount() + resultItem.getCount() <= resultItem.getMaxStackSize()); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            tilePurificator.setPurify(false);
        }
    }
}
