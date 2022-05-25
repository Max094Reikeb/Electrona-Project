package net.reikeb.electrona.entity;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.init.PotionEffectInit;
import net.reikeb.electrona.misc.vm.RadioactivityFunction;

public class RadioactiveZombie extends Zombie {

    public RadioactiveZombie(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createMonsterAttributes()
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
        if (flag && this.getMainHandItem().isEmpty() && (entity instanceof LivingEntity livingEntity)) {
            float f = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            if (!RadioactivityFunction.isEntityWearingAntiRadiationSuit(livingEntity)) {
                livingEntity.addEffect(new MobEffectInstance(PotionEffectInit.RADIOACTIVITY.get(), (int) (140 * f)));
            }
        }
        return flag;
    }

    protected ItemStack getSkull() {
        return new ItemStack(ItemInit.RADIOACTIVE_ZOMBIE_HEAD_ITEM.get());
    }
}
