package net.reikeb.electrona.misc.vm;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.init.PotionEffectInit;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RadioactivityFunction {

    /**
     * Method to get if the entity given is wearing the whole Anti-Radiation Suit
     *
     * @param entity   The entity we're checking
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
     * @param entity   The entity we're checking
     * @return boolean If the entity is wearing the full armor
     */
    public static boolean isEntityWearingLeadArmor(LivingEntity entity) {
        return false;
    }

    /**
     * Applies Radioactivity effect to the entity if it has radioactive item in his inventory
     *
     * @param itemstack The radioactive item
     * @param world     The world the entity is in
     * @param entity    The entity who has the item
     */
    public static void radioactiveItemInInventory(ItemStack itemstack, World world, Entity entity) {
        boolean entityWearsRadiationArmor = isEntityWearingAntiRadiationSuit((LivingEntity) entity);
        boolean entityWearsLeadArmor = isEntityWearingLeadArmor((LivingEntity) entity);
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        {
            List<Entity> _entfound = world.getEntitiesOfClass(Entity.class,
                    new AxisAlignedBB(x - (10 / 2d), y - (10 / 2d), z - (10 / 2d), x + (10 / 2d), y + (10 / 2d), z + (10 / 2d)), null)
                    .stream().sorted(new Object() {
                        Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                            return Comparator.comparing(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                        }
                    }.compareDistOf(x, y, z)).collect(Collectors.toList());
            for (Entity entityiterator : _entfound) {
                if (!entityWearsRadiationArmor) {
                    if (itemstack.getItem() == ItemInit.URANIUM_QUAD_BAR.get()) {
                        ((LivingEntity) (entityWearsLeadArmor ? entity : entityiterator)).addEffect(new EffectInstance(PotionEffectInit.RADIOACTIVITY.get(), 600, 3));
                    } else if (itemstack.getItem() == ItemInit.URANIUM_DUAL_BAR.get()) {
                        ((LivingEntity) (entityWearsLeadArmor ? entity : entityiterator)).addEffect(new EffectInstance(PotionEffectInit.RADIOACTIVITY.get(), 400, 3));
                    } else if (itemstack.getItem() == ItemInit.URANIUM_BAR.get()) {
                        ((LivingEntity) (entityWearsLeadArmor ? entity : entityiterator)).addEffect(new EffectInstance(PotionEffectInit.RADIOACTIVITY.get(), 300, 3));
                    } else if (itemstack.getItem() == ItemInit.PURIFIED_URANIUM.get()) {
                        ((LivingEntity) (entityWearsLeadArmor ? entity : entityiterator)).addEffect(new EffectInstance(PotionEffectInit.RADIOACTIVITY.get(), 200, 2));
                    } else if (itemstack.getItem() == ItemInit.CONCENTRATED_URANIUM.get()) {
                        ((LivingEntity) (entityWearsLeadArmor ? entity : entityiterator)).addEffect(new EffectInstance(PotionEffectInit.RADIOACTIVITY.get(), 200, 1));
                    } else if (itemstack.getItem() == ItemInit.YELLOWCAKE.get()) {
                        ((LivingEntity) (entityWearsLeadArmor ? entity : entityiterator)).addEffect(new EffectInstance(PotionEffectInit.RADIOACTIVITY.get(), 200, 0));
                    }
                }
            }
        }
    }
}
