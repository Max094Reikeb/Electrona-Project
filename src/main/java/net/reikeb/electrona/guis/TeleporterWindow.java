package net.reikeb.electrona.guis;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.TeleporterContainer;
import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.TeleporterAutoDeletePacket;
import net.reikeb.electrona.network.packets.TeleporterLinkPacket;
import net.reikeb.electrona.tileentities.TileTeleporter;

import org.lwjgl.opengl.GL11;

public class TeleporterWindow extends AbstractContainerScreen<TeleporterContainer> {

    private static final ResourceLocation TELEPORTER_GUI = new ResourceLocation(Electrona.MODID, "textures/guis/teleporter_gui.png");
    public TileTeleporter tileEntity;

    public TeleporterWindow(TeleporterContainer container, Inventory inv, Component title) {
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
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.teleporter.name"), 62, 8, -16777216);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.teleporter.link"), 136, 8, -16777216);
        this.font.draw(matrixStack, "" + this.tileEntity.getTileData().getDouble("teleportX") + "", 135, 19, -13408513);
        this.font.draw(matrixStack, "" + this.tileEntity.getTileData().getDouble("teleportY") + "", 135, 28, -13408513);
        this.font.draw(matrixStack, "" + this.tileEntity.getTileData().getDouble("teleportZ") + "", 135, 38, -13408513);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.generic.storage"), 5, 7, -16777216);
        this.font.draw(matrixStack, "" + ((int) this.tileEntity.getTileData().getDouble("ElectronicPower")) + " ELs", 5, 18, -3407821);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.teleporter.auto_deletion"), 6, 28, -16777216);
        this.font.draw(matrixStack, "" + this.tileEntity.getTileData().getBoolean("autoDeletion") + "", 5, 38, -3407821);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TELEPORTER_GUI);
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
    public void removed() {
        super.removed();
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public void init() {
        this.addRenderableWidget(new Button(this.leftPos + 89, this.topPos + 60, 70, 20,
                new TranslatableComponent("gui.electrona.teleporter.transfer_button"), e -> {
            NetworkManager.INSTANCE.sendToServer(new TeleporterLinkPacket());
        }));

        this.addRenderableWidget(new Button(this.leftPos + 7, this.topPos + 60, 40, 20,
                new TranslatableComponent("gui.electrona.teleporter.auto_button"), e -> {
            NetworkManager.INSTANCE.sendToServer(new TeleporterAutoDeletePacket());
        }));
    }
}
