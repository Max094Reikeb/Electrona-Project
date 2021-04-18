package net.reikeb.electrona.potions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.*;
import net.minecraft.util.ResourceLocation;

import net.reikeb.electrona.misc.vm.RadioactivityFunction;

public class RadioactivityEffect extends Effect {

    public RadioactivityEffect() {
        super(EffectType.HARMFUL, -3355648);
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
    public boolean shouldRenderInvText(EffectInstance effect) {
        return true;
    }

    @Override
    public boolean shouldRender(EffectInstance effect) {
        return true;
    }

    @Override
    public boolean shouldRenderHUD(EffectInstance effect) {
        return true;
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
