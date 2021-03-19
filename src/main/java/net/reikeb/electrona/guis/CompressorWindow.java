package net.reikeb.electrona.guis;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.CompressorContainer;
import net.reikeb.electrona.tileentities.TileCompressor;

public class CompressorWindow extends ContainerScreen<CompressorContainer> {

    private static final ResourceLocation COMPRESSOR_GUI = new ResourceLocation(Electrona.MODID, "textures/guis/compressor_gui.png");
    public TileCompressor tileEntity;

    public CompressorWindow(CompressorContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
        this.tileEntity = container.getTileEntity();
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        int currentCompress = tileEntity.getTileData().getInt("CurrentCompressingTime");
        int compressTime = tileEntity.getTileData().getInt("CompressingTime");
        int percentage = (currentCompress * 100) / ((compressTime == 0) ? 100 : compressTime);
        int XposT1 = leftPos + 107;
        int XposT2 = leftPos + 125;
        int YposT1 = topPos + 43;
        int YposT2 = topPos + 52;
        String string = (percentage + "%");
        if (mouseX > XposT1 && mouseX < XposT2 && mouseY > YposT1 && mouseY < YposT2) {
            renderTooltip(matrixStack, ITextComponent.nullToEmpty(string), mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, new TranslationTextComponent("electrona.compressor_gui.name"), 50, 6, -16777216);
        this.font.draw(matrixStack, new TranslationTextComponent("electrona.generic_gui.power"), 126, 6, -16777216);
        this.font.draw(matrixStack, "" + ((int) this.tileEntity.getTileData().getDouble("ElectronicPower")) + " ELs", 126, 16, -3407821);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(COMPRESSOR_GUI);
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
    public void tick() {
        super.tick();
    }

    @Override
    public void removed() {
        super.removed();
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
    }
}
