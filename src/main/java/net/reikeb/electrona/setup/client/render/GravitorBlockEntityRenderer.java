package net.reikeb.electrona.setup.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.util.Mth;
import net.reikeb.electrona.blockentities.GravitorBlockEntity;
import net.reikeb.electrona.misc.Keys;

public class GravitorBlockEntityRenderer implements BlockEntityRenderer<GravitorBlockEntity> {

    public static final Material SHELL_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, Keys.GRAVITOR_BASE);
    public static final Material ACTIVE_SHELL_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, Keys.GRAVITOR_CAGE);
    public static final Material WIND_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, Keys.GRAVITOR_WIND);
    public static final Material VERTICAL_WIND_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, Keys.GRAVITOR_WIND_VERTICAL);
    public static final Material OPEN_EYE_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, Keys.GRAVITOR_OPEN_EYE);
    public static final Material CLOSED_EYE_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, Keys.GRAVITOR_CLOSED_EYE);
    private final ModelPart eye;
    private final ModelPart wind;
    private final ModelPart shell;
    private final ModelPart cage;
    private final BlockEntityRenderDispatcher renderer;

    public GravitorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.renderer = context.getBlockEntityRenderDispatcher();
        this.eye = context.bakeLayer(ModelLayers.CONDUIT_EYE);
        this.wind = context.bakeLayer(ModelLayers.CONDUIT_WIND);
        this.shell = context.bakeLayer(ModelLayers.CONDUIT_SHELL);
        this.cage = context.bakeLayer(ModelLayers.CONDUIT_CAGE);
    }

    public static LayerDefinition createEyeLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    public static LayerDefinition createWindLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("wind", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    public static LayerDefinition createShellLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("shell", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 32, 16);
    }

    public static LayerDefinition createCageLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("shell", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 32, 16);
    }

    public void render(GravitorBlockEntity gravitorBlockEntity, float n, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int x, int y) {
        float f = (float) gravitorBlockEntity.tickCount + n;
        if (!gravitorBlockEntity.isActive()) {
            float f5 = gravitorBlockEntity.getActiveRotation(0.0F);
            VertexConsumer vertexConsumer = SHELL_TEXTURE.buffer(renderTypeBuffer, RenderType::entitySolid);
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(f5));
            this.shell.render(matrixStack, vertexConsumer, x, y);
            matrixStack.popPose();
        } else {
            float f1 = gravitorBlockEntity.getActiveRotation(n) * (180F / (float) Math.PI);
            float f2 = Mth.sin(f * 0.1F) / 2.0F + 0.5F;
            f2 = f2 * f2 + f2;
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 0.3F + f2 * 0.2F, 0.5D);
            Vector3f vector3f = new Vector3f(0.5F, 1.0F, 0.5F);
            vector3f.normalize();
            matrixStack.mulPose(vector3f.rotationDegrees(f1));
            this.cage.render(matrixStack, ACTIVE_SHELL_TEXTURE.buffer(renderTypeBuffer, RenderType::entityCutoutNoCull), x, y);
            matrixStack.popPose();
            int i = gravitorBlockEntity.tickCount / 66 % 3;
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            if (i == 1) {
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            } else if (i == 2) {
                matrixStack.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
            }

            VertexConsumer vertexConsumer1 = (i == 1 ? VERTICAL_WIND_TEXTURE : WIND_TEXTURE).buffer(renderTypeBuffer, RenderType::entityCutoutNoCull);
            this.wind.render(matrixStack, vertexConsumer1, x, y);
            matrixStack.popPose();
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            matrixStack.scale(0.875F, 0.875F, 0.875F);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            this.wind.render(matrixStack, vertexConsumer1, x, y);
            matrixStack.popPose();
            Camera camera = this.renderer.camera;
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 0.3F + f2 * 0.2F, 0.5D);
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            float f3 = -camera.getYRot();
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(f3));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(camera.getXRot()));
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            matrixStack.scale(1.3333334F, 1.3333334F, 1.3333334F);
            this.eye.render(matrixStack, (gravitorBlockEntity.isHunting() ? OPEN_EYE_TEXTURE : CLOSED_EYE_TEXTURE).buffer(renderTypeBuffer, RenderType::entityCutoutNoCull), x, y);
            matrixStack.popPose();
        }
    }
}
