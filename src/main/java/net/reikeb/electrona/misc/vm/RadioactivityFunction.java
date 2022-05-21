package net.reikeb.electrona.misc.vm;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.reikeb.electrona.entity.RadioactiveZombie;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.init.PotionEffectInit;
import net.reikeb.electrona.misc.DamageSources;
import net.reikeb.maxilib.utils.Utils;

public class RadioactivityFunction {

    /**
     * Method to get if the entity given is wearing the whole Anti-Radiation Suit
     *
     * @param entity The entity we're checking
     * @return boolean If the entity is wearing the full suit
     */
    public static boolean isEntityWearingAntiRadiationSuit(LivingEntity entity) {
        return ((entity.getItemBySlot(EquipmentSlot.HEAD).getItem() == ItemInit.ANTI_RADIATION_HELMET.get())
                && (entity.getItemBySlot(EquipmentSlot.CHEST).getItem() == ItemInit.ANTI_RADIATION_CHESTPLATE.get())
                && (entity.getItemBySlot(EquipmentSlot.LEGS).getItem() == ItemInit.ANTI_RADIATION_LEGGINGS.get())
                && (entity.getItemBySlot(EquipmentSlot.FEET).getItem() == ItemInit.ANTI_RADIATION_BOOTS.get()));
    }

    /**
     * Method to get if the entity given is wearing the whole Lead Armor
     *
     * @param entity The entity we're checking
     * @return boolean If the entity is wearing the full armor
     */
    public static boolean isEntityWearingLeadArmor(LivingEntity entity) {
        return ((entity.getItemBySlot(EquipmentSlot.HEAD).getItem() == ItemInit.LEAD_HELMET.get())
                && (entity.getItemBySlot(EquipmentSlot.CHEST).getItem() == ItemInit.LEAD_CHESTPLATE.get())
                && (entity.getItemBySlot(EquipmentSlot.LEGS).getItem() == ItemInit.LEAD_LEGGINGS.get())
                && (entity.getItemBySlot(EquipmentSlot.FEET).getItem() == ItemInit.LEAD_BOOTS.get()));
    }

    /**
     * Applies Radioactivity effect to the entity if it has radioactive item in his inventory
     *
     * @param level    The level the entity is in
     * @param entity   The entity who has the item
     * @param duration The duration of the given effect
     * @param power    The power of the give effect
     */
    public static void radioactiveItemInInventory(Level level, Entity entity, int duration, int power) {
        Utils.forEntitiesInRadius(level, entity.blockPosition(), (int) (10 / 2d), (worldEntity -> {
            if ((worldEntity instanceof Skeleton) || (worldEntity instanceof RadioactiveZombie)) return;
            if (!(entity instanceof LivingEntity livingEntity)) return;
            if (worldEntity instanceof Player player) {
                if (player.isCreative()) return;
            }
            if (!isEntityWearingAntiRadiationSuit(livingEntity)) {
                boolean entityWearsLeadArmor = isEntityWearingLeadArmor(livingEntity);
                (entityWearsLeadArmor ? livingEntity : worldEntity).addEffect(new MobEffectInstance(PotionEffectInit.RADIOACTIVITY.get(), duration, power));
            }
        }));
    }

    /**
     * Applies Radioactivity effect to the entity if it steps on a radioactive block
     *
     * @param entity The entity that steps on the radioactive block
     */
    public static void stepOnRadioactiveBlock(Entity entity) {
        if ((!(entity instanceof LivingEntity livingEntity)) || (entity instanceof Skeleton)
                || (entity instanceof RadioactiveZombie)) return;
        if (entity instanceof Player player) {
            if (player.isCreative()) return;
        }
        if (!isEntityWearingAntiRadiationSuit(livingEntity)) {
            livingEntity.addEffect(new MobEffectInstance(PotionEffectInit.RADIOACTIVITY.get(), 200, 2));
        }
    }

    /**
     * This method applies the side effects of the radioactivity
     *
     * @param entity    The entity that has the effect
     * @param amplifier The amplifier of the effect
     */
    public static void radioactivityEffect(LivingEntity entity, int amplifier) {
        int radioactivity = entity.getPersistentData().getInt("radioactive");
        entity.getPersistentData().putInt("radioactive", (radioactivity + 1));
        if (radioactivity > (1200 - (60 * amplifier))) {
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 1400, 2));
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1400, 2));
            entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 1400, 2));
            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1400, 2));
            entity.hurt(DamageSources.RADIOACTIVITY.bypassArmor(), (float) 10);
            entity.setSecondsOnFire(10);
        } else if (radioactivity > (800 - (40 * amplifier))) {
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 600, 1));
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600, 1));
            entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 600, 1));
            entity.hurt(DamageSources.RADIOACTIVITY.bypassArmor(), (float) 6);
        } else if (radioactivity > (300 - (20 * amplifier))) {
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 300, 0));
            entity.hurt(DamageSources.RADIOACTIVITY.bypassArmor(), (float) 4);
        } else if (radioactivity > (100 - (20 * amplifier))) {
            entity.hurt(DamageSources.RADIOACTIVITY.bypassArmor(), (float) 1);
        }
    }
}
