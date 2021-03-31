package net.reikeb.electrona.client.render;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.entity.model.ElytraModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class MechanicWingsModel<T extends LivingEntity> extends ElytraModel<T> {
    private final ModelRenderer root;

    public MechanicWingsModel() {
        this.texWidth = 128;
        this.texHeight = 128;
        root = new ModelRenderer(this);
        root.texOffs(27, 0).addBox(-5.745F, -18.495F, -0.005F, 11.0F, 14.0F, 3.0F, 0.01F, false);
        // The position and rotation of both wings is calculated by
        // ElytraLayer#setupAnim
        this.leftWing = new ModelRenderer(this);
        leftWing.texOffs(50, 50).addBox(-1.5F, -0.995F, 0.245F, 12.0F, 12.0F, 2.0F, 0.02F, false);
        leftWing.texOffs(0, 0).addBox(-1.495F, -0.995F, -1.255F, 11.0F, 24.0F, 2.0F, 0.01F, false);
        leftWing.texOffs(27, 18).addBox(-1.495F, 23.005F, -1.255F, 8.0F, 3.0F, 2.0F, 0.01F, false);
        this.rightWing = new ModelRenderer(this);
        // this.rightWing.mirror = true;
        rightWing.texOffs(0, 52).addBox(-11.5F, -1.0F, 0.25F, 12.0F, 12.0F, 2.0F, 0.01F, false);
        rightWing.texOffs(25, 25).addBox(-10.5F, -1.0F, -1.25F, 11.0F, 24.0F, 2.0F, 0.0F, false);
        rightWing.texOffs(0, 27).addBox(-7.5F, 23.0F, -1.25F, 8.0F, 3.0F, 2.0F, 0.0F, false);
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
    protected Iterable<ModelRenderer> bodyParts() {
        return ImmutableList.of(this.leftWing, this.rightWing, this.root);
    }
}
