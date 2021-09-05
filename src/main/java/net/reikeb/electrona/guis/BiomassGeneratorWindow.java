package net.reikeb.electrona.guis;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.BiomassGeneratorContainer;
import net.reikeb.electrona.tileentities.TileBiomassGenerator;

public class BiomassGeneratorWindow extends AbstractWindow<BiomassGeneratorContainer> {

    private static final ResourceLocation BIOMASS_GENERATOR_GUI = new ResourceLocation(Electrona.MODID, "textures/guis/biomass_generator_gui.png");
    public TileBiomassGenerator tileEntity;

    public BiomassGeneratorWindow(BiomassGeneratorContainer container, Inventory inv, Component title) {
        super(container, inv, title, BIOMASS_GENERATOR_GUI);
        this.tileEntity = container.getTileEntity();
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.biomass_generator.name"), 7, 7, -16777216);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.generic.storage"), 125, 5, -16777216);
        this.font.draw(matrixStack, "" + ((int) this.tileEntity.getTileData().getDouble("ElectronicPower")) + " ELs", 125, 15, -3407821);
    }
}
