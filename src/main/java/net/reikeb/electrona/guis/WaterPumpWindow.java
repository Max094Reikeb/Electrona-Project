package net.reikeb.electrona.guis;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;

import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.WaterPumpContainer;
import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.WaterPumpActivationPacket;
import net.reikeb.electrona.tileentities.TileWaterPump;

import org.lwjgl.opengl.GL11;

import java.util.concurrent.atomic.AtomicInteger;

public class WaterPumpWindow extends ContainerScreen<WaterPumpContainer> {

    private static final ResourceLocation WATER_PUMP_GUI = new ResourceLocation(Electrona.MODID, "textures/guis/water_pump_gui.png");
    public TileWaterPump tileEntity;

    /**
     * Texture position of the water
     */
    final static int WATER_XPOS = 16;
    final static int WATER_YPOS = 7;
    final static int WATER_ICON_U = 178;
    final static int WATER_ICON_V = 0;
    final static int WATER_WIDTH = 16;
    final static int WATER_HEIGHT = 52;

    public WaterPumpWindow(WaterPumpContainer container, PlayerInventory inv, ITextComponent title) {
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
        AtomicInteger currentWater = new AtomicInteger();
        tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                .ifPresent(cap -> currentWater.set(cap.getFluidInTank(1).getAmount()));
        int XposT1 = leftPos + 15;
        int XposT2 = leftPos + 32;
        int YposT1 = topPos + 6;
        int YposT2 = topPos + 59;
        String string = (currentWater.get() + " mb");
        if (mouseX > XposT1 && mouseX < XposT2 && mouseY > YposT1 && mouseY < YposT2) {
            renderTooltip(matrixStack, ITextComponent.nullToEmpty(string), mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, new TranslationTextComponent("gui.electrona.water_pump.name"), 51, 8, -16777216);
        this.font.draw(matrixStack, new TranslationTextComponent("gui.electrona.generic.storage"), 125, 6, -16777216);
        this.font.draw(matrixStack, "" + ((int) tileEntity.getTileData().getDouble("ElectronicPower")) + " EL", 125, 16, -3407821);
        this.font.draw(matrixStack, new TranslationTextComponent("gui.electrona.generic.state"), 62, 24, -16777216);
        boolean isPowered = tileEntity.getTileData().getBoolean("isOn");
        this.font.draw(matrixStack, (isPowered ? "ON" : "OFF"), 99, 24, -3407821);
        this.font.draw(matrixStack, new TranslationTextComponent("gui.electrona.generic.input"), 132, 47, -16777216);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bind(WATER_PUMP_GUI);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, k, l, 0, 0, this.imageWidth, this.imageHeight);
        // Get current water
        AtomicInteger currentWater = new AtomicInteger();
        tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                .ifPresent(cap -> currentWater.set(cap.getFluidInTank(1).getAmount()));
        // Get water progress as a double between 0 and 1
        double waterProgress = ((currentWater.get()) / 10000.0);
        int yOffsetWater = (int) ((1.0 - waterProgress) * WATER_HEIGHT);
        // Draw water bar
        Minecraft.getInstance().getTextureManager().bind(new ResourceLocation("electrona:textures/guis/purificator_gui.png"));
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
        this.addButton(new Button(this.leftPos + 65, this.topPos + 39, 50, 20,
                ITextComponent.nullToEmpty("On/Off"), e -> {
            NetworkManager.INSTANCE.sendToServer(new WaterPumpActivationPacket());
        }));
    }
}
