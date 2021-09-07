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
        registration.addRecipes(generateCompressorBlockRecipes2(), CompressorBlockJeiCategory.Uid);
        registration.addRecipes(generateCompressorBlockRecipes3(), CompressorBlockJeiCategory.Uid);
        registration.addRecipes(generateCompressorBlockRecipes4(), CompressorBlockJeiCategory.Uid);
        registration.addRecipes(generateCompressorBlockRecipes5(), CompressorBlockJeiCategory.Uid);
        registration.addRecipes(generateCompressorBlockRecipes6(), CompressorBlockJeiCategory.Uid);
        registration.addRecipes(generateCompressorBlockRecipes7(), CompressorBlockJeiCategory.Uid);
        registration.addRecipes(generateCompressorBlockRecipes8(), CompressorBlockJeiCategory.Uid);
        registration.addRecipes(generateCompressorBlockRecipes9(), CompressorBlockJeiCategory.Uid);
        registration.addRecipes(generatePurificatorBlockRecipes(), PurificatorBlockJeiCategory.Uid);
        registration.addRecipes(generatePurificatorBlockRecipes2(), PurificatorBlockJeiCategory.Uid);
    }

    private List<CompressorBlockRecipeWrapper> generateCompressorBlockRecipes() {
        List<CompressorBlockRecipeWrapper> recipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        inputs.add(new ItemStack(Items.IRON_INGOT));
        inputs.add(new ItemStack(Items.COAL));
        outputs.add(new ItemStack(ItemInit.STEEL_INGOT.get()));
        recipes.add(new CompressorBlockRecipeWrapper(inputs, outputs));
        return recipes;
    }

    private List<CompressorBlockRecipeWrapper> generateCompressorBlockRecipes2() {
        List<CompressorBlockRecipeWrapper> recipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        inputs.add(new ItemStack(ItemInit.STEEL_INGOT.get()));
        inputs.add(new ItemStack(ItemInit.STEEL_INGOT.get()));
        outputs.add(new ItemStack(ItemInit.STEEL_PLATE.get()));
        recipes.add(new CompressorBlockRecipeWrapper(inputs, outputs));
        return recipes;
    }

    private List<CompressorBlockRecipeWrapper> generateCompressorBlockRecipes3() {
        List<CompressorBlockRecipeWrapper> recipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        inputs.add(new ItemStack(Blocks.OBSIDIAN));
        inputs.add(new ItemStack(Blocks.OBSIDIAN));
        outputs.add(new ItemStack(BlockInit.COMPRESSED_OBSIDIAN.get().asItem()));
        recipes.add(new CompressorBlockRecipeWrapper(inputs, outputs));
        return recipes;
    }

    private List<CompressorBlockRecipeWrapper> generateCompressorBlockRecipes4() {
        List<CompressorBlockRecipeWrapper> recipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        inputs.add(new ItemStack(Blocks.COAL_BLOCK));
        inputs.add(new ItemStack(Blocks.COAL_BLOCK));
        outputs.add(new ItemStack(Items.DIAMOND));
        recipes.add(new CompressorBlockRecipeWrapper(inputs, outputs));
        return recipes;
    }

    private List<CompressorBlockRecipeWrapper> generateCompressorBlockRecipes5() {
        List<CompressorBlockRecipeWrapper> recipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        inputs.add(new ItemStack(Items.TOTEM_OF_UNDYING));
        inputs.add(new ItemStack(Items.NETHER_STAR));
        outputs.add(new ItemStack(ItemInit.ADVANCED_TOTEM_OF_UNDYING.get()));
        recipes.add(new CompressorBlockRecipeWrapper(inputs, outputs));
        return recipes;
    }

    private List<CompressorBlockRecipeWrapper> generateCompressorBlockRecipes6() {
        List<CompressorBlockRecipeWrapper> recipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        inputs.add(new ItemStack(BlockInit.URANIUM_ORE.get().asItem()));
        inputs.add(new ItemStack(BlockInit.URANIUM_ORE.get().asItem()));
        outputs.add(new ItemStack(ItemInit.YELLOWCAKE.get()));
        recipes.add(new CompressorBlockRecipeWrapper(inputs, outputs));
        return recipes;
    }

    private List<CompressorBlockRecipeWrapper> generateCompressorBlockRecipes7() {
        List<CompressorBlockRecipeWrapper> recipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        inputs.add(new ItemStack(ItemInit.PURIFIED_URANIUM.get()));
        inputs.add(new ItemStack(ItemInit.PURIFIED_URANIUM.get()));
        outputs.add(new ItemStack(ItemInit.URANIUM_BAR.get()));
        recipes.add(new CompressorBlockRecipeWrapper(inputs, outputs));
        return recipes;
    }

    private List<CompressorBlockRecipeWrapper> generateCompressorBlockRecipes8() {
        List<CompressorBlockRecipeWrapper> recipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        inputs.add(new ItemStack(ItemInit.URANIUM_BAR.get()));
        inputs.add(new ItemStack(ItemInit.URANIUM_BAR.get()));
        outputs.add(new ItemStack(ItemInit.URANIUM_DUAL_BAR.get()));
        recipes.add(new CompressorBlockRecipeWrapper(inputs, outputs));
        return recipes;
    }

    private List<CompressorBlockRecipeWrapper> generateCompressorBlockRecipes9() {
        List<CompressorBlockRecipeWrapper> recipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        inputs.add(new ItemStack(ItemInit.URANIUM_DUAL_BAR.get()));
        inputs.add(new ItemStack(ItemInit.URANIUM_DUAL_BAR.get()));
        outputs.add(new ItemStack(ItemInit.URANIUM_QUAD_BAR.get()));
        recipes.add(new CompressorBlockRecipeWrapper(inputs, outputs));
        return recipes;
    }

    private List<PurificatorBlockRecipeWrapper> generatePurificatorBlockRecipes() {
        List<PurificatorBlockRecipeWrapper> recipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        inputs.add(new ItemStack(Items.WATER_BUCKET));
        inputs.add(new ItemStack(ItemInit.CONCENTRATED_URANIUM.get(), (int) 3));
        outputs.add(new ItemStack(ItemInit.PURIFIED_URANIUM.get()));
        recipes.add(new PurificatorBlockRecipeWrapper(inputs, outputs));
        return recipes;
    }

    private List<PurificatorBlockRecipeWrapper> generatePurificatorBlockRecipes2() {
        List<PurificatorBlockRecipeWrapper> recipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        inputs.add(new ItemStack(Items.WATER_BUCKET));
        inputs.add(new ItemStack(Blocks.GOLD_ORE));
        int goldPowderNum = 1;
        if ((Math.random() < 0.5)) {
            goldPowderNum = 2;
        }
        outputs.add(new ItemStack(ItemInit.GOLD_POWDER.get(), goldPowderNum));
        recipes.add(new PurificatorBlockRecipeWrapper(inputs, outputs));
        return recipes;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockInit.COMPRESSOR.get().asItem()), CompressorBlockJeiCategory.Uid);
        registration.addRecipeCatalyst(new ItemStack(BlockInit.PURIFICATOR.get().asItem()), PurificatorBlockJeiCategory.Uid);
    }
}