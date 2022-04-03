package net.reikeb.electrona.setup.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.misc.Keys;

public class MechanicWingsLayer<T extends LivingEntity, M extends EntityModel<T>> extends ElytraLayer<T, M> {

    public static ModelLayerLocation MECHANIC_WINGS_LAYER = new ModelLayerLocation(Keys.PLAYER, "mechanic_wings");
    private final MechanicWingsModel<T> wingsModel;

    public MechanicWingsLayer(RenderLayerParent<T, M> p_i50942_1_, EntityModelSet p_i50942_2_) {
        super(p_i50942_1_, p_i50942_2_);

        wingsModel = new MechanicWingsModel<>(p_i50942_2_.bakeLayer(MECHANIC_WINGS_LAYER));
    }

    @Override
    public void render(PoseStack p_225628_1_, MultiBufferSource p_225628_2_, int p_225628_3_, T p_225628_4_, float p_225628_5_, float p_225628_6_,
                       float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        ItemStack itemstack = p_225628_4_.getItemBySlot(EquipmentSlot.CHEST);
        if (shouldRender(itemstack, p_225628_4_)) {
            p_225628_1_.pushPose();
            p_225628_1_.translate(0.0D, 0.0D, 0.125D);
            this.getParentModel().copyPropertiesTo(this.wingsModel);
            this.wingsModel.setupAnim(p_225628_4_, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_);
            VertexConsumer ivertexbuilder = ItemRenderer.getArmorFoilBuffer(p_225628_2_, RenderType.armorCutoutNoCull(Keys.MECHANIC_WINGS), false,
                    itemstack.hasFoil());
            this.wingsModel.renderToBuffer(p_225628_1_, ivertexbuilder, p_225628_3_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            p_225628_1_.popPose();
        }
    }

    @Override
    public boolean shouldRender(ItemStack stack, T entity) {
        return stack.getItem() == ItemInit.MECHANIC_WINGS.get();
    }

    @Override
    public ResourceLocation getElytraTexture(ItemStack stack, T entity) {
        return Keys.MECHANIC_WINGS;
    }
}
