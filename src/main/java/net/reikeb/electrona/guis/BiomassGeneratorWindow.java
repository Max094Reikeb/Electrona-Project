package net.reikeb.electrona.guis;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.reikeb.electrona.containers.BiomassGeneratorContainer;
import net.reikeb.electrona.misc.Keys;

public class BiomassGeneratorWindow extends AbstractWindow<BiomassGeneratorContainer> {

    public BiomassGeneratorWindow(BiomassGeneratorContainer container, Inventory inv, Component title) {
        super(container, inv, title, Keys.BIOMASS_GENERATOR_GUI);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.biomass_generator.name"), 7, 7, -16777216);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.generic.storage"), 125, 5, -16777216);
        this.font.draw(matrixStack, "" + (int) this.menu.getElectronicPower() + " ELs", 125, 15, -3407821);
    }
}
