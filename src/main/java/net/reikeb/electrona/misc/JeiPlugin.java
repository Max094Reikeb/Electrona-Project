package net.reikeb.electrona.misc;

import com.mojang.blaze3d.vertex.PoseStack;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.helpers.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.item.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.*;

import java.util.*;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
    public static IJeiHelpers jeiHelper;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Electrona.MODID, "default");
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

    private List<CompressorBlockJeiCategory.CompressorBlockRecipeWrapper> generateCompressorBlockRecipes() {
        List<CompressorBlockJeiCategory.CompressorBlockRecipeWrapper> recipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        inputs.add(new ItemStack(Items.IRON_INGOT));
        inputs.add(new ItemStack(Items.COAL));
        outputs.add(new ItemStack(ItemInit.STEEL_INGOT.get()));
        recipes.add(new CompressorBlockJeiCategory.CompressorBlockRecipeWrapper(inputs, outputs));
        return recipes;
    }

    private List<CompressorBlockJeiCategory.CompressorBlockRecipeWrapper> generateCompressorBlockRecipes2() {
        List<CompressorBlockJeiCategory.CompressorBlockRecipeWrapper> recipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        inputs.add(new ItemStack(ItemInit.STEEL_INGOT.get()));
        inputs.add(new ItemStack(ItemInit.STEEL_INGOT.get()));
        outputs.add(new ItemStack(ItemInit.STEEL_PLATE.get()));
        recipes.add(new CompressorBlockJeiCategory.CompressorBlockRecipeWrapper(inputs, outputs));
        return recipes;
    }

    private List<CompressorBlockJeiCategory.CompressorBlockRecipeWrapper> generateCompressorBlockRecipes3() {
        List<CompressorBlockJeiCategory.CompressorBlockRecipeWrapper> recipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        inputs.add(new ItemStack(Blocks.OBSIDIAN));
        inputs.add(new ItemStack(Blocks.OBSIDIAN));
        outputs.add(new ItemStack(BlockInit.COMPRESSED_OBSIDIAN.get().asItem()));
        recipes.add(new CompressorBlockJeiCategory.CompressorBlockRecipeWrapper(inputs, outputs));
        return recipes;
    }

    private List<CompressorBlockJeiCategory.CompressorBlockRecipeWrapper> generateCompressorBlockRecipes4() {
        List<CompressorBlockJeiCategory.CompressorBlockRecipeWrapper> recipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        inputs.add(new ItemStack(Blocks.COAL_BLOCK));
        inputs.add(new ItemStack(Blocks.COAL_BLOCK));
        outputs.add(new ItemStack(Items.DIAMOND));
        recipes.add(new CompressorBlockJeiCategory.CompressorBlockRecipeWrapper(inputs, outputs));
        return recipes;
    }

    private List<CompressorBlockJeiCategory.CompressorBlockRecipeWrapper> generateCompressorBlockRecipes5() {
        List<CompressorBlockJeiCategory.CompressorBlockRecipeWrapper> recipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        inputs.add(new ItemStack(Items.TOTEM_OF_UNDYING));
        inputs.add(new ItemStack(Items.NETHER_STAR));
        outputs.add(new ItemStack(ItemInit.ADVANCED_TOTEM_OF_UNDYING.get()));
        recipes.add(new CompressorBlockJeiCategory.CompressorBlockRecipeWrapper(inputs, outputs));
        return recipes;
    }

    private List<CompressorBlockJeiCategory.CompressorBlockRecipeWrapper> generateCompressorBlockRecipes6() {
        List<CompressorBlockJeiCategory.CompressorBlockRecipeWrapper> recipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        inputs.add(new ItemStack(BlockInit.URANIUM_ORE.get().asItem()));
        inputs.add(new ItemStack(BlockInit.URANIUM_ORE.get().asItem()));
        outputs.add(new ItemStack(ItemInit.YELLOWCAKE.get()));
        recipes.add(new CompressorBlockJeiCategory.CompressorBlockRecipeWrapper(inputs, outputs));
        return recipes;
    }

    private List<CompressorBlockJeiCategory.CompressorBlockRecipeWrapper> generateCompressorBlockRecipes7() {
        List<CompressorBlockJeiCategory.CompressorBlockRecipeWrapper> recipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        inputs.add(new ItemStack(ItemInit.PURIFIED_URANIUM.get()));
        inputs.add(new ItemStack(ItemInit.PURIFIED_URANIUM.get()));
        outputs.add(new ItemStack(ItemInit.URANIUM_BAR.get()));
        recipes.add(new CompressorBlockJeiCategory.CompressorBlockRecipeWrapper(inputs, outputs));
        return recipes;
    }

    private List<CompressorBlockJeiCategory.CompressorBlockRecipeWrapper> generateCompressorBlockRecipes8() {
        List<CompressorBlockJeiCategory.CompressorBlockRecipeWrapper> recipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        inputs.add(new ItemStack(ItemInit.URANIUM_BAR.get()));
        inputs.add(new ItemStack(ItemInit.URANIUM_BAR.get()));
        outputs.add(new ItemStack(ItemInit.URANIUM_DUAL_BAR.get()));
        recipes.add(new CompressorBlockJeiCategory.CompressorBlockRecipeWrapper(inputs, outputs));
        return recipes;
    }

    private List<CompressorBlockJeiCategory.CompressorBlockRecipeWrapper> generateCompressorBlockRecipes9() {
        List<CompressorBlockJeiCategory.CompressorBlockRecipeWrapper> recipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        inputs.add(new ItemStack(ItemInit.URANIUM_DUAL_BAR.get()));
        inputs.add(new ItemStack(ItemInit.URANIUM_DUAL_BAR.get()));
        outputs.add(new ItemStack(ItemInit.URANIUM_QUAD_BAR.get()));
        recipes.add(new CompressorBlockJeiCategory.CompressorBlockRecipeWrapper(inputs, outputs));
        return recipes;
    }

    private List<PurificatorBlockJeiCategory.PurificatorBlockRecipeWrapper> generatePurificatorBlockRecipes() {
        List<PurificatorBlockJeiCategory.PurificatorBlockRecipeWrapper> recipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        inputs.add(new ItemStack(Items.WATER_BUCKET));
        inputs.add(new ItemStack(ItemInit.CONCENTRATED_URANIUM.get(), (int) 3));
        outputs.add(new ItemStack(ItemInit.PURIFIED_URANIUM.get()));
        recipes.add(new PurificatorBlockJeiCategory.PurificatorBlockRecipeWrapper(inputs, outputs));
        return recipes;
    }

    private List<PurificatorBlockJeiCategory.PurificatorBlockRecipeWrapper> generatePurificatorBlockRecipes2() {
        List<PurificatorBlockJeiCategory.PurificatorBlockRecipeWrapper> recipes = new ArrayList<>();
        ArrayList<ItemStack> inputs = new ArrayList<>();
        ArrayList<ItemStack> outputs = new ArrayList<>();
        inputs.add(new ItemStack(Items.WATER_BUCKET));
        inputs.add(new ItemStack(Blocks.GOLD_ORE));
        int goldPowderNum = 1;
        if ((Math.random() < 0.5)) {
            goldPowderNum = 2;
        }
        outputs.add(new ItemStack(ItemInit.GOLD_POWDER.get(), goldPowderNum));
        recipes.add(new PurificatorBlockJeiCategory.PurificatorBlockRecipeWrapper(inputs, outputs));
        return recipes;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockInit.COMPRESSOR.get().asItem()), CompressorBlockJeiCategory.Uid);
        registration.addRecipeCatalyst(new ItemStack(BlockInit.PURIFICATOR.get().asItem()), PurificatorBlockJeiCategory.Uid);
    }

    public static class CompressorBlockJeiCategory implements IRecipeCategory<CompressorBlockJeiCategory.CompressorBlockRecipeWrapper> {
        private static ResourceLocation Uid = new ResourceLocation(Electrona.MODID, "compressorblockcategory");
        private static final int input1 = 0; // THE NUMBER = SLOTID
        private static final int input2 = 1; // The NUMBER = SLOTID
        private static final int output1 = 2; // THE NUMBER = SLOTID

        // ...
        public void draw(CompressorBlockJeiCategory.CompressorBlockRecipeWrapper recipe, PoseStack matrixStack, double mouseX, double mouseY) {
            Font fontRenderer = Minecraft.getInstance().font;
            fontRenderer.draw(matrixStack, new TranslatableComponent("gui.electrona.compressor.name"), 50, 6, -16777216);
            fontRenderer.draw(matrixStack, new TranslatableComponent("gui.electrona.generic.power"), 126, 6, -16777216);
            ItemStack recipeOutput = (ItemStack) recipe.output.get(0);
            if (recipeOutput.getItem() == ItemInit.STEEL_INGOT.get()) {
                fontRenderer.draw(matrixStack, "400 ELs", 126, 16, -3407821);
            } else if (recipeOutput.getItem() == ItemInit.STEEL_PLATE.get()) {
                fontRenderer.draw(matrixStack, "480 ELs", 126, 16, -3407821);
            } else if (recipeOutput.getItem() == Items.DIAMOND) {
                fontRenderer.draw(matrixStack, "1500 ELs", 126, 16, -3407821);
            } else if (recipeOutput.getItem() == BlockInit.COMPRESSED_OBSIDIAN.get().asItem()) {
                fontRenderer.draw(matrixStack, "800 ELs", 126, 16, -3407821);
            } else if (recipeOutput.getItem() == ItemInit.ADVANCED_TOTEM_OF_UNDYING.get()) {
                fontRenderer.draw(matrixStack, "1200 ELs", 126, 16, -3407821);
            } else if (recipeOutput.getItem() == ItemInit.YELLOWCAKE.get()) {
                fontRenderer.draw(matrixStack, "400 ELs", 126, 16, -3407821);
            } else if (recipeOutput.getItem() == ItemInit.URANIUM_BAR.get()) {
                fontRenderer.draw(matrixStack, "400 ELs", 126, 16, -3407821);
            } else if (recipeOutput.getItem() == ItemInit.URANIUM_DUAL_BAR.get()) {
                fontRenderer.draw(matrixStack, "800 ELs", 126, 16, -3407821);
            } else if (recipeOutput.getItem() == ItemInit.URANIUM_QUAD_BAR.get()) {
                fontRenderer.draw(matrixStack, "1600 ELs", 126, 16, -3407821);
            }
        }

        // ...
        private final String title;
        private final IDrawable background;

        public CompressorBlockJeiCategory(IGuiHelper guiHelper) {
            this.title = "Compressing";
            this.background = guiHelper.createDrawable(new ResourceLocation(Electrona.MODID, "textures/guis/compressor_gui.png"), 0, 0, 176, 65);
        }

        @Override
        public ResourceLocation getUid() {
            return Uid;
        }

        @Override
        public Class<? extends CompressorBlockRecipeWrapper> getRecipeClass() {
            return CompressorBlockJeiCategory.CompressorBlockRecipeWrapper.class;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public IDrawable getBackground() {
            return background;
        }

        @Override
        public IDrawable getIcon() {
            return null;
        }

        @Override
        public void setIngredients(CompressorBlockRecipeWrapper recipeWrapper, IIngredients iIngredients) {
            iIngredients.setInputs(VanillaTypes.ITEM, recipeWrapper.getInput());
            iIngredients.setOutputs(VanillaTypes.ITEM, recipeWrapper.getOutput());
            // ...
        }

        @Override
        public void setRecipe(IRecipeLayout iRecipeLayout, CompressorBlockRecipeWrapper recipeWrapper, IIngredients iIngredients) {
            IGuiItemStackGroup stacks = iRecipeLayout.getItemStacks();
            stacks.init(input1, true, 26, 38);
            stacks.set(input1, iIngredients.getInputs(VanillaTypes.ITEM).get(0));
            stacks.init(input2, true, 80, 38);
            stacks.set(input2, iIngredients.getInputs(VanillaTypes.ITEM).get(1));
            stacks.init(output1, false, 134, 37);
            stacks.set(output1, iIngredients.getOutputs(VanillaTypes.ITEM).get(0));
            // ...
        }

        public static class CompressorBlockRecipeWrapper {
            private ArrayList input;
            private ArrayList output;

            public CompressorBlockRecipeWrapper(ArrayList input, ArrayList output) {
                this.input = input;
                this.output = output;
            }

            public ArrayList getInput() {
                return input;
            }

            public ArrayList getOutput() {
                return output;
            }
        }
    }

    public static class PurificatorBlockJeiCategory implements IRecipeCategory<PurificatorBlockJeiCategory.PurificatorBlockRecipeWrapper> {
        private static ResourceLocation Uid = new ResourceLocation(Electrona.MODID, "purificatorblockcategory");
        private static final int input1 = 0; // THE NUMBER = SLOTID
        private static final int input2 = 1; // The NUMBER = SLOTID
        private static final int output1 = 2; // THE NUMBER = SLOTID

        // ...
        public void draw(PurificatorBlockJeiCategory.PurificatorBlockRecipeWrapper recipe, PoseStack matrixStack, double mouseX, double mouseY) {
            Font fontRenderer = Minecraft.getInstance().font;
            fontRenderer.draw(matrixStack, new TranslatableComponent("gui.electrona.purificator.name"), 48, 6, -16777216);
        }

        // ...
        private final String title;
        private final IDrawable background;

        public PurificatorBlockJeiCategory(IGuiHelper guiHelper) {
            this.title = "Purifying";
            this.background = guiHelper.createDrawable(new ResourceLocation(Electrona.MODID, "textures/guis/purificator_gui.png"), 0, 0, 166, 73);
        }

        @Override
        public ResourceLocation getUid() {
            return Uid;
        }

        @Override
        public Class<? extends PurificatorBlockRecipeWrapper> getRecipeClass() {
            return PurificatorBlockJeiCategory.PurificatorBlockRecipeWrapper.class;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public IDrawable getBackground() {
            return background;
        }

        @Override
        public IDrawable getIcon() {
            return null;
        }

        @Override
        public void setIngredients(PurificatorBlockRecipeWrapper recipeWrapper, IIngredients iIngredients) {
            iIngredients.setInputs(VanillaTypes.ITEM, recipeWrapper.getInput());
            iIngredients.setOutputs(VanillaTypes.ITEM, recipeWrapper.getOutput());
            // ...
        }

        @Override
        public void setRecipe(IRecipeLayout iRecipeLayout, PurificatorBlockRecipeWrapper recipeWrapper, IIngredients iIngredients) {
            IGuiItemStackGroup stacks = iRecipeLayout.getItemStacks();
            stacks.init(input1, true, 26, 42);
            stacks.set(input1, iIngredients.getInputs(VanillaTypes.ITEM).get(0));
            stacks.init(input2, true, 55, 27);
            stacks.set(input2, iIngredients.getInputs(VanillaTypes.ITEM).get(1));
            stacks.init(output1, false, 135, 27);
            stacks.set(output1, iIngredients.getOutputs(VanillaTypes.ITEM).get(0));
            // ...
        }

        public static class PurificatorBlockRecipeWrapper {
            private ArrayList input;
            private ArrayList output;

            public PurificatorBlockRecipeWrapper(ArrayList input, ArrayList output) {
                this.input = input;
                this.output = output;
            }

            public ArrayList getInput() {
                return input;
            }

            public ArrayList getOutput() {
                return output;
            }
        }
    }
}
