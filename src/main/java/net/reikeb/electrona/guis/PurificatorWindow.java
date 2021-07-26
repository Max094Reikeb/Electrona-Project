package net.reikeb.electrona.guis;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.PurificatorContainer;
import net.reikeb.electrona.tileentities.TilePurificator;

import org.lwjgl.opengl.GL11;

import java.util.concurrent.atomic.AtomicInteger;

public class PurificatorWindow extends AbstractContainerScreen<PurificatorContainer> {

    private static final ResourceLocation PURIFICATOR_GUI = new ResourceLocation(Electrona.MODID, "textures/guis/purificator_gui.png");
    public TilePurificator tileEntity;

    /**
     * Texture position of the thermometer
     */
    final static int THERMOMETER_XPOS = 98;
    final static int THERMOMETER_YPOS = 27;
    final static int THERMOMETER_ICON_U = 194;
    final static int THERMOMETER_ICON_V = 0;
    final static int THERMOMETER_WIDTH = 6;
    final static int THERMOMETER_HEIGHT = 16;

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
        // Water level
        AtomicInteger currentWater = new AtomicInteger();
        tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                .ifPresent(cap -> currentWater.set(cap.getFluidInTank(1).getAmount()));
        int XposT1 = leftPos + 6;
        int XposT2 = leftPos + 24;
        int YposT1 = topPos + 6;
        int YposT2 = topPos + 59;
        String string = (currentWater.get() + " mb");
        // Purifying percentage
        int currentPurify = tileEntity.getTileData().getInt("CurrentPurifyingTime");
        int purifyTime = tileEntity.getTileData().getInt("PurifyingTime");
        int percentage = (currentPurify * 5) / (purifyTime == 0 ? 100 : purifyTime);
        int XperT1 = leftPos + 97;
        int XperT2 = leftPos + 105;
        int YperT1 = topPos + 27;
        int YperT2 = topPos + 44;
        String perString = (percentage + "%");
        if (mouseX > XposT1 && mouseX < XposT2 && mouseY > YposT1 && mouseY < YposT2) {
            renderTooltip(matrixStack, Component.nullToEmpty(string), mouseX, mouseY);
        } else if (mouseX > XperT1 && mouseX < XperT2 && mouseY > YperT1 && mouseY < YperT2) {
            renderTooltip(matrixStack, Component.nullToEmpty(perString), mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.purificator.name"), 48, 6, -16777216);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, PURIFICATOR_GUI);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, k, l, 0, 0, this.imageWidth, this.imageHeight);
        double purifyingTime = tileEntity.getTileData().getDouble("PurifyingTime");
        double currentPurifyingTime = tileEntity.getTileData().getDouble("CurrentPurifyingTime");
        // get purifying progress as an int
        double purifyingProgress = (currentPurifyingTime * 0.05) / purifyingTime;
        int yOffset = (int) ((1 - purifyingProgress) * THERMOMETER_HEIGHT);
        // draw thermometer progress bar
        RenderSystem.setShaderTexture(0, PURIFICATOR_GUI);
        if (currentPurifyingTime > 0) {
            this.blit(matrixStack, this.leftPos + THERMOMETER_XPOS, this.topPos + THERMOMETER_YPOS + yOffset, THERMOMETER_ICON_U,
                    THERMOMETER_ICON_V + yOffset, THERMOMETER_WIDTH, THERMOMETER_HEIGHT - yOffset);
        }
        // get current water
        AtomicInteger currentWater = new AtomicInteger();
        tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                .ifPresent(cap -> currentWater.set(cap.getFluidInTank(1).getAmount()));
        // get water progress as a double between 0 and 1
        double waterProgress = (currentWater.get() / 10000.0);
        int yOffsetWater = (int) ((1 - waterProgress) * WATER_HEIGHT);
        // draw water bar
        RenderSystem.setShaderTexture(0, PURIFICATOR_GUI);
        if (currentWater.get() > 0) {
            this.blit(matrixStack, this.leftPos + WATER_XPOS, this.topPos + WATER_YPOS + yOffsetWater, WATER_ICON_U, WATER_ICON_V + yOffsetWater,
                    WATER_WIDTH, WATER_HEIGHT - yOffsetWater);
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
    public void removed() {
        super.removed();
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
    }
}
