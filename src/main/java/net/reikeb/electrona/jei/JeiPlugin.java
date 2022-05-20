package net.reikeb.electrona.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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

    public static IJeiHelpers jeiHelper;
    public static final RecipeType<CompressorRecipe> COMPRESSING = RecipeType.create(Electrona.MODID, "compressing", CompressorRecipe.class);
    public static final RecipeType<PurificatorRecipe> PURIFYING = RecipeType.create(Electrona.MODID, "purifying", PurificatorRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return Keys.DEFAULT_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        jeiHelper = registration.getJeiHelpers();
        registration.addRecipeCategories(new CompressingCategory(jeiHelper.getGuiHelper()));
        registration.addRecipeCategories(new PurificatorCategory(jeiHelper.getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(COMPRESSING, generateCompressingRecipes());
        registration.addRecipes(PURIFYING, generatePurificationRecipes());
    }

    private List<CompressorRecipe> generateCompressingRecipes() {
        List<CompressorRecipe> recipes = new ArrayList<>();

        recipes.add(new CompressingWrapper(new Couple<>(Items.IRON_INGOT, Items.COAL), ItemInit.STEEL_INGOT.get()));
        recipes.add(new CompressingWrapper(new Couple<>(ItemInit.STEEL_INGOT.get(), ItemInit.STEEL_INGOT.get()), ItemInit.STEEL_PLATE.get()));
        recipes.add(new CompressingWrapper(new Couple<>(Blocks.OBSIDIAN, Blocks.OBSIDIAN), BlockInit.COMPRESSED_OBSIDIAN.get()));
        recipes.add(new CompressingWrapper(new Couple<>(Blocks.COAL_BLOCK.asItem(), Blocks.COAL_BLOCK.asItem()), Items.DIAMOND));
        recipes.add(new CompressingWrapper(new Couple<>(Items.TOTEM_OF_UNDYING, Items.NETHER_STAR), ItemInit.ADVANCED_TOTEM_OF_UNDYING.get()));
        recipes.add(new CompressingWrapper(new Couple<>(BlockInit.URANIUM_ORE.get().asItem(), BlockInit.URANIUM_ORE.get().asItem()), ItemInit.YELLOWCAKE.get()));
        recipes.add(new CompressingWrapper(new Couple<>(BlockInit.DEEPSLATE_URANIUM_ORE.get().asItem(), BlockInit.DEEPSLATE_URANIUM_ORE.get().asItem()), ItemInit.YELLOWCAKE.get()));
        recipes.add(new CompressingWrapper(new Couple<>(ItemInit.PURIFIED_URANIUM.get(), ItemInit.PURIFIED_URANIUM.get()), ItemInit.URANIUM_BAR.get()));
        recipes.add(new CompressingWrapper(new Couple<>(ItemInit.URANIUM_BAR.get(), ItemInit.URANIUM_BAR.get()), ItemInit.URANIUM_DUAL_BAR.get()));
        recipes.add(new CompressingWrapper(new Couple<>(ItemInit.URANIUM_DUAL_BAR.get(), ItemInit.URANIUM_DUAL_BAR.get()), ItemInit.URANIUM_QUAD_BAR.get()));
        return recipes;
    }

    private List<PurificatorRecipe> generatePurificationRecipes() {
        List<PurificatorRecipe> recipes = new ArrayList<>();
        recipes.add(new PurificatorWrapper(new Couple<>(Items.WATER_BUCKET, ItemInit.CONCENTRATED_URANIUM.get()), ItemInit.PURIFIED_URANIUM.get()));
        return recipes;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockInit.COMPRESSOR.get()), COMPRESSING);
        registration.addRecipeCatalyst(new ItemStack(BlockInit.PURIFICATOR.get()), PURIFYING);
    }
}