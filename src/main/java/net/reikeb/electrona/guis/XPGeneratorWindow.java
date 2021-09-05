package net.reikeb.electrona.guis;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.XPGeneratorContainer;
import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.ExperienceHarvestPacket;
import net.reikeb.electrona.tileentities.TileXPGenerator;

public class XPGeneratorWindow extends AbstractWindow<XPGeneratorContainer> {

    private static final ResourceLocation XP_GENERATOR_GUI = new ResourceLocation(Electrona.MODID, "textures/guis/xp_generator_gui.png");
    public TileXPGenerator tileEntity;

    public XPGeneratorWindow(XPGeneratorContainer container, Inventory inv, Component title) {
        super(container, inv, title, XP_GENERATOR_GUI);
        this.tileEntity = container.getTileEntity();
    }

    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
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
            renderTooltip(matrixStack, Component.nullToEmpty(string), mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.xp_generator.name"), 8, 7, -16777216);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.generic.power"), 121, 7, -16777216);
        this.font.draw(matrixStack, "" + ((int) this.tileEntity.getTileData().getDouble("ElectronicPower")) + " ELs", 121, 17, -3407821);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.xp_generator.xp_levels"), 121, 26, -16777216);
        this.font.draw(matrixStack, "" + tileEntity.getTileData().getInt("XPLevels") + "", 121, 36, -13395712);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.xp_generator.fuel"), 79, 37, -16777216);
    }

    @Override
    public void init() {
        super.init();
        this.addRenderableWidget(new Button(this.leftPos + 98, this.topPos + 60, 70, 20,
                new TranslatableComponent("gui.electrona.xp_generator.harvest_button"), e -> {
            NetworkManager.INSTANCE.sendToServer(new ExperienceHarvestPacket());
        }));
    }
}
