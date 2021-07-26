package net.reikeb.electrona.misc.vm;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.tileentities.TileSprayer;
import net.reikeb.electrona.utils.ItemHandler;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SprayerFunction {

    /**
     * Method that handles the way the Sprayer gives an effect to entities around it
     *
     * @param inv             The inventory of the Sprayer
     * @param tileSprayer     The TileEntity of the Sprayer
     * @param electronicPower The energy of the Sprayer
     */
    public static void mainSprayer(ItemHandler inv, TileSprayer tileSprayer, double electronicPower) {
        int boostCount = 0;
        Level world = tileSprayer.getLevel();
        double x = tileSprayer.getBlockPos().getX();
        double y = tileSprayer.getBlockPos().getY();
        double z = tileSprayer.getBlockPos().getZ();
        if (inv.getStackInSlot(1).getItem() == ItemInit.WIRELESS_BOOSTER.get()) {
            boostCount += 1;
        }
        if (inv.getStackInSlot(2).getItem() == ItemInit.WIRELESS_BOOSTER.get()) {
            boostCount += 1;
        }
        if (inv.getStackInSlot(3).getItem() == ItemInit.WIRELESS_BOOSTER.get()) {
            boostCount += 1;
        }
        tileSprayer.getTileData().putInt("radius", 5 + (boostCount * 3));
        if ((!(inv.getStackInSlot(0).isEmpty())) && (electronicPower >= 200)) {
            double radiusEffect = tileSprayer.getTileData().getInt("radius");
            {
                List<Entity> _entfound = world.getEntitiesOfClass(Entity.class,
                        new AABB(x - radiusEffect, y - radiusEffect, z - radiusEffect,
                                x + radiusEffect, y + radiusEffect, z + radiusEffect),
                        null).stream().sorted(new Object() {
                    Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                        return Comparator.comparing(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                    }
                }.compareDistOf(x, y, z)).collect(Collectors.toList());
                for (Entity entityiterator : _entfound) {
                    if (entityiterator instanceof LivingEntity) {
                        if (inv.getStackInSlot(0).getItem().isEdible()) {
                            tileSprayer.getTileData().putDouble("ElectronicPower", (electronicPower - 200));
                            FoodProperties usedFood = inv.getStackInSlot(0).getItem().getFoodProperties();
                            for (Pair<MobEffectInstance, Float> pairiterator : usedFood.getEffects()) {
                                if (world.getRandom().nextFloat() < pairiterator.getSecond()) {
                                    ((LivingEntity) entityiterator).addEffect(pairiterator.getFirst());
                                }
                            }
                            inv.decrStackSize(0, 1);
                        } else if (inv.getStackInSlot(0).getItem() instanceof PotionItem) {
                            tileSprayer.getTileData().putDouble("ElectronicPower", (electronicPower - 200));
                            for (MobEffectInstance effectiterator : PotionUtils.getMobEffects(inv.getStackInSlot(0))) {
                                ((LivingEntity) entityiterator).addEffect(new MobEffectInstance(effectiterator));
                            }
                            inv.decrStackSize(0, 1);
                        }
                    }
                }
            }
        }
    }

    /**
     * Method that handles Sprayer's particles
     *
     * @param world       The world of the Sprayer
     * @param tileSprayer The TileEntity of the Sprayer
     * @param pos         The position of the Sprayer
     */
    public static void sprayerParticles(Level world, TileSprayer tileSprayer, BlockPos pos) {
        if (world.isClientSide) return;
        double xRadius = tileSprayer.getTileData().getInt("radius");
        double loop = 0;
        double zRadius = tileSprayer.getTileData().getInt("radius");
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
