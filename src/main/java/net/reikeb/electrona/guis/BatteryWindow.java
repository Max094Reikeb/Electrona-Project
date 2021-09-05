package net.reikeb.electrona.guis;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.BatteryContainer;
import net.reikeb.electrona.tileentities.TileBattery;

public class BatteryWindow extends AbstractWindow<BatteryContainer> {

    private static final ResourceLocation BATTERY_GUI = new ResourceLocation(Electrona.MODID, "textures/guis/battery_gui.png");
    public TileBattery tileEntity;

    public BatteryWindow(BatteryContainer container, Inventory inv, Component title) {
        super(container, inv, title, BATTERY_GUI);
        this.tileEntity = container.getTileEntity();
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.battery.name"), 72, 7, -16777216);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.generic.storage"), 5, 7, -16777216);
        this.font.draw(matrixStack, "" + ((int) this.tileEntity.getTileData().getDouble("ElectronicPower")) + "ELs", 5, 17, -3407821);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.generic.output"), 110, 51, -16777216);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.generic.input"), 40, 51, -16777216);
    }
}
