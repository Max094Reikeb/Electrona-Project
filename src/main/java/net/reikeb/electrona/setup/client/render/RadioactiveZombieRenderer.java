package net.reikeb.electrona.setup.client.render;

import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import net.reikeb.electrona.entity.RadioactiveZombie;
import net.reikeb.electrona.misc.Keys;

import javax.annotation.Nullable;

public class RadioactiveZombieRenderer extends AbstractZombieRenderer<RadioactiveZombie, ZombieModel<RadioactiveZombie>> {

    public RadioactiveZombieRenderer(EntityRendererProvider.Context context) {
        this(context, ModelLayers.ZOMBIE, ModelLayers.ZOMBIE_INNER_ARMOR, ModelLayers.ZOMBIE_OUTER_ARMOR);
    }

    public RadioactiveZombieRenderer(EntityRendererProvider.Context context, ModelLayerLocation layerLocation1, ModelLayerLocation layerLocation2, ModelLayerLocation layerLocation3) {
        super(context, new ZombieModel<>(context.bakeLayer(layerLocation1)), new ZombieModel<>(context.bakeLayer(layerLocation2)), new ZombieModel<>(context.bakeLayer(layerLocation3)));
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(RadioactiveZombie entity) {
        return Keys.RADIOACTIVE_ZOMBIE;
    }
}
