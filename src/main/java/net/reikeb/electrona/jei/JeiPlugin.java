package net.reikeb.electrona.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.electrona.recipes.CompressorRecipe;
import net.reikeb.electrona.recipes.PurificatorRecipe;
import net.reikeb.maxilib.Couple;

import java.util.ArrayList;
import java.util.List;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {

    public static final RecipeType<CompressorRecipe> COMPRESSING = RecipeType.create(Electrona.MODID, "compressing", CompressorRecipe.class);
    public static final RecipeType<PurificatorRecipe> PURIFYING = RecipeType.create(Electrona.MODID, "purifying", PurificatorRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return Keys.DEFAULT_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelper = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelper.getGuiHelper();
        registration.addRecipeCategories(
                new CompressingCategory(guiHelper),
                new PurifyingCategory(guiHelper)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(COMPRESSING, generateCompressingRecipes());
        registration.addRecipes(PURIFYING, generatePurificationRecipes());
    }

    private List<CompressorRecipe> generateCompressingRecipes() {
        List<CompressorRecipe> recipes = new ArrayList<>();

        recipes.add(addCompressingRecipe(Items.IRON_INGOT, Items.COAL, ItemInit.STEEL_INGOT.get()));
        recipes.add(addCompressingRecipe(ItemInit.STEEL_INGOT.get(), ItemInit.STEEL_INGOT.get(), ItemInit.STEEL_PLATE.get()));
        recipes.add(addCompressingRecipe(Blocks.OBSIDIAN, Blocks.OBSIDIAN, BlockInit.COMPRESSED_OBSIDIAN.get()));
        recipes.add(addCompressingRecipe(Blocks.COAL_BLOCK, Blocks.COAL_BLOCK, Items.DIAMOND));
        recipes.add(addCompressingRecipe(Items.TOTEM_OF_UNDYING, Items.NETHER_STAR, ItemInit.ADVANCED_TOTEM_OF_UNDYING.get()));
        recipes.add(addCompressingRecipe(BlockInit.URANIUM_ORE.get(), BlockInit.URANIUM_ORE.get(), ItemInit.YELLOWCAKE.get()));
        recipes.add(addCompressingRecipe(BlockInit.DEEPSLATE_URANIUM_ORE.get(), BlockInit.DEEPSLATE_URANIUM_ORE.get(), ItemInit.YELLOWCAKE.get()));
        recipes.add(addCompressingRecipe(ItemInit.PURIFIED_URANIUM.get(), ItemInit.PURIFIED_URANIUM.get(), ItemInit.URANIUM_BAR.get()));
        recipes.add(addCompressingRecipe(ItemInit.URANIUM_BAR.get(), ItemInit.URANIUM_BAR.get(), ItemInit.URANIUM_DUAL_BAR.get()));
        recipes.add(addCompressingRecipe(ItemInit.URANIUM_DUAL_BAR.get(), ItemInit.URANIUM_DUAL_BAR.get(), ItemInit.URANIUM_QUAD_BAR.get()));
        return recipes;
    }

    private List<PurificatorRecipe> generatePurificationRecipes() {
        List<PurificatorRecipe> recipes = new ArrayList<>();
        recipes.add(addPurifyingRecipe(ItemInit.CONCENTRATED_URANIUM.get(), ItemInit.PURIFIED_URANIUM.get()));
        return recipes;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockInit.COMPRESSOR.get()), COMPRESSING);
        registration.addRecipeCatalyst(new ItemStack(BlockInit.PURIFICATOR.get()), PURIFYING);
    }

    private static CompressorRecipe addCompressingRecipe(Block input1, Block input2, Block output) {
        return addCompressingRecipe(input1.asItem(), input2.asItem(), output.asItem());
    }

    private static CompressorRecipe addCompressingRecipe(Block input1, Block input2, Item output) {
        return addCompressingRecipe(input1.asItem(), input2.asItem(), output);
    }

    private static CompressorRecipe addCompressingRecipe(Item input1, Item input2, Item output) {
        return new CompressorRecipe(Keys.COMPRESSING, new Couple<>(Ingredient.of(input1), Ingredient.of(input2)), new ItemStack(output, 1), 20, 20);
    }

    private static PurificatorRecipe addPurifyingRecipe(Item input, Item output) {
        return new PurificatorRecipe(Keys.PURIFYING, new ItemStack(input, 1), new ItemStack(output, 1), false, 20, 20);
    }
}