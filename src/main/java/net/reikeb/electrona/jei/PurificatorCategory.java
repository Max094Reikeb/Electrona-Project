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
import net.reikeb.electrona.misc.Keys;

import java.util.ArrayList;
import java.util.Collections;

public class PurificatorCategory implements IRecipeCategory<PurificatorWrapper> {

    private static final int input1 = 0;
    private static final int input2 = 1;
    private static final int output1 = 2;

    private final IDrawable background;

    public PurificatorCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(Electrona.RL("textures/guis/purificator_gui.png"), 0, 0, 166, 73);
    }

    public void draw(PurificatorWrapper recipe, PoseStack matrixStack, double mouseX, double mouseY) {
        Font fontRenderer = Minecraft.getInstance().font;
        fontRenderer.draw(matrixStack, new TranslatableComponent("gui.electrona.purificator.name"), 52, 12, -16777216);
    }

    @Override
    public ResourceLocation getUid() {
        return Keys.PURIFICATOR_ID;
    }

    @Override
    public Class<? extends PurificatorWrapper> getRecipeClass() {
        return PurificatorWrapper.class;
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
    public void setIngredients(PurificatorWrapper recipeWrapper, IIngredients iIngredients) {
        iIngredients.setInputs(VanillaTypes.ITEM, recipeWrapper.getInput().getInputs());
        iIngredients.setOutputs(VanillaTypes.ITEM, new ArrayList<>(Collections.singleton(recipeWrapper.getOutput())));
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, PurificatorWrapper recipeWrapper, IIngredients iIngredients) {
        IGuiItemStackGroup stacks = iRecipeLayout.getItemStacks();
        stacks.init(input1, true, 26, 26);
        stacks.set(input1, iIngredients.getInputs(VanillaTypes.ITEM).get(0));
        stacks.init(input2, true, 56, 40);
        stacks.set(input2, iIngredients.getInputs(VanillaTypes.ITEM).get(1));
        stacks.init(output1, false, 136, 40);
        stacks.set(output1, iIngredients.getOutputs(VanillaTypes.ITEM).get(0));
    }
}