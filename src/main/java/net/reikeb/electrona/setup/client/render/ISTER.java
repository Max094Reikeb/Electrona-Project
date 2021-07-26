package net.reikeb.electrona.setup.client.render;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.block.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraft.item.*;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Lazy;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.tileentities.TileGravitor;

import java.util.function.Supplier;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ISTER extends BlockEntityWithoutLevelRenderer {

    private final Lazy<TileGravitor> gravitor;

    public ISTER(Supplier<TileGravitor> gravitor) {
        super(gravitor);
        this.gravitor = Lazy.of(gravitor);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack matrixStack, MultiBufferSource buffer, int x, int y) {
        Item item = stack.getItem();
        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();

            BlockEntity tileentity;
            if (block == BlockInit.GRAVITOR.get()) {
                tileentity = this.gravitor.get();
            } else {
                return;
            }

            BlockEntityRenderDispatcher.instance.renderItem(tileentity, matrixStack, buffer, x, y);
        }
    }

    public static BlockEntityWithoutLevelRenderer create() {
        return new ISTER(TileGravitor::new);
    }
}
