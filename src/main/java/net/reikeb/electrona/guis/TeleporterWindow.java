package net.reikeb.electrona.guis;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;

import net.reikeb.electrona.containers.TeleporterContainer;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.TeleporterAutoDeletePacket;
import net.reikeb.electrona.network.packets.TeleporterLinkPacket;

public class TeleporterWindow extends AbstractWindow<TeleporterContainer> {

    public TeleporterWindow(TeleporterContainer container, Inventory inv, Component title) {
        super(container, inv, title, Keys.TELEPORTER_GUI);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.teleporter.name"), 62, 8, -16777216);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.teleporter.link"), 136, 8, -16777216);
        this.font.draw(matrixStack, "" + this.menu.getTeleportX() + "", 135, 19, -13408513);
        this.font.draw(matrixStack, "" + this.menu.getTeleportY() + "", 135, 28, -13408513);
        this.font.draw(matrixStack, "" + this.menu.getTeleportZ() + "", 135, 38, -13408513);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.generic.storage"), 5, 7, -16777216);
        this.font.draw(matrixStack, "" + (int) this.menu.getElectronicPower() + " ELs", 5, 18, -3407821);
        this.font.draw(matrixStack, new TranslatableComponent("gui.electrona.teleporter.auto_deletion"), 6, 28, -16777216);
        this.font.draw(matrixStack, "" + this.menu.isAutoDelete() + "", 5, 38, -3407821);
    }

    @Override
    public void init() {
        super.init();
        this.addRenderableWidget(new Button(this.leftPos + 89, this.topPos + 60, 70, 20,
                new TranslatableComponent("gui.electrona.teleporter.transfer_button"), e -> {
            NetworkManager.INSTANCE.sendToServer(new TeleporterLinkPacket());
        }));

        this.addRenderableWidget(new Button(this.leftPos + 7, this.topPos + 60, 40, 20,
                new TranslatableComponent("gui.electrona.teleporter.auto_button"), e -> {
            NetworkManager.INSTANCE.sendToServer(new TeleporterAutoDeletePacket());
        }));
    }
}
