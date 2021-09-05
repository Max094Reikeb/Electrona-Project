package net.reikeb.electrona.guis;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.MiningMachineContainer;
import net.reikeb.electrona.tileentities.TileMiningMachine;

public class MiningMachineWindow extends AbstractWindow<MiningMachineContainer> {

    private static final ResourceLocation MINING_MACHINE_GUI = new ResourceLocation(Electrona.MODID, "textures/guis/mining_machine_gui.png");
    public TileMiningMachine tileEntity;

    public MiningMachineWindow(MiningMachineContainer container, Inventory inv, Component title) {
        super(container, inv, title, MINING_MACHINE_GUI);
        this.tileEntity = container.getTileEntity();
    }

    @Override
    protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
        this.font.draw(ms, new TranslatableComponent("gui.electrona.mining_machine.name"), 45, 6, -16777216);
        this.font.draw(ms, new TranslatableComponent("gui.electrona.generic.power"), 126, 6, -16777216);
        this.font.draw(ms, "" + ((int) tileEntity.getTileData().getDouble("ElectronicPower")) + " ELs", 126, 17, -3407821);
    }
}
