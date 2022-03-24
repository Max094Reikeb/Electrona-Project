package net.reikeb.electrona.guis;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.MiningMachineContainer;

public class MiningMachineWindow extends AbstractWindow<MiningMachineContainer> {

    private static final ResourceLocation MINING_MACHINE_GUI = Electrona.RL("textures/guis/mining_machine_gui.png");

    public MiningMachineWindow(MiningMachineContainer container, Inventory inv, Component title) {
        super(container, inv, title, MINING_MACHINE_GUI);
    }

    @Override
    protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
        this.font.draw(ms, new TranslatableComponent("gui.electrona.mining_machine.name"), 6, 8, -16777216);
        this.font.draw(ms, new TranslatableComponent("gui.electrona.generic.power"), 125, 8, -16777216);
        this.font.draw(ms, "" + this.menu.getElectronicPower() + " ELs", 125, 18, -3407821);
    }
}
