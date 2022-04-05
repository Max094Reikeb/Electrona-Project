package net.reikeb.electrona.misc.vm;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;

import net.reikeb.electrona.blockentities.SprayerBlockEntity;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.utils.ElectronaUtils;
import net.reikeb.electrona.utils.ItemHandler;

public class SprayerFunction {

    /**
     * Method that handles the way the Sprayer gives an effect to entities around it
     *
     * @param inv                The inventory of the Sprayer
     * @param sprayerBlockEntity The BlockEntity of the Sprayer
     * @param electronicPower    The energy of the Sprayer
     */
    public static void mainSprayer(ItemHandler inv, SprayerBlockEntity sprayerBlockEntity, double electronicPower) {
        int boostCount = 0;
        Level world = sprayerBlockEntity.getLevel();
        if (inv.getStackInSlot(1).getItem() == ItemInit.WIRELESS_BOOSTER.get()) {
            boostCount += 1;
        }
        if (inv.getStackInSlot(2).getItem() == ItemInit.WIRELESS_BOOSTER.get()) {
            boostCount += 1;
        }
        if (inv.getStackInSlot(3).getItem() == ItemInit.WIRELESS_BOOSTER.get()) {
            boostCount += 1;
        }
        sprayerBlockEntity.getTileData().putInt("radius", 5 + (boostCount * 3));
        if ((!(inv.getStackInSlot(0).isEmpty())) && (electronicPower >= 200)) {
            double radiusEffect = sprayerBlockEntity.getTileData().getInt("radius");
            if (world == null) return;
            for (LivingEntity entityiterator : ElectronaUtils.getLivingEntitiesInRadius(world, sprayerBlockEntity.getBlockPos(), (int) radiusEffect)) {
                if (inv.getStackInSlot(0).getItem().isEdible()) {
                    sprayerBlockEntity.getTileData().putDouble("ElectronicPower", (electronicPower - 200));
                    FoodProperties usedFood = inv.getStackInSlot(0).getItem().getFoodProperties();
                    if (usedFood == null) return;
                    for (Pair<MobEffectInstance, Float> pairiterator : usedFood.getEffects()) {
                        if (world.getRandom().nextFloat() < pairiterator.getSecond()) {
                            entityiterator.addEffect(pairiterator.getFirst());
                        }
                    }
                    inv.decrStackSize(0, 1);
                } else if (inv.getStackInSlot(0).getItem() instanceof PotionItem) {
                    sprayerBlockEntity.getTileData().putDouble("ElectronicPower", (electronicPower - 200));
                    for (MobEffectInstance effectiterator : PotionUtils.getMobEffects(inv.getStackInSlot(0))) {
                        entityiterator.addEffect(new MobEffectInstance(effectiterator));
                    }
                    inv.decrStackSize(0, 1);
                }
            }
        }
    }

    /**
     * Method that handles Sprayer's particles
     *
     * @param world              The world of the Sprayer
     * @param sprayerBlockEntity The BlockEntity of the Sprayer
     * @param pos                The position of the Sprayer
     */
    public static void sprayerParticles(Level world, SprayerBlockEntity sprayerBlockEntity, BlockPos pos) {
        if (world.isClientSide) return;
        double xRadius = sprayerBlockEntity.getTileData().getInt("radius");
        double loop = 0;
        double zRadius = sprayerBlockEntity.getTileData().getInt("radius");
        double particleAmount = (xRadius) * 4;
        while (loop < particleAmount) {
            if (world instanceof ServerLevel) {
                ((ServerLevel) world).sendParticles(ParticleTypes.CLOUD, (pos.getX() + (Math.cos((((Math.PI * 2) / (particleAmount)) * (loop))) * (xRadius))),
                        pos.getY(), (pos.getZ() + (Math.sin((((Math.PI * 2) / (particleAmount)) * (loop))) * (zRadius))), 3, 0, 0, 0, 0.05);
            }
            loop = (loop) + 1;
        }
    }
}
