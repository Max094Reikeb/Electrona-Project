package net.reikeb.electrona.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
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
import net.reikeb.electrona.utils.RInputs;

import java.util.ArrayList;
import java.util.List;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
    public static IJeiHelpers jeiHelper;

    @Override
    public ResourceLocation getPluginUid() {
        return Electrona.RL("default");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        jeiHelper = registration.getJeiHelpers();
        registration.addRecipeCategories(new CompressorBlockJeiCategory(jeiHelper.getGuiHelper()));
        registration.addRecipeCategories(new PurificatorBlockJeiCategory(jeiHelper.getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(generateCompressorBlockRecipes(), CompressorBlockJeiCategory.Uid);
        registration.addRecipes(generatePurificatorBlockRecipes(), PurificatorBlockJeiCategory.Uid);
    }

    private List<CompressorBlockRecipeWrapper> generateCompressorBlockRecipes() {
        List<CompressorBlockRecipeWrapper> recipes = new ArrayList<>();
        RInputs input1 = new RInputs(Items.IRON_INGOT, Items.COAL);
        RInputs input2 = new RInputs(ItemInit.STEEL_INGOT.get(), ItemInit.STEEL_INGOT.get());
        RInputs input3 = new RInputs(Blocks.OBSIDIAN, Blocks.OBSIDIAN);
        RInputs input4 = new RInputs(Blocks.COAL_BLOCK, Blocks.COAL_BLOCK);
        RInputs input5 = new RInputs(Items.TOTEM_OF_UNDYING, Items.NETHER_STAR);
        RInputs input6 = new RInputs(BlockInit.URANIUM_ORE.get(), BlockInit.URANIUM_ORE.get());
        RInputs input7 = new RInputs(BlockInit.DEEPSLATE_URANIUM_ORE.get(), BlockInit.DEEPSLATE_URANIUM_ORE.get());
        RInputs input8 = new RInputs(ItemInit.PURIFIED_URANIUM.get(), ItemInit.PURIFIED_URANIUM.get());
        RInputs input9 = new RInputs(ItemInit.URANIUM_BAR.get(), ItemInit.URANIUM_BAR.get());
        RInputs input10 = new RInputs(ItemInit.URANIUM_DUAL_BAR.get(), ItemInit.URANIUM_DUAL_BAR.get());
        recipes.add(new CompressorBlockRecipeWrapper(input1, ItemInit.STEEL_INGOT.get()));
        recipes.add(new CompressorBlockRecipeWrapper(input2, ItemInit.STEEL_PLATE.get()));
        recipes.add(new CompressorBlockRecipeWrapper(input3, BlockInit.COMPRESSED_OBSIDIAN.get().asItem()));
        recipes.add(new CompressorBlockRecipeWrapper(input4, Items.DIAMOND));
        recipes.add(new CompressorBlockRecipeWrapper(input5, ItemInit.ADVANCED_TOTEM_OF_UNDYING.get()));
        recipes.add(new CompressorBlockRecipeWrapper(input6, ItemInit.YELLOWCAKE.get()));
        recipes.add(new CompressorBlockRecipeWrapper(input7, ItemInit.YELLOWCAKE.get()));
        recipes.add(new CompressorBlockRecipeWrapper(input8, ItemInit.URANIUM_BAR.get()));
        recipes.add(new CompressorBlockRecipeWrapper(input9, ItemInit.URANIUM_DUAL_BAR.get()));
        recipes.add(new CompressorBlockRecipeWrapper(input10, ItemInit.URANIUM_QUAD_BAR.get()));
        return recipes;
    }

    private List<PurificatorBlockRecipeWrapper> generatePurificatorBlockRecipes() {
        List<PurificatorBlockRecipeWrapper> recipes = new ArrayList<>();
        RInputs input1 = new RInputs(Items.WATER_BUCKET, ItemInit.CONCENTRATED_URANIUM.get());
        recipes.add(new PurificatorBlockRecipeWrapper(input1, ItemInit.PURIFIED_URANIUM.get()));
        return recipes;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockInit.COMPRESSOR.get().asItem()), CompressorBlockJeiCategory.Uid);
        registration.addRecipeCatalyst(new ItemStack(BlockInit.PURIFICATOR.get().asItem()), PurificatorBlockJeiCategory.Uid);
    }
}