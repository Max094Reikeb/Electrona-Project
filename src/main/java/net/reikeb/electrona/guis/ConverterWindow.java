package net.reikeb.electrona.guis;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import net.minecraftforge.energy.CapabilityEnergy;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.ConverterContainer;
import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.ConverterPacket;
import net.reikeb.electrona.tileentities.TileConverter;

import java.util.concurrent.atomic.AtomicInteger;

public class ConverterWindow extends AbstractWindow<ConverterContainer> {

    private static final ResourceLocation CONVERTER_GUI = new ResourceLocation(Electrona.MODID, "textures/guis/converter_gui.png");
    public TileConverter tileEntity;

    public ConverterWindow(ConverterContainer container, Inventory inv, Component title) {
        super(container, inv, title, CONVERTER_GUI);
        this.tileEntity = container.getTileEntity();
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        AtomicInteger energy = new AtomicInteger();
        boolean toOthers = tileEntity.getTileData().getBoolean("toOthers");
        tileEntity.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(cap -> energy.set(cap.getEnergyStored()));
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.el_converter.name"), 66, 6, -16777216);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.generic.storage"), 7, 6, -16777216);
        this.font.draw(matrixStack, "" + (toOthers ? energy.get() + " FE" : ((int) tileEntity.getTileData().getDouble("vp")) + " VPs"), 7, 16, -3407821);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.generic.input"), 77, 49, -16777216);
        this.font.draw(matrixStack, "" + tileEntity.getTileData().getBoolean("toVP") + "", 140, 16, -3407821);
        this.font.draw(matrixStack, "VP", 140, 6, -16777216);
        this.font.draw(matrixStack, "FE", 140, 26, -16777216);
        this.font.draw(matrixStack, "" + toOthers + "", 140, 36, -3407821);
    }

    @Override
    public void init() {
        super.init();
        this.addRenderableWidget(new Button(this.leftPos + 109, this.topPos + 61, 60, 20,
                new TranslatableComponent("gui.electrona.el_converter.change_button"), e -> {
            NetworkManager.INSTANCE.sendToServer(new ConverterPacket());
        }));
    }
}
