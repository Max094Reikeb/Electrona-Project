package net.reikeb.electrona.setup.client.render;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;

import net.reikeb.electrona.tileentities.TileSingularity;

public class TileSingularityRenderer implements BlockEntityRenderer<TileSingularity> {

    public static final ResourceLocation BEAM_LOCATION = new ResourceLocation("textures/entity/beacon_beam.png");

    public TileSingularityRenderer(BlockEntityRenderDispatcher rendererDispatcher) {
        super();
    }

    public void render(TileSingularity singularity, float n, PoseStack matrixStack, MultiBufferSource buffer, int x, int y) {
        if (singularity.getLevel() == null) return;
        double d0 = 256.0D;
        float f = Mth.sin((float) (0.9 * Math.PI));
        int i = Mth.floor((double) f * d0);
        float[] afloat = DyeColor.MAGENTA.getTextureDiffuseColors();
        long j = singularity.getLevel().getGameTime();
        BeaconRenderer.renderBeaconBeam(matrixStack, buffer, BEAM_LOCATION, n, f, j, 0, i, afloat, 0.15F, 0.175F);
        BeaconRenderer.renderBeaconBeam(matrixStack, buffer, BEAM_LOCATION, n, f, j, 0, -i, afloat, 0.15F, 0.175F);
    }
}
