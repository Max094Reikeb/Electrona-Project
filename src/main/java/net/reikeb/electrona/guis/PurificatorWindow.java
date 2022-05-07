package net.reikeb.electrona.guis;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.reikeb.electrona.containers.PurificatorContainer;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.maxilib.utils.Utils;

public class PurificatorWindow extends AbstractWindow<PurificatorContainer> {

    /**
     * Texture position of the thermometer
     */
    final static int THERMOMETER_XPOS = 101;
    final static int THERMOMETER_YPOS = 36;
    final static int THERMOMETER_ICON_U = 194;
    final static int THERMOMETER_ICON_V = 0;
    final static int THERMOMETER_WIDTH = 6;
    final static int THERMOMETER_HEIGHT = 20;
    /**
     * Texture position of the water
     */
    final static int WATER_XPOS = 7;
    final static int WATER_YPOS = 7;
    final static int WATER_ICON_U = 178;
    final static int WATER_ICON_V = 0;
    final static int WATER_WIDTH = 16;
    final static int WATER_HEIGHT = 52;

    public PurificatorWindow(PurificatorContainer container, Inventory inv, Component title) {
        super(container, inv, title, Keys.PURIFICATOR_GUI);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        // Water level
        int XposT1 = leftPos + 6;
        int XposT2 = leftPos + 23;
        int YposT1 = topPos + 6;
        int YposT2 = topPos + 60;
        String string = (this.menu.getCurrentWater() + " mb");
        // Purifying percentage
        int currentPurify = this.menu.getCurrentPurifyingTime();
        int purifyTime = this.menu.getPurifyingTime();
        int percentage = (currentPurify * 5) / (purifyTime == 0 ? 100 : purifyTime);
        int XperT1 = leftPos + 100;
        int XperT2 = leftPos + 108;
        int YperT1 = topPos + 35;
        int YperT2 = topPos + 57;
        String perString = (percentage + "%");
        if (mouseX > XposT1 && mouseX < XposT2 && mouseY > YposT1 && mouseY < YposT2) {
            renderTooltip(matrixStack, Component.nullToEmpty(string), mouseX, mouseY);
        } else if (mouseX > XperT1 && mouseX < XperT2 && mouseY > YperT1 && mouseY < YperT2) {
            renderTooltip(matrixStack, Component.nullToEmpty(perString), mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.purificator.name"), 52, 12, -16777216);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float par1, int par2, int par3) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        Utils.bind(Keys.PURIFICATOR_GUI);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, k, l, 0, 0, this.imageWidth, this.imageHeight);
        double purifyingTime = this.menu.getPurifyingTime();
        double currentPurifyingTime = this.menu.getCurrentPurifyingTime();
        // get purifying progress as an int
        double purifyingProgress = (currentPurifyingTime * 0.05) / purifyingTime;
        int yOffset = (int) ((1 - purifyingProgress) * THERMOMETER_HEIGHT);
        // draw thermometer progress bar
        RenderSystem.setShaderTexture(0, Keys.PURIFICATOR_GUI);
        if (currentPurifyingTime > 0) {
            this.blit(matrixStack, this.leftPos + THERMOMETER_XPOS, this.topPos + THERMOMETER_YPOS + yOffset, THERMOMETER_ICON_U,
                    THERMOMETER_ICON_V + yOffset, THERMOMETER_WIDTH, THERMOMETER_HEIGHT - yOffset);
        }
        // get water progress as a double between 0 and 1
        double waterProgress = (this.menu.getCurrentWater() / 10000.0);
        int yOffsetWater = (int) ((1 - waterProgress) * WATER_HEIGHT);
        // draw water bar
        RenderSystem.setShaderTexture(0, Keys.PURIFICATOR_GUI);
        if (this.menu.getCurrentWater() > 0) {
            this.blit(matrixStack, this.leftPos + WATER_XPOS, this.topPos + WATER_YPOS + yOffsetWater, WATER_ICON_U, WATER_ICON_V + yOffsetWater,
                    WATER_WIDTH, WATER_HEIGHT - yOffsetWater);
        }
    }
}
