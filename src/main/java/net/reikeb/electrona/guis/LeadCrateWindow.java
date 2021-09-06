package net.reikeb.electrona.guis;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.LeadCrateContainer;
import net.reikeb.electrona.tileentities.TileLeadCrate;

public class LeadCrateWindow extends AbstractWindow<LeadCrateContainer> {

    private static final ResourceLocation LEAD_CRATE_GUI = Electrona.RL("textures/guis/lead_crate_gui.png");
    public TileLeadCrate tileEntity;

    public LeadCrateWindow(LeadCrateContainer container, Inventory inv, Component title) {
        super(container, inv, title, LEAD_CRATE_GUI);
        this.tileEntity = container.getTileEntity();
    }

    @Override
    protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
        this.font.draw(ms, new TranslatableComponent("gui.electrona.lead_crate.name"), 7, 6, -16777216);
    }
}
