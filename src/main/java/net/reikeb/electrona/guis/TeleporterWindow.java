package net.reikeb.electrona.guis;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.TeleporterContainer;
import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.*;
import net.reikeb.electrona.tileentities.TileTeleporter;

import org.lwjgl.opengl.GL11;

public class TeleporterWindow extends ContainerScreen<TeleporterContainer> {

    private static final ResourceLocation TELEPORTER_GUI = new ResourceLocation(Electrona.MODID, "textures/guis/teleporter_gui.png");
    public TileTeleporter tileEntity;

    public TeleporterWindow(TeleporterContainer container, PlayerInventory inv, ITextComponent title) {
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
        this.font.draw(matrixStack, new TranslationTextComponent("electrona.teleporter_gui.name"), 62, 8, -16777216);
        this.font.draw(matrixStack, new TranslationTextComponent("electrona.teleporter_gui.link"), 136, 8, -16777216);
        this.font.draw(matrixStack, "" + this.tileEntity.getTileData().getDouble("teleportX") + "", 135, 19, -13408513);
        this.font.draw(matrixStack, "" + this.tileEntity.getTileData().getDouble("teleportY") + "", 135, 28, -13408513);
        this.font.draw(matrixStack, "" + this.tileEntity.getTileData().getDouble("teleportZ") + "", 135, 38, -13408513);
        this.font.draw(matrixStack, new TranslationTextComponent("electrona.generic_gui.storage"), 5, 7, -16777216);
        this.font.draw(matrixStack, "" + ((int) this.tileEntity.getTileData().getDouble("ElectronicPower")) + " ELs", 5, 18, -3407821);
        this.font.draw(matrixStack, new TranslationTextComponent("electrona.teleporter_gui.auto_deletion"), 6, 28, -16777216);
        this.font.draw(matrixStack, "" + this.tileEntity.getTileData().getBoolean("autoDeletion") + "", 5, 38, -3407821);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bind(TELEPORTER_GUI);
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
        super.removed();
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
        this.addButton(new Button(this.leftPos + 89, this.topPos + 60, 70, 20,
                new TranslationTextComponent("electrona.teleporter_gui.transfer_button"), e -> {
            NetworkManager.INSTANCE.sendToServer(new TeleporterLinkPacket());
        }));

        this.addButton(new Button(this.leftPos + 7, this.topPos + 60, 40, 20,
                new TranslationTextComponent("electrona.teleporter_gui.auto_button"), e -> {
            NetworkManager.INSTANCE.sendToServer(new TeleporterAutoDeletePacket());
        }));
    }
}
