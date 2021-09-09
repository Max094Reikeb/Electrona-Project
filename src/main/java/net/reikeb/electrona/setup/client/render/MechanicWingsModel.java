package net.reikeb.electrona.setup.client.render;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class MechanicWingsModel<T extends LivingEntity> extends HierarchicalModel<T> {

    private final ModelPart basePlate;
    private final ModelPart rightWing;
    private final ModelPart leftWing;

    public MechanicWingsModel(ModelPart model) {
        this.basePlate = model.getChild("root");
        this.rightWing = basePlate.getChild("right_wing");
        this.leftWing = basePlate.getChild("left_wing");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition basePlate = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(27, 0).addBox(-5.745F, -18.495F, -0.005F, 11.0F, 14.0F, 3.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        basePlate.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 52).addBox(-11.5F, -1.0F, 0.25F, 12.0F, 12.0F, 2.0F, new CubeDeformation(0.01F))
                .texOffs(25, 25).addBox(-10.5F, -1.0F, -1.25F, 11.0F, 24.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(-7.5F, 23.0F, -1.25F, 8.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 1.0F, -0.25F, 0.0F, 0.0F, 0.2182F));

        basePlate.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(50, 50).addBox(-0.995F, -0.995F, 0.245F, 12.0F, 12.0F, 2.0F, new CubeDeformation(0.02F))
                .texOffs(0, 0).addBox(-1.495F, -0.995F, -1.255F, 11.0F, 24.0F, 2.0F, new CubeDeformation(0.01F))
                .texOffs(27, 18).addBox(-1.495F, 23.005F, -1.255F, 8.0F, 3.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(1.0F, 1.0F, -0.25F, 0.0F, 0.0F, -0.2182F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        setupElytraAnim(entity);
        leftWing.y -= 15.0F;
        rightWing.y -= 15.0F;
        if (entity.isCrouching()) {
            basePlate.z = 3.0F;
            basePlate.y = 22.0F;
            leftWing.z = 3.0F;
            rightWing.z = 3.0F;
        } else {
            basePlate.z = 0.0F;
            basePlate.y = 20.0F;
            leftWing.z = 1.0F;
            rightWing.z = 1.0F;
        }
    }

    public void setupElytraAnim(T entity) {
        float f = 0.2617994F;
        float f1 = -0.2617994F;
        float f2 = 0.0F;
        float f3 = 0.0F;
        if (entity.isFallFlying()) {
            float f4 = 1.0F;
            Vec3 vec3 = entity.getDeltaMovement();
            if (vec3.y < 0.0D) {
                Vec3 vec31 = vec3.normalize();
                f4 = 1.0F - (float) Math.pow(-vec31.y, 1.5D);
            }

            f = f4 * 0.34906584F + (1.0F - f4) * f;
            f1 = f4 * (-(float) Math.PI / 2F) + (1.0F - f4) * f1;
        } else if (entity.isCrouching()) {
            f = 0.6981317F;
            f1 = (-(float) Math.PI / 4F);
            f2 = 3.0F;
            f3 = 0.08726646F;
        }

        this.leftWing.y = f2;
        if (entity instanceof AbstractClientPlayer abstractclientplayer) {
            abstractclientplayer.elytraRotX = (float) ((double) abstractclientplayer.elytraRotX + (double) (f - abstractclientplayer.elytraRotX) * 0.1D);
            abstractclientplayer.elytraRotY = (float) ((double) abstractclientplayer.elytraRotY + (double) (f3 - abstractclientplayer.elytraRotY) * 0.1D);
            abstractclientplayer.elytraRotZ = (float) ((double) abstractclientplayer.elytraRotZ + (double) (f1 - abstractclientplayer.elytraRotZ) * 0.1D);
            this.leftWing.xRot = abstractclientplayer.elytraRotX;
            this.leftWing.yRot = abstractclientplayer.elytraRotY;
            this.leftWing.zRot = abstractclientplayer.elytraRotZ;
        } else {
            this.leftWing.xRot = f;
            this.leftWing.zRot = f1;
            this.leftWing.yRot = f3;
        }

        this.rightWing.yRot = -this.leftWing.yRot;
        this.rightWing.y = this.leftWing.y;
        this.rightWing.xRot = this.leftWing.xRot;
        this.rightWing.zRot = -this.leftWing.zRot;
    }

    public ModelPart root() {
        return this.basePlate;
    }
}
