package net.reikeb.electrona.potions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import net.reikeb.electrona.misc.vm.RadioactivityFunction;

public class RadioactivityEffect extends MobEffect {

    public RadioactivityEffect() {
        super(MobEffectCategory.HARMFUL, -3355648);
        ResourceLocation potionIcon = new ResourceLocation("electrona:textures/mob_effect/radioactivity.png");
    }

    @Override
    public String getDescriptionId() {
        return "effect.radioactivity";
    }

    @Override
    public boolean isBeneficial() {
        return false;
    }

    @Override
    public boolean isInstantenous() {
        return false;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        RadioactivityFunction.radioactivityEffect(entity, amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
