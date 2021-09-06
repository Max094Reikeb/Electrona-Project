package net.reikeb.electrona.guis;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.CompressorContainer;
import net.reikeb.electrona.tileentities.TileCompressor;

public class CompressorWindow extends AbstractWindow<CompressorContainer> {

    private static final ResourceLocation COMPRESSOR_GUI = Electrona.RL("textures/guis/compressor_gui.png");
    public TileCompressor tileEntity;

    public CompressorWindow(CompressorContainer container, Inventory inv, Component title) {
        super(container, inv, title, COMPRESSOR_GUI);
        this.tileEntity = container.getTileEntity();
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
}
