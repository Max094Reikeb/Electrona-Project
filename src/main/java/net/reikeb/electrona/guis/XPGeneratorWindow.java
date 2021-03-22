package net.reikeb.electrona.guis;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.XPGeneratorContainer;
import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.ExperienceHarvestPacket;
import net.reikeb.electrona.tileentities.TileXPGenerator;

import org.lwjgl.opengl.GL11;

public class XPGeneratorWindow extends ContainerScreen<XPGeneratorContainer> {

    private static final ResourceLocation XP_GENERATOR_GUI = new ResourceLocation(Electrona.MODID, "textures/guis/xp_generator_gui.png");
    public TileXPGenerator tileEntity;

    public XPGeneratorWindow(XPGeneratorContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
        this.tileEntity = container.getTileEntity();
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        int currentXP = this.tileEntity.getTileData().getInt("wait");
        int percentage = (currentXP * 100) / 4800;
        int XposT1 = leftPos + 19;
        int XposT2 = leftPos + 30;
        int YposT1 = topPos + 23;
        int YposT2 = topPos + 39;
        String string = (percentage + "%");
        if (mouseX > XposT1 && mouseX < XposT2 && mouseY > YposT1 && mouseY < YposT2) {
            renderTooltip(matrixStack, ITextComponent.nullToEmpty(string), mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, new TranslationTextComponent("electrona.xp_generator_gui.name"), 8, 7, -16777216);
        this.font.draw(matrixStack, new TranslationTextComponent("electrona.generic_gui.power"), 121, 7, -16777216);
        this.font.draw(matrixStack, "" + ((int) this.tileEntity.getTileData().getDouble("ElectronicPower")) + " ELs", 121, 17, -3407821);
        this.font.draw(matrixStack, new TranslationTextComponent("electrona.xp_generator_gui.xp_levels"), 121, 26, -16777216);
        this.font.draw(matrixStack, "" + tileEntity.getTileData().getInt("XPLevels") + "", 121, 36, -13395712);
        this.font.draw(matrixStack, new TranslationTextComponent("electrona.xp_generator_gui.fuel"), 79, 37, -16777216);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bind(XP_GENERATOR_GUI);
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
        this.addButton(new Button(this.leftPos + 98, this.topPos + 60, 70, 20,
                new TranslationTextComponent("electrona.xp_generator_gui.harvest_button"), e -> {
            NetworkManager.INSTANCE.sendToServer(new ExperienceHarvestPacket());
        }));
    }
}
