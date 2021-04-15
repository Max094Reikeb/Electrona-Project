package net.reikeb.electrona.guis;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.MiningMachineContainer;
import net.reikeb.electrona.tileentities.TileMiningMachine;

import org.lwjgl.opengl.GL11;

public class MiningMachineWindow extends ContainerScreen<MiningMachineContainer> {

    private static final ResourceLocation MINING_MACHINE_GUI = new ResourceLocation(Electrona.MODID, "textures/guis/mining_machine_gui.png");
    public TileMiningMachine tileEntity;

    public MiningMachineWindow(MiningMachineContainer container, PlayerInventory inv, ITextComponent title) {
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
        this.font.draw(ms, new TranslationTextComponent("gui.electrona.mining_machine.name"), 45, 6, -16777216);
        this.font.draw(ms, new TranslationTextComponent("gui.electrona.generic.power"), 126, 6, -16777216);
        this.font.draw(ms, "" + ((int) tileEntity.getTileData().getDouble("ElectronicPower")) + " ELs", 126, 17, -3407821);
    }

    protected void renderBg(MatrixStack ms, float par1, int par2, int par3) {
        GL11.glColor4f(1, 1, 1, 1);
        Minecraft.getInstance().getTextureManager().bind(MINING_MACHINE_GUI);
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
        super.removed();
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
    }
}
