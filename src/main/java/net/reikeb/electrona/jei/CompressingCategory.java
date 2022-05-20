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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.maxilib.Couple;

import java.util.List;

public class CompressingCategory implements IRecipeCategory<CompressingWrapper> {

    private final IDrawable background;

    public CompressingCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(Keys.COMPRESSOR_GUI, 0, 0, 176, 65);
    }

    public void draw(CompressingWrapper recipe, IRecipeSlotsView recipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        Font fontRenderer = Minecraft.getInstance().font;
        fontRenderer.draw(matrixStack, new TranslatableComponent("gui.electrona.compressor.name"), 50, 6, -16777216);
        fontRenderer.draw(matrixStack, new TranslatableComponent("gui.electrona.generic.power"), 126, 6, -16777216);
        Couple<ItemStack, ItemStack> recipeIntput = recipe.getInput();
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
            fontRenderer.draw(matrixStack, (recipeIntput.part1().getItem() == ItemInit.URANIUM_ORE_ITEM.get() ? "400 ELs" : "450 ELs"), 126, 16, -3407821);
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
    public void setRecipe(IRecipeLayoutBuilder builder, CompressingWrapper recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 26, 38)
                .addItemStacks(List.of(recipe.getInput().part1()))
                .setSlotName("input1Slot");
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 38)
                .addItemStacks(List.of(recipe.getInput().part2()))
                .setSlotName("input2Slot");
        builder.addSlot(RecipeIngredientRole.OUTPUT, 134, 38)
                .addItemStacks(List.of(recipe.getOutput()));
    }
}
