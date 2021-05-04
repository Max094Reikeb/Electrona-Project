package net.reikeb.electrona.tileentities;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class TileSingularityRenderer extends TileEntityRenderer<TileSingularity> {

    public static final ResourceLocation BEAM_LOCATION = new ResourceLocation("textures/entity/beacon_beam.png");

    public TileSingularityRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    public void render(TileSingularity singularity, float n, MatrixStack matrixStack, IRenderTypeBuffer buffer, int x, int y) {
        if (singularity.getLevel() == null) return;
        double d0 = 256.0D;
        float f = MathHelper.sin((float) (0.9 * Math.PI));
        int i = MathHelper.floor((double) f * d0);
        float[] afloat = DyeColor.MAGENTA.getTextureDiffuseColors();
        long j = singularity.getLevel().getGameTime();
        BeaconTileEntityRenderer.renderBeaconBeam(matrixStack, buffer, BEAM_LOCATION, n, f, j, 0, i, afloat, 0.15F, 0.175F);
        BeaconTileEntityRenderer.renderBeaconBeam(matrixStack, buffer, BEAM_LOCATION, n, f, j, 0, -i, afloat, 0.15F, 0.175F);
    }
}
