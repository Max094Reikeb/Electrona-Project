package net.reikeb.electrona.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.blockentities.CompressorBlockEntity;
import net.reikeb.electrona.blockentities.PurificatorBlockEntity;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class Recipes {

    private static Set<Recipe<?>> findRecipesByType(RecipeType<?> typeIn, Level level) {
        return level != null ? level.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Collections.emptySet();
    }

    @Nullable
    public static CompressorRecipe getRecipe(CompressorBlockEntity compressorBlockEntity, ItemStack stack, ItemStack stack2) {
        if (stack == null || stack2 == null) return null;

        Set<Recipe<?>> recipes = findRecipesByType(Electrona.COMPRESSING, compressorBlockEntity.getLevel());
        for (Recipe<?> iRecipe : recipes) {
            CompressorRecipe recipe = (CompressorRecipe) iRecipe;
            if (recipe.matches(new RecipeWrapper(compressorBlockEntity.inventory), compressorBlockEntity.getLevel())) {
                canCompress(compressorBlockEntity, recipe);
                return recipe;
            }
        }
        return null;
    }

    @Nullable
    public static PurificatorRecipe getRecipe(PurificatorBlockEntity purificatorBlockEntity, ItemStack stack) {
        if (stack == null) return null;

        Set<Recipe<?>> recipes = findRecipesByType(Electrona.PURIFYING, purificatorBlockEntity.getLevel());
        for (Recipe<?> iRecipe : recipes) {
            PurificatorRecipe recipe = (PurificatorRecipe) iRecipe;
            if (recipe.matches(new RecipeWrapper(purificatorBlockEntity.inventory), purificatorBlockEntity.getLevel())) {
                canPurify(purificatorBlockEntity, recipe);
                return recipe;
            }
        }
        return null;
    }

    private static void canCompress(CompressorBlockEntity compressorBlockEntity, @Nullable Recipe<?> recipe) {
        if (!compressorBlockEntity.inventory.getStackInSlot(0).isEmpty() && recipe != null) {
            ItemStack resultItem = recipe.getResultItem();
            if (resultItem.isEmpty()) {
                compressorBlockEntity.setCompress(false);
            } else {
                ItemStack stackInSlot = compressorBlockEntity.inventory.getStackInSlot(2);
                if (stackInSlot.isEmpty()) {
                    compressorBlockEntity.setCompress(true);
                } else if (!stackInSlot.sameItem(resultItem)) {
                    compressorBlockEntity.setCompress(false);
                } else if (stackInSlot.getCount() + resultItem.getCount() <= 64 && stackInSlot.getCount() + resultItem.getCount() <= stackInSlot.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    compressorBlockEntity.setCompress(true);
                } else {
                    compressorBlockEntity.setCompress(stackInSlot.getCount() + resultItem.getCount() <= resultItem.getMaxStackSize()); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            compressorBlockEntity.setCompress(false);
        }
    }

    private static void canPurify(PurificatorBlockEntity purificatorBlockEntity, @Nullable Recipe<?> recipe) {
        if (!purificatorBlockEntity.inventory.getStackInSlot(1).isEmpty() && recipe != null) {
            ItemStack resultItem = recipe.getResultItem();
            if (resultItem.isEmpty()) {
                purificatorBlockEntity.setPurify(false);
            } else {
                ItemStack stackInSlot = purificatorBlockEntity.inventory.getStackInSlot(2);
                if (stackInSlot.isEmpty()) {
                    purificatorBlockEntity.setPurify(true);
                } else if (!stackInSlot.sameItem(resultItem)) {
                    purificatorBlockEntity.setPurify(false);
                } else if (stackInSlot.getCount() + resultItem.getCount() <= 64 && stackInSlot.getCount() + resultItem.getCount() <= stackInSlot.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    purificatorBlockEntity.setPurify(true);
                } else {
                    purificatorBlockEntity.setPurify(stackInSlot.getCount() + resultItem.getCount() <= resultItem.getMaxStackSize()); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            purificatorBlockEntity.setPurify(false);
        }
    }
}
