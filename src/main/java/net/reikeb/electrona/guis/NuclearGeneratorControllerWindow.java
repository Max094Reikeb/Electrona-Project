package net.reikeb.electrona.guis;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;

import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.NuclearGeneratorControllerContainer;
import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.*;
import net.reikeb.electrona.tileentities.*;

import org.lwjgl.opengl.GL11;

import java.util.concurrent.atomic.AtomicInteger;

public class NuclearGeneratorControllerWindow extends ContainerScreen<NuclearGeneratorControllerContainer> {

    private static final ResourceLocation NUCLEAR_GENERATOR_CONTROLLER_GUI = new ResourceLocation(Electrona.MODID, "textures/guis/nuclear_generator_controller_gui.png");
    public TileNuclearGeneratorController tileEntity;
    public TileEntity underTileEntity;

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

    public NuclearGeneratorControllerWindow(NuclearGeneratorControllerContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
        this.tileEntity = container.getTileEntity();
        this.underTileEntity = inv.player.level.getBlockEntity(new BlockPos(tileEntity.getBlockPos().getX(),
                (tileEntity.getBlockPos().getY() - 1), tileEntity.getBlockPos().getZ()));
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        // Water level
        AtomicInteger currentWater = new AtomicInteger();
        if (underTileEntity instanceof TileCooler)
            underTileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                    .ifPresent(cap -> currentWater.set(cap.getFluidInTank(1).getAmount()));
        int XposWaterT1 = leftPos + 6;
        int XposWaterT2 = leftPos + 23;
        int YposWaterT1 = topPos + 6;
        int YposWaterT2 = topPos + 59;
        String waterLevel = (currentWater.get() + " mb");
        // Power level
        int currentPower = (int) tileEntity.getTileData().getDouble("ElectronicPower");
        int XposPowerT1 = leftPos + 160;
        int XposPowerT2 = leftPos + 167;
        int YposPowerT1 = topPos + 5;
        int YposPowerT2 = topPos + 45;
        String power = ("Stored: " + currentPower + " ELs");
        // Temperature
        int currentTemp = tileEntity.getTileData().getInt("temperature");
        int XposTempT1 = leftPos + 146;
        int XposTempT2 = leftPos + 153;
        int YposTempT1 = topPos + 5;
        int YposTempT2 = topPos + 45;
        String temperature = ("Temp.: " + currentTemp + " °C");
        if (mouseX > XposTempT1 && mouseX < XposTempT2 && mouseY > YposTempT1 && mouseY < YposTempT2) {
            renderTooltip(matrixStack, ITextComponent.nullToEmpty(temperature), mouseX, mouseY);
        } else if (mouseX > XposWaterT1 && mouseX < XposWaterT2 && mouseY > YposWaterT1 && mouseY < YposWaterT2) {
            renderTooltip(matrixStack, ITextComponent.nullToEmpty(waterLevel), mouseX, mouseY);
        } else if (mouseX > XposPowerT1 && mouseX < XposPowerT2 && mouseY > YposPowerT1 && mouseY < YposPowerT2) {
            renderTooltip(matrixStack, ITextComponent.nullToEmpty(power), mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        boolean isPowered = tileEntity.getTileData().getBoolean("powered");
        boolean isIn = tileEntity.getTileData().getBoolean("UBIn");
        this.font.draw(matrixStack, new TranslationTextComponent("gui.electrona.nuclear_generator_controller.name"), 70, 8, -16777216);
        this.font.draw(matrixStack, new TranslationTextComponent("gui.electrona.generic.state"), 79, 24, -16777216);
        this.font.draw(matrixStack, (isPowered ? "ON" : "OFF"), 115, 24, -3407821);
        this.font.draw(matrixStack, new TranslationTextComponent("gui.electrona.generic.bars"), 79, 34, -16777216);
        this.font.draw(matrixStack, (isIn ? "IN" : "OUT"), 115, 34, -3407821);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bind(NUCLEAR_GENERATOR_CONTROLLER_GUI);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, k, l, 0, 0, this.imageWidth, this.imageHeight);
        // get current water
        AtomicInteger currentWater = new AtomicInteger();
        if (underTileEntity instanceof TileCooler)
            underTileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                    .ifPresent(cap -> currentWater.set(cap.getFluidInTank(1).getAmount()));
        // get water progress as a double between 0 and 1
        double waterProgress = (currentWater.get() / 10000.0);
        int yOffsetWater = (int) ((1.0 - waterProgress) * WATER_HEIGHT);
        // draw water bar
        Minecraft.getInstance().getTextureManager().bind(NUCLEAR_GENERATOR_CONTROLLER_GUI);
        if (currentWater.get() > 0) {
            this.blit(matrixStack, this.leftPos + WATER_XPOS, this.topPos + WATER_YPOS + yOffsetWater, WATER_ICON_U, WATER_ICON_V + yOffsetWater,
                    WATER_WIDTH, (int) (WATER_HEIGHT - yOffsetWater));
        }
        // get alert status
        boolean isAlerted = tileEntity.getTileData().getBoolean("alert");
        // draw alert sign
        Minecraft.getInstance().getTextureManager().bind(NUCLEAR_GENERATOR_CONTROLLER_GUI);
        if (isAlerted) {
            this.blit(matrixStack, this.leftPos + WARNING_XPOS, this.topPos + WARNING_YPOS, WARNING_ICON_U, WARNING_ICON_V, WARNING_WIDTH,
                    WARNING_HEIGHT);
        }
        // get current power
        int currentPower = (int) tileEntity.getTileData().getDouble("ElectronicPower");
        // get current power as a double between 0 and 1
        double powerProgress = (currentPower / 10000.0);
        int yOffsetPower = (int) ((1.0 - powerProgress) * POWER_HEIGHT);
        // draw power bar
        Minecraft.getInstance().getTextureManager().bind(NUCLEAR_GENERATOR_CONTROLLER_GUI);
        if (currentPower > 0) {
            this.blit(matrixStack, this.leftPos + POWER_XPOS, this.topPos + POWER_YPOS + yOffsetPower, POWER_ICON_U, POWER_ICON_V + yOffsetPower,
                    POWER_WIDTH, POWER_HEIGHT - yOffsetPower);
        }
        // get current temperature
        int currentTemp = tileEntity.getTileData().getInt("temperature");
        // get current temperature as a double between 0 and 1
        double tempProgress = (currentTemp / 3000.0);
        int yOffsetTemp = (int) ((1.0 - tempProgress) * TEMPERATURE_HEIGHT);
        // draw temperature bar
        Minecraft.getInstance().getTextureManager().bind(NUCLEAR_GENERATOR_CONTROLLER_GUI);
        if (currentTemp > 0) {
            this.blit(matrixStack, this.leftPos + TEMPERATURE_XPOS, this.topPos + TEMPERATURE_YPOS + yOffsetTemp, TEMPERATURE_ICON_U,
                    TEMPERATURE_ICON_V + yOffsetTemp, TEMPERATURE_WIDTH, TEMPERATURE_HEIGHT - yOffsetTemp);
        }
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
        this.addButton(new Button(this.leftPos + 43, this.topPos + 60, 60, 20,
                ITextComponent.nullToEmpty("In/Out"), e -> {
            NetworkManager.INSTANCE.sendToServer(new NuclearBarStatusPacket());
        }));
        this.addButton(new Button(this.leftPos + 109, this.topPos + 60, 60, 20,
                ITextComponent.nullToEmpty("On/Off"), e -> {
            NetworkManager.INSTANCE.sendToServer(new NuclearActivatePacket());
        }));
    }
}
