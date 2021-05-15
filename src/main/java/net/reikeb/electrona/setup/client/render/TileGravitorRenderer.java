package net.reikeb.electrona.setup.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.*;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.tileentities.TileGravitor;

public class TileGravitorRenderer extends TileEntityRenderer<TileGravitor> {

    public static final RenderMaterial SHELL_TEXTURE = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, new ResourceLocation(Electrona.MODID, "entity/gravitor/base"));
    public static final RenderMaterial ACTIVE_SHELL_TEXTURE = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, new ResourceLocation(Electrona.MODID, "entity/gravitor/cage"));
    public static final RenderMaterial WIND_TEXTURE = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, new ResourceLocation(Electrona.MODID, "entity/gravitor/wind"));
    public static final RenderMaterial VERTICAL_WIND_TEXTURE = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, new ResourceLocation(Electrona.MODID, "entity/gravitor/wind_vertical"));
    public static final RenderMaterial OPEN_EYE_TEXTURE = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, new ResourceLocation(Electrona.MODID, "entity/gravitor/open_eye"));
    public static final RenderMaterial CLOSED_EYE_TEXTURE = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, new ResourceLocation(Electrona.MODID, "entity/gravitor/closed_eye"));
    private final ModelRenderer eye = new ModelRenderer(16, 16, 0, 0);
    private final ModelRenderer wind;
    private final ModelRenderer shell;
    private final ModelRenderer cage;

    public TileGravitorRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
        this.eye.addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, 0.01F);
        this.wind = new ModelRenderer(64, 32, 0, 0);
        this.wind.addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F);
        this.shell = new ModelRenderer(32, 16, 0, 0);
        this.shell.addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F);
        this.cage = new ModelRenderer(32, 16, 0, 0);
        this.cage.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);
    }

    public void render(TileGravitor tileGravitor, float n, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int x, int y) {
        float f = (float) tileGravitor.tickCount + n;
        if (!tileGravitor.isActive()) {
            float f5 = tileGravitor.getActiveRotation(0.0F);
            IVertexBuilder ivertexbuilder1 = SHELL_TEXTURE.buffer(renderTypeBuffer, RenderType::entitySolid);
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(f5));
            this.shell.render(matrixStack, ivertexbuilder1, x, y);
            matrixStack.popPose();
        } else {
            float f1 = tileGravitor.getActiveRotation(n) * (180F / (float) Math.PI);
            float f2 = MathHelper.sin(f * 0.1F) / 2.0F + 0.5F;
            f2 = f2 * f2 + f2;
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 0.3F + f2 * 0.2F, 0.5D);
            Vector3f vector3f = new Vector3f(0.5F, 1.0F, 0.5F);
            vector3f.normalize();
            matrixStack.mulPose(new Quaternion(vector3f, f1, true));
            this.cage.render(matrixStack, ACTIVE_SHELL_TEXTURE.buffer(renderTypeBuffer, RenderType::entityCutoutNoCull), x, y);
            matrixStack.popPose();
            int i = tileGravitor.tickCount / 66 % 3;
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            if (i == 1) {
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            } else if (i == 2) {
                matrixStack.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
            }

            IVertexBuilder ivertexbuilder = (i == 1 ? VERTICAL_WIND_TEXTURE : WIND_TEXTURE).buffer(renderTypeBuffer, RenderType::entityCutoutNoCull);
            this.wind.render(matrixStack, ivertexbuilder, x, y);
            matrixStack.popPose();
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            matrixStack.scale(0.875F, 0.875F, 0.875F);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            this.wind.render(matrixStack, ivertexbuilder, x, y);
            matrixStack.popPose();
            ActiveRenderInfo activerenderinfo = this.renderer.camera;
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 0.3F + f2 * 0.2F, 0.5D);
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            float f3 = -activerenderinfo.getYRot();
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(f3));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(activerenderinfo.getXRot()));
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            matrixStack.scale(1.3333334F, 1.3333334F, 1.3333334F);
            this.eye.render(matrixStack, (tileGravitor.isHunting() ? OPEN_EYE_TEXTURE : CLOSED_EYE_TEXTURE).buffer(renderTypeBuffer, RenderType::entityCutoutNoCull), x, y);
            matrixStack.popPose();
        }
    }
}
