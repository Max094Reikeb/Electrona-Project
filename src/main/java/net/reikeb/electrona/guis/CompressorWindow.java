package net.reikeb.electrona.guis;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.CompressorContainer;
import net.reikeb.electrona.tileentities.TileCompressor;

import org.lwjgl.opengl.GL11;

public class CompressorWindow extends AbstractContainerScreen<CompressorContainer> {

    private static final ResourceLocation COMPRESSOR_GUI = new ResourceLocation(Electrona.MODID, "textures/guis/compressor_gui.png");
    public TileCompressor tileEntity;

    public CompressorWindow(CompressorContainer container, Inventory inv, Component title) {
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
        int currentCompress = tileEntity.getTileData().getInt("CurrentCompressingTime");
        int compressTime = tileEntity.getTileData().getInt("CompressingTime");
        int percentage = (currentCompress * 5) / (compressTime == 0 ? 100 : compressTime);
        int XposT1 = leftPos + 107;
        int XposT2 = leftPos + 125;
        int YposT1 = topPos + 43;
        int YposT2 = topPos + 52;
        String string = (percentage + "%");
        if (mouseX > XposT1 && mouseX < XposT2 && mouseY > YposT1 && mouseY < YposT2) {
            renderTooltip(matrixStack, Component.nullToEmpty(string), mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.compressor.name"), 50, 6, -16777216);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.generic.power"), 126, 6, -16777216);
        this.font.draw(matrixStack, "" + ((int) this.tileEntity.getTileData().getDouble("ElectronicPower")) + " ELs", 126, 16, -3407821);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, COMPRESSOR_GUI);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) {
            this.minecraft.player.closeContainer();
            return true;
        }
        return super.keyPressed(key, b, c);
    }

    @Override
    public void removed() {
        super.removed();
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
    }
}
