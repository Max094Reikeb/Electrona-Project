package net.reikeb.electrona.misc.vm;

import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.reikeb.electrona.blockentities.NuclearGeneratorControllerBlockEntity;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.electrona.world.Gamerules;
import net.reikeb.electrona.world.NuclearExplosion;
import net.reikeb.maxilib.utils.Utils;

import java.util.Random;

public class NuclearFunction {

    /**
     * Functionment of the Nuclear Generator
     *
     * @param nuclearGeneratorControllerBlockEntity The Block Entity of the Nuclear Generator Controller
     * @param coolerBlockEntity                     The Block Entity of the Cooler below the Nuclear Generator Controller
     * @param slotCooler                            The slot of the Cooler
     */
    public static void nuclearGeneration(NuclearGeneratorControllerBlockEntity nuclearGeneratorControllerBlockEntity, BlockEntity coolerBlockEntity, ItemStack slotCooler) {
        double electronicPower = nuclearGeneratorControllerBlockEntity.getElectronicPower();
        int temperature = nuclearGeneratorControllerBlockEntity.getTemperature();
        int waterLevel = nuclearGeneratorControllerBlockEntity.getUnderWater();
        ItemStack slotGenerator = nuclearGeneratorControllerBlockEntity.getItemInventory().getStackInSlot(1);
        if ((slotCooler.getItem() == ItemInit.URANIUM_BAR.get())
                || (slotCooler.getItem() == ItemInit.URANIUM_DUAL_BAR.get())
                || (slotCooler.getItem() == ItemInit.URANIUM_QUAD_BAR.get())) {
            if ((nuclearGeneratorControllerBlockEntity.isPowered() == 1)
                    && (nuclearGeneratorControllerBlockEntity.areUbIn() == 1)) {
                if (slotCooler.hurt(1, new Random(), null)) {
                    slotCooler.shrink(1);
                    slotCooler.setDamageValue(0);
                }
                if (slotGenerator.hurt(1, new Random(), null)) {
                    slotGenerator.shrink(1);
                    slotGenerator.setDamageValue(0);
                }
                if (temperature <= 500) {
                    nuclearGeneratorControllerBlockEntity.setTemperature(temperature += 1);
                    if (electronicPower <= 9950) {
                        EnergyFunction.fillEnergy(nuclearGeneratorControllerBlockEntity, 2.5);
                    } else if ((electronicPower > 9950) && (electronicPower < 10000)) {
                        EnergyFunction.fillEnergy(nuclearGeneratorControllerBlockEntity, 0.05);
                    }
                    if (waterLevel >= 500) {
                        FluidFunction.drainWater(coolerBlockEntity, 40);
                    }
                } else if (temperature <= 1500) {
                    nuclearGeneratorControllerBlockEntity.setTemperature(temperature + 5);
                    if (electronicPower <= 9900) {
                        EnergyFunction.fillEnergy(nuclearGeneratorControllerBlockEntity, 5);
                    } else if ((electronicPower > 9900) && (electronicPower <= 9950)) {
                        EnergyFunction.fillEnergy(nuclearGeneratorControllerBlockEntity, 2.5);
                    } else if ((electronicPower > 9950) && (electronicPower < 10000)) {
                        EnergyFunction.fillEnergy(nuclearGeneratorControllerBlockEntity, 0.05);
                    }
                    if (waterLevel >= 800) {
                        FluidFunction.drainWater(coolerBlockEntity, 50);
                    }
                } else if (temperature <= 2400) {
                    nuclearGeneratorControllerBlockEntity.setTemperature(temperature + 8);
                    if (electronicPower <= 9500) {
                        EnergyFunction.fillEnergy(nuclearGeneratorControllerBlockEntity, 25);
                    } else if ((electronicPower > 9500) && (electronicPower <= 9900)) {
                        EnergyFunction.fillEnergy(nuclearGeneratorControllerBlockEntity, 5);
                    } else if ((electronicPower > 9900) && (electronicPower <= 9950)) {
                        EnergyFunction.fillEnergy(nuclearGeneratorControllerBlockEntity, 2.5);
                    } else if ((electronicPower > 9950) && (electronicPower < 10000)) {
                        EnergyFunction.fillEnergy(nuclearGeneratorControllerBlockEntity, 0.05);
                    }
                    if (waterLevel >= 1000) {
                        FluidFunction.drainWater(coolerBlockEntity, 80);
                    }
                } else {
                    if (waterLevel >= 1000) {
                        FluidFunction.drainWater(coolerBlockEntity, 80);
                    } else {
                        nuclearGeneratorControllerBlockEntity.setTemperature(temperature + 5);
                    }
                    if (electronicPower <= 9500) {
                        EnergyFunction.fillEnergy(nuclearGeneratorControllerBlockEntity, 25);
                    } else if ((electronicPower > 9500) && (electronicPower <= 9900)) {
                        EnergyFunction.fillEnergy(nuclearGeneratorControllerBlockEntity, 5);
                    } else if ((electronicPower > 9900) && (electronicPower <= 9950)) {
                        EnergyFunction.fillEnergy(nuclearGeneratorControllerBlockEntity, 2.5);
                    } else if ((electronicPower > 9950) && (electronicPower < 10000)) {
                        EnergyFunction.fillEnergy(nuclearGeneratorControllerBlockEntity, 0.05);
                    }
                }
                if ((waterLevel < 800) && (temperature >= 2000)) {
                    nuclearGeneratorControllerBlockEntity.setAlert(1);
                }
                if ((waterLevel < 800) && (temperature >= 2800)) {
                    if (nuclearGeneratorControllerBlockEntity.getLevel() == null) return;
                    Level world = nuclearGeneratorControllerBlockEntity.getLevel();
                    BlockPos pos = nuclearGeneratorControllerBlockEntity.getBlockPos();
                    world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    world.setBlock(coolerBlockEntity.getBlockPos(), Blocks.AIR.defaultBlockState(), 3);
                    new NuclearExplosion(world, null, pos, 20);
                    if ((Math.random() < 0.45) && world.getLevelData().getGameRules().getBoolean(Gamerules.DO_BLACK_HOLES_EXIST)) {
                        world.setBlock(pos, BlockInit.SINGULARITY.get().defaultBlockState(), 3);
                        advancementInevitableFunction(world, pos);
                    }
                }
                if ((waterLevel > 400) && (temperature >= 2000)) {
                    FluidFunction.drainWater(coolerBlockEntity, 40);
                    nuclearGeneratorControllerBlockEntity.setTemperature(temperature - 10);
                } else if ((waterLevel > 300) && (temperature >= 1000)) {
                    FluidFunction.drainWater(coolerBlockEntity, 30);
                    nuclearGeneratorControllerBlockEntity.setTemperature(temperature - 5);
                } else if ((waterLevel > 200) && (temperature >= 50)) {
                    FluidFunction.drainWater(coolerBlockEntity, 20);
                    nuclearGeneratorControllerBlockEntity.setTemperature((int) (temperature - 2.5));
                } else if ((waterLevel >= 1) && (temperature >= 1)) {
                    FluidFunction.drainWater(coolerBlockEntity, 1);
                    nuclearGeneratorControllerBlockEntity.setTemperature(temperature - 1);
                }
            }
        }
    }

    /**
     * Handles the I Am... Inevitable! advancement
     *
     * @param world World of the player(s)
     * @param pos   Position of the Singularity
     */
    public static void advancementInevitableFunction(Level world, BlockPos pos) {
        for (LivingEntity entityiterator : Utils.getLivingEntitiesInRadius(world, pos, 5)) {
            if (entityiterator instanceof ServerPlayer serverPlayer) {
                Advancement advancement = serverPlayer.server.getAdvancements().getAdvancement(Keys.I_AM_INEVITABLE_ADVANCEMENT);
                Utils.awardAdvancement(serverPlayer, advancement, "I Am... Inevitable!");
            }
        }
    }
}
