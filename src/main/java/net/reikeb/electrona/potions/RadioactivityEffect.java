package net.reikeb.electrona.potions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

import net.reikeb.electrona.init.PotionEffectInit;

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
        double radioactivity = entity.getPersistentData().getDouble("radioactive");
        entity.getPersistentData().putDouble("radioactive", (radioactivity + 1));
        if (radioactivity > (1200 - (60 * amplifier))) {
            entity.addEffect(new EffectInstance(Effects.CONFUSION, 1400, 2));
            entity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 1400, 2));
            entity.addEffect(new EffectInstance(Effects.DIG_SLOWDOWN, 1400, 2));
            entity.addEffect(new EffectInstance(Effects.WEAKNESS, 1400, 2));
            entity.hurt(new DamageSource("radioactivity").bypassArmor(), (float) 10);
            entity.setSecondsOnFire(10);
        } else if (radioactivity > (800 - (40 * amplifier))) {
            entity.addEffect(new EffectInstance(Effects.CONFUSION, 600, 1));
            entity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 600, 1));
            entity.addEffect(new EffectInstance(Effects.DIG_SLOWDOWN, 600, 1));
            entity.hurt(new DamageSource("radioactivity").bypassArmor(), (float) 6);
        } else if (radioactivity > (300 - (20 * amplifier))) {
            entity.addEffect(new EffectInstance(Effects.CONFUSION, 300, 0));
            entity.hurt(new DamageSource("radioactivity").bypassArmor(), (float) 4);
        } else if (radioactivity > (100 - (20 * amplifier))) {
            entity.hurt(new DamageSource("radioactivity").bypassArmor(), (float) 1);
        }
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
