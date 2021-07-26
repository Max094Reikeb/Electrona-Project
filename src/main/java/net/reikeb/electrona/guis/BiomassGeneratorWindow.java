package net.reikeb.electrona.guis;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.BiomassGeneratorContainer;
import net.reikeb.electrona.tileentities.TileBiomassGenerator;

import org.lwjgl.opengl.GL11;

public class BiomassGeneratorWindow extends AbstractContainerScreen<BiomassGeneratorContainer> {

    private static final ResourceLocation BIOMASS_GENERATOR_GUI = new ResourceLocation(Electrona.MODID, "textures/guis/biomass_generator_gui.png");
    public TileBiomassGenerator tileEntity;

    public BiomassGeneratorWindow(BiomassGeneratorContainer container, Inventory inv, Component title) {
        super(container, inv, title);
        this.tileEntity = container.getTileEntity();
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.biomass_generator.name"), 7, 7, -16777216);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.generic.storage"), 125, 5, -16777216);
        this.font.draw(matrixStack, "" + ((int) this.tileEntity.getTileData().getDouble("ElectronicPower")) + " ELs", 125, 15, -3407821);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BIOMASS_GENERATOR_GUI);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, k, l, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) {
            this.minecraft.player.closeContainer();
            return true;
        }
        return super.keyPressed(key, b, c);
    }

    public void removed() {
        Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public void init() {
        minecraft.keyboardHandler.setSendRepeatsToGui(true);
    }
}
