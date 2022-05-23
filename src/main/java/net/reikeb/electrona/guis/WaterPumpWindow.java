package net.reikeb.electrona.guis;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.reikeb.electrona.containers.WaterPumpContainer;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.WaterPumpActivationPacket;
import net.reikeb.maxilib.abs.AbstractWindow;
import net.reikeb.maxilib.utils.Utils;

public class WaterPumpWindow extends AbstractWindow<WaterPumpContainer> {

    /**
     * Texture position of the water
     */
    final static int WATER_XPOS = 16;
    final static int WATER_YPOS = 7;
    final static int WATER_ICON_U = 178;
    final static int WATER_ICON_V = 0;
    final static int WATER_WIDTH = 16;
    final static int WATER_HEIGHT = 52;

    public WaterPumpWindow(WaterPumpContainer container, Inventory inv, Component title) {
        super(container, inv, title, Keys.WATER_PUMP_GUI);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        int XposT1 = leftPos + 15;
        int XposT2 = leftPos + 32;
        int YposT1 = topPos + 6;
        int YposT2 = topPos + 59;
        String string = (this.menu.getWaterLevel() + " mb");
        if (mouseX > XposT1 && mouseX < XposT2 && mouseY > YposT1 && mouseY < YposT2) {
            renderTooltip(matrixStack, Component.nullToEmpty(string), mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.water_pump.name"), 51, 8, -16777216);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.generic.storage"), 125, 6, -16777216);
        this.font.draw(matrixStack, "" + (int) this.menu.getEnergy() + " EL", 125, 16, -3407821);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.generic.state"), 62, 24, -16777216);
        this.font.draw(matrixStack, (this.menu.isOn() ? "ON" : "OFF"), 99, 24, -3407821);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.generic.input"), 132, 47, -16777216);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float par1, int par2, int par3) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        Utils.bind(Keys.WATER_PUMP_GUI);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, k, l, 0, 0, this.imageWidth, this.imageHeight);
        // Get water progress as a double between 0 and 1
        double waterProgress = (this.menu.getWaterLevel() / 10000.0);
        int yOffsetWater = (int) ((1.0 - waterProgress) * WATER_HEIGHT);
        // Draw water bar
        Utils.bind(Keys.PURIFICATOR_GUI);
        if (this.menu.getWaterLevel() > 0) {
            this.blit(matrixStack, this.leftPos + WATER_XPOS, this.topPos + WATER_YPOS + yOffsetWater, WATER_ICON_U, WATER_ICON_V + yOffsetWater,
                    WATER_WIDTH, WATER_HEIGHT - yOffsetWater);
        }
    }

    @Override
    public void init() {
        super.init();
        this.addRenderableWidget(new Button(this.leftPos + 65, this.topPos + 39, 50, 20,
                Component.nullToEmpty("On/Off"), e -> {
            NetworkManager.INSTANCE.sendToServer(new WaterPumpActivationPacket());
        }));
    }
}
