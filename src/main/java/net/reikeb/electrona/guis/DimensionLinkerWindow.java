package net.reikeb.electrona.guis;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.DimensionLinkerContainer;
import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.DimensionIDPacket;
import net.reikeb.electrona.tileentities.TileDimensionLinker;

import org.lwjgl.opengl.GL11;

public class DimensionLinkerWindow extends ContainerScreen<DimensionLinkerContainer> {

    private static final ResourceLocation DIMENSION_LINKER_GUI = new ResourceLocation(Electrona.MODID, "textures/guis/dimension_linker_gui.png");
    public TileDimensionLinker tileEntity;
    TextFieldWidget dimension_id;

    public DimensionLinkerWindow(DimensionLinkerContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
        this.tileEntity = container.getTileEntity();
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);
        this.renderTooltip(ms, mouseX, mouseY);
        dimension_id.render(ms, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderLabels(MatrixStack ms, int mouseX, int mouseY) {
        this.font.draw(ms, new TranslationTextComponent("gui.electrona.dimension_linker.name"), 48, 6, -16777216);
    }

    @Override
    protected void renderBg(MatrixStack ms, float par1, int par2, int par3) {
        GL11.glColor4f(1, 1, 1, 1);
        Minecraft.getInstance().getTextureManager().bind(DIMENSION_LINKER_GUI);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        this.blit(ms, k, l, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) {
            this.minecraft.player.closeContainer();
            return true;
        }
        if (dimension_id.isFocused())
            return dimension_id.keyPressed(key, b, c);
        return super.keyPressed(key, b, c);
    }

    @Override
    public void tick() {
        super.tick();
        dimension_id.tick();
    }

    @Override
    public void removed() {
        super.removed();
        Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(false);
    }

    public String getDimensionID() {
        String id = tileEntity.getTileData().getString("dimensionID");
        return ((id.equals("")) ? "minecraft:overworld" : id);
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
        minecraft.keyboardHandler.setSendRepeatsToGui(true);
        dimension_id = new TextFieldWidget(this.font, this.leftPos + 27, this.topPos + 28, 120, 20,
                new StringTextComponent("minecraft:overworld")) {
            {
                setSuggestion(getDimensionID());
            }

            @Override
            public void insertText(String text) {
                super.insertText(text);
                if (getValue().isEmpty())
                    setSuggestion(getDimensionID());
                else
                    setSuggestion(null);
            }

            @Override
            public void moveCursorTo(int pos) {
                super.moveCursorTo(pos);
                if (getValue().isEmpty())
                    setSuggestion(getDimensionID());
                else
                    setSuggestion(null);
            }
        };
        DimensionLinkerContainer.textFieldWidget.put("text:dimension_id", dimension_id);
        dimension_id.setMaxLength(32767);
        this.children.add(this.dimension_id);
        this.addButton(new Button(this.leftPos + 123, this.topPos + 58, 45, 20,
                new StringTextComponent("Save"), e -> {
            NetworkManager.INSTANCE.sendToServer(new DimensionIDPacket());
        }));
    }
}
