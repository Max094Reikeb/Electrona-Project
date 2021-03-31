package net.reikeb.electrona.potions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.potion.*;
import net.minecraft.util.ResourceLocation;

import net.reikeb.electrona.init.PotionEffectInit;
import net.reikeb.electrona.misc.vm.RadioactivityFunction;

import java.util.Collection;

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
    public void removeAttributeModifiers(LivingEntity entity, AttributeModifierManager attributeMapIn, int amplifier) {
        Collection<EffectInstance> effects = entity.getActiveEffects();
        for (EffectInstance effect : effects) {
            if (effect.getEffect() == PotionEffectInit.RADIOACTIVITY.get()) {
                entity.getPersistentData().putDouble("radioactive", 0);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
