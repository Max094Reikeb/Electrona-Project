package net.reikeb.electrona.setup.client.render;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

public class MechanicWingsModel<T extends LivingEntity> extends ElytraModel<T> {

    private final ModelPart root;

    public MechanicWingsModel(ModelPart root) {
        super(createLayer().bakeRoot());
        this.root = root.getChild("root");
        this.rightWing = root.getChild("right_wing");
        this.leftWing = root.getChild("left_wing");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(27, 0).addBox(-5.745F, -18.495F, -0.005F, 11.0F, 14.0F, 3.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        root.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 52).addBox(-11.5F, -1.0F, 0.25F, 12.0F, 12.0F, 2.0F, new CubeDeformation(0.01F))
                .texOffs(25, 25).addBox(-10.5F, -1.0F, -1.25F, 11.0F, 24.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(-7.5F, 23.0F, -1.25F, 8.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -17.0F, -0.25F, 0.0F, 0.0F, 0.2182F));

        root.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(50, 50).addBox(-0.995F, -0.995F, 0.245F, 12.0F, 12.0F, 2.0F, new CubeDeformation(0.02F))
                .texOffs(0, 0).addBox(-1.495F, -0.995F, -1.255F, 11.0F, 24.0F, 2.0F, new CubeDeformation(0.01F))
                .texOffs(27, 18).addBox(-1.495F, 23.005F, -1.255F, 8.0F, 3.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(1.0F, -17.0F, -0.25F, 0.0F, 0.0F, -0.2182F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        leftWing.x -= 2;
        rightWing.x += 2;
        leftWing.y += 2.5F;
        rightWing.y += 2.5F;
        if (entity.isCrouching()) {
            root.z = 3.0F;
            root.y = 22.0F;
            leftWing.z = 3.0F;
            rightWing.z = 3.0F;
        } else {
            root.z = 0.0F;
            root.y = 20.0F;
            leftWing.z = 1.0F;
            rightWing.z = 1.0F;
        }
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.leftWing, this.rightWing, this.root);
    }
}
