package net.reikeb.electrona.entity;

import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.model.ZombieModel;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RadioactiveZombieRenderer extends AbstractZombieRenderer<RadioactiveZombie, ZombieModel<RadioactiveZombie>> {

    public RadioactiveZombieRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new ZombieModel<>(0.0F, false), new ZombieModel<>(0.5F, true), new ZombieModel<>(1.0F, true));
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(RadioactiveZombie entity) {
        return entity.getResourceLocation();
    }
}
