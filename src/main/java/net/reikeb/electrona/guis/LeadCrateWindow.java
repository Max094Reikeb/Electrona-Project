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
import net.reikeb.electrona.containers.LeadCrateContainer;
import net.reikeb.electrona.tileentities.TileLeadCrate;

import org.lwjgl.opengl.GL11;

public class LeadCrateWindow extends AbstractContainerScreen<LeadCrateContainer> {

    private static final ResourceLocation LEAD_CRATE_GUI = new ResourceLocation(Electrona.MODID, "textures/guis/lead_crate_gui.png");
    public TileLeadCrate tileEntity;

    public LeadCrateWindow(LeadCrateContainer container, Inventory inv, Component title) {
        super(container, inv, title);
        this.tileEntity = container.getTileEntity();
        this.imageWidth = 176;
        this.imageHeight = 166;

    }

    @Override
    public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);
        this.renderTooltip(ms, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
        this.font.draw(ms, new TranslatableComponent("gui.electrona.lead_crate.name"), 7, 6, -16777216);
    }

    @Override
    protected void renderBg(PoseStack ms, float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, LEAD_CRATE_GUI);
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
    public void removed() {
        Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public void init() {
        minecraft.keyboardHandler.setSendRepeatsToGui(true);
    }
}
