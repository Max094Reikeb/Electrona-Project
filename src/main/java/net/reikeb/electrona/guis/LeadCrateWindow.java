package net.reikeb.electrona.guis;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.LeadCrateContainer;
import net.reikeb.electrona.tileentities.TileLeadCrate;

import org.lwjgl.opengl.GL11;

public class LeadCrateWindow extends ContainerScreen<LeadCrateContainer> {

    private static final ResourceLocation LEAD_CRATE_GUI = new ResourceLocation(Electrona.MODID, "textures/guis/lead_crate_gui.png");
    public TileLeadCrate tileEntity;

    public LeadCrateWindow(LeadCrateContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
        this.tileEntity = container.getTileEntity();
        this.imageWidth = 176;
        this.imageHeight = 166;

    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);
        this.renderTooltip(ms, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(MatrixStack ms, int mouseX, int mouseY) {
        this.font.draw(ms, new TranslationTextComponent("gui.electrona.lead_crate.name"), 7, 6, -16777216);
    }

    @Override
    protected void renderBg(MatrixStack ms, float par1, int par2, int par3) {
        GL11.glColor4f(1, 1, 1, 1);
        Minecraft.getInstance().getTextureManager().bind(LEAD_CRATE_GUI);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        this.blit(ms, k, l, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
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
