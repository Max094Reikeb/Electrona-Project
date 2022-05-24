package net.reikeb.electrona.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.electrona.recipes.PurificatorRecipe;

import java.util.List;

public class PurifyingCategory implements IRecipeCategory<PurificatorRecipe> {

    private final IDrawable background;
    private final IDrawable icon;

    public PurifyingCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(Keys.PURIFICATOR_GUI, 0, 0, 166, 73);
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockInit.PURIFICATOR.get()));
    }

    public void draw(PurificatorRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        Font fontRenderer = Minecraft.getInstance().font;
        fontRenderer.draw(matrixStack, new TranslatableComponent("gui.electrona.purificator.name"), 52, 12, -16777216);
    }

    @SuppressWarnings("removal")
    @Override
    public ResourceLocation getUid() {
        return getRecipeType().getUid();
    }

    @SuppressWarnings("removal")
    @Override
    public Class<? extends PurificatorRecipe> getRecipeClass() {
        return getRecipeType().getRecipeClass();
    }

    @Override
    public RecipeType<PurificatorRecipe> getRecipeType() {
        return JeiPlugin.PURIFYING;
    }

    @Override
    public Component getTitle() {
        return BlockInit.PURIFICATOR.get().getName();
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PurificatorRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 27, 26)
                .addItemStacks(List.of(new ItemStack(Items.WATER_BUCKET, 1)))
                .setSlotName("input1Slot");
        builder.addSlot(RecipeIngredientRole.INPUT, 56, 40)
                .addItemStacks(List.of(recipe.getInput()))
                .setSlotName("input2Slot");
        builder.addSlot(RecipeIngredientRole.OUTPUT, 136, 40)
                .addItemStacks(List.of(recipe.getResultItem()));
    }

    @Override
    public boolean isHandled(PurificatorRecipe recipe) {
        return !recipe.isSpecial();
    }
}
