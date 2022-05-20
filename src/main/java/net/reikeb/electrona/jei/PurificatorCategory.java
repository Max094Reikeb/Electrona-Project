package net.reikeb.electrona.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.reikeb.electrona.misc.Keys;

import java.util.List;

public class PurificatorCategory implements IRecipeCategory<PurificatorWrapper> {

    private final IDrawable background;

    public PurificatorCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(Keys.PURIFICATOR_GUI, 0, 0, 166, 73);
    }

    public void draw(PurificatorWrapper recipe, IRecipeSlotsView recipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
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
    public void setRecipe(IRecipeLayoutBuilder builder, PurificatorWrapper recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 26, 26)
                .addItemStacks(List.of(recipe.getInput().part1()))
                .setSlotName("input1Slot");
        builder.addSlot(RecipeIngredientRole.INPUT, 56, 40)
                .addItemStacks(List.of(recipe.getInput().part2()))
                .setSlotName("input2Slot");
        builder.addSlot(RecipeIngredientRole.OUTPUT, 136, 40)
                .addItemStacks(List.of(recipe.getOutput()));

    }
}
