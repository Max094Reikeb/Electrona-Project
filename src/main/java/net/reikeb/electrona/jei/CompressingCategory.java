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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.maxilib.RInputs;

import java.util.ArrayList;
import java.util.Collections;

public class CompressingCategory implements IRecipeCategory<CompressingWrapper> {

    private static final int input1 = 0;
    private static final int input2 = 1;
    private static final int output1 = 2;

    private final IDrawable background;

    public CompressingCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(Electrona.RL("textures/guis/compressor_gui.png"), 0, 0, 176, 65);
    }

    public void draw(CompressingWrapper recipe, PoseStack matrixStack, double mouseX, double mouseY) {
        Font fontRenderer = Minecraft.getInstance().font;
        fontRenderer.draw(matrixStack, new TranslatableComponent("gui.electrona.compressor.name"), 50, 6, -16777216);
        fontRenderer.draw(matrixStack, new TranslatableComponent("gui.electrona.generic.power"), 126, 6, -16777216);
        RInputs recipeIntput = recipe.getInput();
        ItemStack recipeOutput = recipe.getOutput();
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
            fontRenderer.draw(matrixStack, (recipeIntput.getItemInput1() == ItemInit.URANIUM_ORE_ITEM.get() ? "400 ELs" : "450 ELs"), 126, 16, -3407821);
        } else if (recipeOutput.getItem() == ItemInit.URANIUM_BAR.get()) {
            fontRenderer.draw(matrixStack, "400 ELs", 126, 16, -3407821);
        } else if (recipeOutput.getItem() == ItemInit.URANIUM_DUAL_BAR.get()) {
            fontRenderer.draw(matrixStack, "800 ELs", 126, 16, -3407821);
        } else if (recipeOutput.getItem() == ItemInit.URANIUM_QUAD_BAR.get()) {
            fontRenderer.draw(matrixStack, "1600 ELs", 126, 16, -3407821);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return Keys.COMPRESSOR_ID;
    }

    @Override
    public Class<? extends CompressingWrapper> getRecipeClass() {
        return CompressingWrapper.class;
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("jei.title.compressing");
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
    public void setIngredients(CompressingWrapper recipeWrapper, IIngredients iIngredients) {
        iIngredients.setInputs(VanillaTypes.ITEM, recipeWrapper.getInput().getInputs());
        iIngredients.setOutputs(VanillaTypes.ITEM, new ArrayList<>(Collections.singleton(recipeWrapper.getOutput())));
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, CompressingWrapper recipeWrapper, IIngredients iIngredients) {
        IGuiItemStackGroup stacks = iRecipeLayout.getItemStacks();
        stacks.init(input1, true, 26, 38);
        stacks.set(input1, iIngredients.getInputs(VanillaTypes.ITEM).get(0));
        stacks.init(input2, true, 80, 38);
        stacks.set(input2, iIngredients.getInputs(VanillaTypes.ITEM).get(1));
        stacks.init(output1, false, 134, 37);
        stacks.set(output1, iIngredients.getOutputs(VanillaTypes.ITEM).get(0));
    }
}
