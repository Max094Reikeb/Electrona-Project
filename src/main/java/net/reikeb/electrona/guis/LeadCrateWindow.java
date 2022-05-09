package net.reikeb.electrona.guis;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.reikeb.electrona.containers.LeadCrateContainer;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.maxilib.abs.AbstractWindow;

public class LeadCrateWindow extends AbstractWindow<LeadCrateContainer> {

    public LeadCrateWindow(LeadCrateContainer container, Inventory inv, Component title) {
        super(container, inv, title, Keys.CRATE_GUI);
    }

    @Override
    protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
        this.font.draw(ms, new TranslatableComponent("gui.electrona.lead_crate.name"), 7, 6, -16777216);
    }
}
