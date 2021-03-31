package net.reikeb.electrona.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import net.reikeb.electrona.init.ItemInit;

public class MechanicWingsLayer<T extends LivingEntity, M extends EntityModel<T>> extends ElytraLayer<T, M> {
    private static final ResourceLocation TEXTURE_WINGS = new ResourceLocation("electrona", "textures/mechanic_wings.png");
    private final MechanicWingsModel<T> wingsModel = new MechanicWingsModel<>();

    public MechanicWingsLayer(IEntityRenderer<T, M> p_i50942_1_) {
        super(p_i50942_1_);
    }

    @Override
    public void render(MatrixStack p_225628_1_, IRenderTypeBuffer p_225628_2_, int p_225628_3_, T p_225628_4_, float p_225628_5_, float p_225628_6_,
                       float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        ItemStack itemstack = p_225628_4_.getItemBySlot(EquipmentSlotType.CHEST);
        if (shouldRender(itemstack, p_225628_4_)) {
            p_225628_1_.pushPose();
            p_225628_1_.translate(0.0D, 0.0D, 0.125D);
            this.getParentModel().copyPropertiesTo(this.wingsModel);
            this.wingsModel.setupAnim(p_225628_4_, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_);
            IVertexBuilder ivertexbuilder = ItemRenderer.getArmorFoilBuffer(p_225628_2_, RenderType.armorCutoutNoCull(TEXTURE_WINGS), false,
                    itemstack.hasFoil());
            this.wingsModel.renderToBuffer(p_225628_1_, ivertexbuilder, p_225628_3_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            p_225628_1_.popPose();
        }
    }

    @Override
    public boolean shouldRender(ItemStack stack, T entity) {
        return stack.getItem() == ItemInit.MECHANIC_WINGS.get().getItem();
    }

    @Override
    public ResourceLocation getElytraTexture(ItemStack stack, T entity) {
        return TEXTURE_WINGS;
    }
}
