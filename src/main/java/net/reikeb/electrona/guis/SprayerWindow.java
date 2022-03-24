package net.reikeb.electrona.guis;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.SprayerContainer;

public class SprayerWindow extends AbstractWindow<SprayerContainer> {

    private static final ResourceLocation SPRAYER_GUI = Electrona.RL("textures/guis/sprayer_gui.png");

    public SprayerWindow(SprayerContainer container, Inventory inv, Component title) {
        super(container, inv, title, SPRAYER_GUI);
    }

    @Override
    protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
        this.font.draw(ms, new TranslatableComponent("gui.electrona.sprayer.name"), 19, 8, -16777216);
        this.font.draw(ms, new TranslatableComponent("gui.electrona.generic.power"), 123, 7, -16777216);
        this.font.draw(ms, "" + this.menu.getElectronicPower() + " ELs", 123, 17, -3407821);
        this.font.draw(ms, new TranslatableComponent("gui.electrona.generic.radius"), 123, 26, -16777216);
        this.font.draw(ms, "" + this.menu.getRadius() + "", 123, 36, -3407821);
    }
}
