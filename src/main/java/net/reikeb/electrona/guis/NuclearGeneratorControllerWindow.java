package net.reikeb.electrona.guis;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.NuclearGeneratorControllerContainer;
import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.NuclearActivatePacket;
import net.reikeb.electrona.network.packets.NuclearBarStatusPacket;
import net.reikeb.electrona.utils.ElectronaUtils;

public class NuclearGeneratorControllerWindow extends AbstractWindow<NuclearGeneratorControllerContainer> {

    /**
     * Texture position of water
     */
    final static int WATER_XPOS = 7;
    final static int WATER_YPOS = 7;
    final static int WATER_ICON_U = 178;
    final static int WATER_ICON_V = 0;
    final static int WATER_WIDTH = 16;
    final static int WATER_HEIGHT = 52;
    /**
     * Texture position of warning sign
     */
    final static int WARNING_XPOS = 72;
    final static int WARNING_YPOS = 37;
    final static int WARNING_ICON_U = 196;
    final static int WARNING_ICON_V = 0;
    final static int WARNING_WIDTH = 32;
    final static int WARNING_HEIGHT = 32;
    /**
     * Texture position of the temperature bar
     */
    final static int TEMPERATURE_XPOS = 146;
    final static int TEMPERATURE_YPOS = 5;
    final static int TEMPERATURE_ICON_U = 230;
    final static int TEMPERATURE_ICON_V = 0;
    final static int TEMPERATURE_WIDTH = 7;
    final static int TEMPERATURE_HEIGHT = 40;
    /**
     * Texture position of the power bar
     */
    final static int POWER_XPOS = 160;
    final static int POWER_YPOS = 5;
    final static int POWER_ICON_U = 238;
    final static int POWER_ICON_V = 0;
    final static int POWER_WIDTH = 7;
    final static int POWER_HEIGHT = 40;
    private static final ResourceLocation NUCLEAR_GENERATOR_CONTROLLER_GUI = Electrona.RL("textures/guis/nuclear_generator_controller_gui.png");

