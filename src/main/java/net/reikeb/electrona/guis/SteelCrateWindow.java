package net.reikeb.electrona.guis;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.SteelCrateContainer;
import net.reikeb.electrona.tileentities.TileSteelCrate;

public class SteelCrateWindow extends AbstractWindow<SteelCrateContainer> {

    private static final ResourceLocation STEEL_CRATE_GUI = Electrona.RL("textures/guis/steel_crate_gui.png");
    public TileSteelCrate tileEntity;

    public SteelCrateWindow(SteelCrateContainer container, Inventory inv, Component title) {
        super(container, inv, title, STEEL_CRATE_GUI);
        this.tileEntity = container.getTileEntity();
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.steel_crate.name"), 7, 6, -16777216);
    }
}
