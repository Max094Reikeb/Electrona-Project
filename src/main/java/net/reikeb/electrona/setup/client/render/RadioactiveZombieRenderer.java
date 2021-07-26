package net.reikeb.electrona.setup.client.render;

import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.model.ZombieModel;

import net.minecraft.resources.ResourceLocation;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.entity.RadioactiveZombie;

import javax.annotation.Nullable;

public class RadioactiveZombieRenderer extends AbstractZombieRenderer<RadioactiveZombie, ZombieModel<RadioactiveZombie>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Electrona.MODID, "textures/entity/radioactive_zombie.png");

    public RadioactiveZombieRenderer(EntityRenderDispatcher rendererManager) {
        super(rendererManager, new ZombieModel<RadioactiveZombie>(0.0F, false), new ZombieModel<>(0.5F, true), new ZombieModel<>(1.0F, true));
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(RadioactiveZombie entity) {
        return TEXTURE;
    }
}
