package net.reikeb.electrona.setup.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.*;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Lazy;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.tileentities.TileGravitor;

import java.util.function.Supplier;

public class ISTER extends ItemStackTileEntityRenderer {

    private final Lazy<TileGravitor> gravitor;

    public ISTER(Supplier<TileGravitor> gravitor) {
        this.gravitor = Lazy.of(gravitor);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int x, int y) {
        Item item = stack.getItem();
        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();

            TileEntity tileentity;
            if (block == BlockInit.GRAVITOR.get()) {
                tileentity = this.gravitor.get();
            } else {
                return;
            }

            TileEntityRendererDispatcher.instance.renderItem(tileentity, matrixStack, buffer, x, y);
        }
    }

    public static ItemStackTileEntityRenderer create() {
        return new ISTER(TileGravitor::new);
    }
}
