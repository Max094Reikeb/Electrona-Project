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
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.electrona.recipes.CompressorRecipe;
import net.reikeb.maxilib.Couple;

import java.util.List;

public class CompressingCategory implements IRecipeCategory<CompressorRecipe> {

    private final IDrawable background;
    private final IDrawable icon;

    public CompressingCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(Keys.COMPRESSOR_GUI, 0, 0, 176, 65);
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockInit.COMPRESSOR.get()));
    }

    public void draw(CompressorRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        Font fontRenderer = Minecraft.getInstance().font;
        fontRenderer.draw(matrixStack, new TranslatableComponent("gui.electrona.compressor.name"), 50, 6, -16777216);
        fontRenderer.draw(matrixStack, new TranslatableComponent("gui.electrona.generic.power"), 126, 6, -16777216);
        Couple<ItemStack, ItemStack> recipeInput = recipe.getInputs();
        ItemStack recipeOutput = recipe.getResultItem();
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
            fontRenderer.draw(matrixStack, (recipeInput.part1().getItem() == ItemInit.URANIUM_ORE_ITEM.get() ? "400 ELs" : "450 ELs"), 126, 16, -3407821);
        } else if (recipeOutput.getItem() == ItemInit.URANIUM_BAR.get()) {
            fontRenderer.draw(matrixStack, "400 ELs", 126, 16, -3407821);
        } else if (recipeOutput.getItem() == ItemInit.URANIUM_DUAL_BAR.get()) {
            fontRenderer.draw(matrixStack, "800 ELs", 126, 16, -3407821);
        } else if (recipeOutput.getItem() == ItemInit.URANIUM_QUAD_BAR.get()) {
            fontRenderer.draw(matrixStack, "1600 ELs", 126, 16, -3407821);
        }
    }

    @SuppressWarnings("removal")
    @Override
    public ResourceLocation getUid() {
        return getRecipeType().getUid();
    }

    @SuppressWarnings("removal")
    @Override
    public Class<? extends CompressorRecipe> getRecipeClass() {
        return getRecipeType().getRecipeClass();
    }

    @Override
    public RecipeType<CompressorRecipe> getRecipeType() {
        return JeiPlugin.COMPRESSING;
    }

    @Override
    public Component getTitle() {
        return BlockInit.COMPRESSOR.get().getName();
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
    public void setRecipe(IRecipeLayoutBuilder builder, CompressorRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 27, 39)
                .addItemStacks(List.of(recipe.getInputIngredients().part1().getItems()))
                .setSlotName("input1Slot");
        builder.addSlot(RecipeIngredientRole.INPUT, 81, 39)
                .addItemStacks(List.of(recipe.getInputIngredients().part2().getItems()))
                .setSlotName("input2Slot");
        builder.addSlot(RecipeIngredientRole.OUTPUT, 135, 38)
                .addItemStacks(List.of(recipe.getResultItem()));
    }

    @Override
    public boolean isHandled(CompressorRecipe recipe) {
        return !recipe.isSpecial();
    }
}
