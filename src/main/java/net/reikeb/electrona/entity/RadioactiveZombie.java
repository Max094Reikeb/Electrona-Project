package net.reikeb.electrona.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.monster.*;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.*;

import net.reikeb.electrona.init.PotionEffectInit;
import net.reikeb.electrona.misc.vm.RadioactivityFunction;

public class RadioactiveZombie extends ZombieEntity {

    public RadioactiveZombie(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return ZombieEntity.createMonsterAttributes()
                .add(Attributes.ARMOR, 2.5F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }

    @Override
    public boolean canBreakDoors() {
        return true;
    }

    @Override
    public boolean convertsInWater() {
        return false;
    }

    @Override
    public boolean isSunSensitive() {
        return false;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean flag = super.doHurtTarget(entity);
        if (flag && this.getMainHandItem().isEmpty() && entity instanceof LivingEntity) {
            float f = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            if (!RadioactivityFunction.isEntityWearingAntiRadiationSuit((LivingEntity) entity)) {
                ((LivingEntity) entity).addEffect(new EffectInstance(PotionEffectInit.RADIOACTIVITY.get(), (int) (140 * f)));
            }
        }
        return flag;
    }

    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }
}
