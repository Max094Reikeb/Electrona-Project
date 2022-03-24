package net.reikeb.electrona.guis;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.NuclearBombContainer;

public class NuclearBombWindow extends AbstractWindow<NuclearBombContainer> {

    private static final ResourceLocation NUCLEAR_BOMB_GUI = Electrona.RL("textures/guis/nuclear_bomb_gui.png");

    public NuclearBombWindow(NuclearBombContainer container, Inventory inv, Component title) {
        super(container, inv, title, NUCLEAR_BOMB_GUI);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.nuclear_bomb.name"), 55, 12, -16777216);
    }
}
