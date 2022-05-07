package net.reikeb.electrona.guis;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.reikeb.electrona.containers.SteelCrateContainer;
import net.reikeb.electrona.misc.Keys;

public class SteelCrateWindow extends AbstractWindow<SteelCrateContainer> {

    public SteelCrateWindow(SteelCrateContainer container, Inventory inv, Component title) {
        super(container, inv, title, Keys.CRATE_GUI);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.steel_crate.name"), 7, 6, -16777216);
    }
}
