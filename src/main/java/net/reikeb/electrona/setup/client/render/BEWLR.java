package net.reikeb.electrona.setup.client.render;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraftforge.common.util.Lazy;

import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.tileentities.TileGravitor;

import java.util.function.Supplier;

public class BEWLR extends BlockEntityWithoutLevelRenderer {

    private final Lazy<TileGravitor> gravitor;

    public BEWLR(Supplier<TileGravitor> gravitor) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.gravitor = Lazy.of(gravitor);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource bufferSource, int x, int y) {
        Item item = stack.getItem();
        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();

            BlockEntity blockentity;
            if (block == BlockInit.GRAVITOR.get()) {
                blockentity = this.gravitor.get();
            } else {
                return;
            }

            Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(blockentity, poseStack, bufferSource, x, y);
        }
    }

    public static BlockEntityWithoutLevelRenderer create() {
        return new BEWLR(() -> new TileGravitor(BlockPos.ZERO, Blocks.CONDUIT.defaultBlockState()));
    }
}
