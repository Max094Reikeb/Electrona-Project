package net.reikeb.electrona.guis;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.BatteryContainer;

public class BatteryWindow extends AbstractWindow<BatteryContainer> {

    private static final ResourceLocation BATTERY_GUI = Electrona.RL("textures/guis/battery_gui.png");

    public BatteryWindow(BatteryContainer container, Inventory inv, Component title) {
        super(container, inv, title, BATTERY_GUI);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.battery.name"), 72, 7, -16777216);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.generic.storage"), 5, 7, -16777216);
        this.font.draw(matrixStack, "" + this.menu.getElectronicPower() + "ELs", 5, 17, -3407821);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.generic.output"), 110, 51, -16777216);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.generic.input"), 40, 51, -16777216);
    }
}
