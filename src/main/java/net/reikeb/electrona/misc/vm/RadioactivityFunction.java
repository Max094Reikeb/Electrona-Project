package net.reikeb.electrona.misc.vm;

import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import net.reikeb.electrona.init.*;

import java.util.*;
import java.util.stream.Collectors;

public class RadioactivityFunction {

    /**
     * Method to get if the entity given is wearing the whole Anti-Radiation Suit
     *
     * @param entity The entity we're checking
     * @return boolean If the entity is wearing the full suit
     */
    public static boolean isEntityWearingAntiRadiationSuit(LivingEntity entity) {
        return ((entity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ItemInit.ANTI_RADIATION_HELMET.get())
                && (entity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ItemInit.ANTI_RADIATION_CHESTPLATE.get())
                && (entity.getItemBySlot(EquipmentSlotType.LEGS).getItem() == ItemInit.ANTI_RADIATION_LEGGINGS.get())
                && (entity.getItemBySlot(EquipmentSlotType.FEET).getItem() == ItemInit.ANTI_RADIATION_BOOTS.get()));
    }

    /**
     * Method to get if the entity given is wearing the whole Lead Armor
     *
     * @param entity The entity we're checking
     * @return boolean If the entity is wearing the full armor
     */
    public static boolean isEntityWearingLeadArmor(LivingEntity entity) {
        return ((entity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ItemInit.LEAD_HELMET.get())
                && (entity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ItemInit.LEAD_CHESTPLATE.get())
                && (entity.getItemBySlot(EquipmentSlotType.LEGS).getItem() == ItemInit.LEAD_LEGGINGS.get())
                && (entity.getItemBySlot(EquipmentSlotType.FEET).getItem() == ItemInit.LEAD_BOOTS.get()));
    }

    /**
     * Applies Radioactivity effect to the entity if it has radioactive item in his inventory
     *
     * @param world    The world the entity is in
     * @param entity   The entity who has the item
     * @param duration The duration of the given effect
     * @param power    The power of the give effect
     */
    public static void radioactiveItemInInventory(World world, Entity entity, int duration, int power) {
        {
            List<Entity> _entfound = world.getEntitiesOfClass(Entity.class,
                    new AxisAlignedBB(entity.getX() - (10 / 2d),
                            entity.getY() - (10 / 2d), entity.getZ() - (10 / 2d),
                            entity.getX() + (10 / 2d), entity.getY() + (10 / 2d),
                            entity.getZ() + (10 / 2d)), null)
                    .stream().sorted(new Object() {
                        Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                            return Comparator.comparing(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                        }
                    }.compareDistOf(entity.getX(), entity.getY(), entity.getZ())).collect(Collectors.toList());
            for (Entity entityiterator : _entfound) {
                if (!(entityiterator instanceof LivingEntity)) return;
                if (entityiterator instanceof PlayerEntity) {
                    if (((PlayerEntity) entityiterator).abilities.instabuild) return;
                }
                if (!isEntityWearingAntiRadiationSuit((LivingEntity) entity)) {
                    boolean entityWearsLeadArmor = isEntityWearingLeadArmor((LivingEntity) entity);
                    ((LivingEntity) (entityWearsLeadArmor ? entity : entityiterator)).addEffect(new EffectInstance(PotionEffectInit.RADIOACTIVITY.get(), duration, power));
                }
            }
        }
    }

    /**
     * Applies Radioactivity effect to the entity if it steps on a radioactive block
     *
     * @param entity The entity that steps on the radioactive block
     */
    public static void stepOnRadioactiveBlock(Entity entity) {
        if (!(entity instanceof LivingEntity)) return;
        if (entity instanceof PlayerEntity) {
            if (((PlayerEntity) entity).abilities.instabuild) return;
        }
        if (!isEntityWearingAntiRadiationSuit((LivingEntity) entity)) {
            ((LivingEntity) entity).addEffect(new EffectInstance(PotionEffectInit.RADIOACTIVITY.get(), 200, 2));
        }
    }

    /**
     * This method applies the side effects of the radioactivity
     *
     * @param entity    The entity that has the effect
     * @param amplifier The amplifier of the effect
     */
    public static void radioactivityEffect(LivingEntity entity, int amplifier) {
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
}
