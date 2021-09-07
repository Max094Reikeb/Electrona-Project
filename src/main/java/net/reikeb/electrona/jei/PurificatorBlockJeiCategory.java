package net.reikeb.electrona.jei;

import com.mojang.blaze3d.vertex.PoseStack;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import net.reikeb.electrona.Electrona;

public class PurificatorBlockJeiCategory implements IRecipeCategory<PurificatorBlockRecipeWrapper> {

    public static final ResourceLocation Uid = Electrona.RL("purificatorblockcategory");
    private static final int input1 = 0;
    private static final int input2 = 1;
    private static final int output1 = 2;

    private final IDrawable background;

    public void draw(PurificatorBlockRecipeWrapper recipe, PoseStack matrixStack, double mouseX, double mouseY) {
        Font fontRenderer = Minecraft.getInstance().font;
        fontRenderer.draw(matrixStack, new TranslatableComponent("gui.electrona.purificator.name"), 48, 6, -16777216);
    }

    public PurificatorBlockJeiCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(Electrona.RL("textures/guis/purificator_gui.png"), 0, 0, 166, 73);
    }

    @Override
    public ResourceLocation getUid() {
        return Uid;
    }

    @Override
    public Class<? extends PurificatorBlockRecipeWrapper> getRecipeClass() {
        return PurificatorBlockRecipeWrapper.class;
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("jei.title.purifying");
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
    }
}