    public NuclearGeneratorControllerWindow(NuclearGeneratorControllerContainer container, Inventory inv, Component title) {
        super(container, inv, title, NUCLEAR_GENERATOR_CONTROLLER_GUI);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        // Water level
        int XposWaterT1 = leftPos + 6;
        int XposWaterT2 = leftPos + 23;
        int YposWaterT1 = topPos + 6;
        int YposWaterT2 = topPos + 59;
        String waterLevel = (this.menu.getUnderWater() + " mb");
        // Power level
        int XposPowerT1 = leftPos + 160;
        int XposPowerT2 = leftPos + 167;
        int YposPowerT1 = topPos + 5;
        int YposPowerT2 = topPos + 45;
        String power = ("Stored: " + this.menu.getElectronicPower() + " ELs");
        // Temperature
        int currentTemp = this.menu.getTemperature();
        int XposTempT1 = leftPos + 146;
        int XposTempT2 = leftPos + 153;
        int YposTempT1 = topPos + 5;
        int YposTempT2 = topPos + 45;
        String temperature = ("Temp.: " + currentTemp + " °C");
        if (mouseX > XposTempT1 && mouseX < XposTempT2 && mouseY > YposTempT1 && mouseY < YposTempT2) {
            renderTooltip(matrixStack, Component.nullToEmpty(temperature), mouseX, mouseY);
        } else if (mouseX > XposWaterT1 && mouseX < XposWaterT2 && mouseY > YposWaterT1 && mouseY < YposWaterT2) {
            renderTooltip(matrixStack, Component.nullToEmpty(waterLevel), mouseX, mouseY);
        } else if (mouseX > XposPowerT1 && mouseX < XposPowerT2 && mouseY > YposPowerT1 && mouseY < YposPowerT2) {
            renderTooltip(matrixStack, Component.nullToEmpty(power), mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        boolean isPowered = this.menu.isPowered();
        boolean isIn = this.menu.areUBIn();
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.nuclear_generator_controller.name"), 70, 8, -16777216);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.generic.state"), 79, 24, -16777216);
        this.font.draw(matrixStack, (isPowered ? "ON" : "OFF"), 115, 24, -3407821);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.generic.bars"), 79, 34, -16777216);
        this.font.draw(matrixStack, (isIn ? "IN" : "OUT"), 115, 34, -3407821);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float par1, int par2, int par3) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        ElectronaUtils.bind(NUCLEAR_GENERATOR_CONTROLLER_GUI);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, k, l, 0, 0, this.imageWidth, this.imageHeight);
        // get water progress as a double between 0 and 1
        double waterProgress = (this.menu.getUnderWater() / 10000.0);
        int yOffsetWater = (int) ((1.0 - waterProgress) * WATER_HEIGHT);
        // draw water bar
        RenderSystem.setShaderTexture(0, NUCLEAR_GENERATOR_CONTROLLER_GUI);
        if (this.menu.getUnderWater() > 0) {
            this.blit(matrixStack, this.leftPos + WATER_XPOS, this.topPos + WATER_YPOS + yOffsetWater, WATER_ICON_U, WATER_ICON_V + yOffsetWater,
                    WATER_WIDTH, (int) (WATER_HEIGHT - yOffsetWater));
        }
        // get alert status
        boolean isAlerted = this.menu.alert();
        // draw alert sign
        RenderSystem.setShaderTexture(0, NUCLEAR_GENERATOR_CONTROLLER_GUI);
        if (isAlerted) {
            this.blit(matrixStack, this.leftPos + WARNING_XPOS, this.topPos + WARNING_YPOS, WARNING_ICON_U, WARNING_ICON_V, WARNING_WIDTH,
                    WARNING_HEIGHT);
        }
        // get current power
        int currentPower = this.menu.getElectronicPower();
        // get current power as a double between 0 and 1
        double powerProgress = (currentPower / 10000.0);
        int yOffsetPower = (int) ((1.0 - powerProgress) * POWER_HEIGHT);
        // draw power bar
        RenderSystem.setShaderTexture(0, NUCLEAR_GENERATOR_CONTROLLER_GUI);
        if (currentPower > 0) {
            this.blit(matrixStack, this.leftPos + POWER_XPOS, this.topPos + POWER_YPOS + yOffsetPower, POWER_ICON_U, POWER_ICON_V + yOffsetPower,
                    POWER_WIDTH, POWER_HEIGHT - yOffsetPower);
        }
        // get current temperature
        int currentTemp = this.menu.getTemperature();
        // get current temperature as a double between 0 and 1
        double tempProgress = (currentTemp / 3000.0);
        int yOffsetTemp = (int) ((1.0 - tempProgress) * TEMPERATURE_HEIGHT);
        // draw temperature bar
        RenderSystem.setShaderTexture(0, NUCLEAR_GENERATOR_CONTROLLER_GUI);
        if (currentTemp > 0) {
            this.blit(matrixStack, this.leftPos + TEMPERATURE_XPOS, this.topPos + TEMPERATURE_YPOS + yOffsetTemp, TEMPERATURE_ICON_U,
                    TEMPERATURE_ICON_V + yOffsetTemp, TEMPERATURE_WIDTH, TEMPERATURE_HEIGHT - yOffsetTemp);
        }
    }

    @Override
    public void init() {
        super.init();
        this.addRenderableWidget(new Button(this.leftPos + 43, this.topPos + 60, 60, 20,
                Component.nullToEmpty("In/Out"), e -> {
            NetworkManager.INSTANCE.sendToServer(new NuclearBarStatusPacket());
        }));
        this.addRenderableWidget(new Button(this.leftPos + 109, this.topPos + 60, 60, 20,
                Component.nullToEmpty("On/Off"), e -> {
            NetworkManager.INSTANCE.sendToServer(new NuclearActivatePacket());
        }));
    }
}
