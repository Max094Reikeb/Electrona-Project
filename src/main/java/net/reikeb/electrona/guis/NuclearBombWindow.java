package net.reikeb.electrona.guis;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.reikeb.electrona.containers.NuclearBombContainer;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.maxilib.abs.AbstractWindow;

public class NuclearBombWindow extends AbstractWindow<NuclearBombContainer> {

    public NuclearBombWindow(NuclearBombContainer container, Inventory inv, Component title) {
        super(container, inv, title, Keys.NUCLEAR_BOMB_GUI);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.nuclear_bomb.name"), 55, 12, -16777216);
    }
}
