package net.reikeb.electrona.guis;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.SteelCrateContainer;
import net.reikeb.electrona.tileentities.TileSteelCrate;

import org.lwjgl.opengl.GL11;

public class SteelCrateWindow extends ContainerScreen<SteelCrateContainer> {

    private static final ResourceLocation STEEL_CRATE_GUI = new ResourceLocation(Electrona.MODID, "textures/guis/steel_crate_gui.png");
    public TileSteelCrate tileEntity;

    public SteelCrateWindow(SteelCrateContainer container, PlayerInventory inv, ITextComponent title) {
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
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, new TranslationTextComponent("gui.electrona.steel_crate.name"), 7, 6, -16777216);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bind(STEEL_CRATE_GUI);
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

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void removed() {
        Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
        minecraft.keyboardHandler.setSendRepeatsToGui(true);
    }
}
