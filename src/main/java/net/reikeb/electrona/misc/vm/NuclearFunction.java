package net.reikeb.electrona.misc.vm;

import net.minecraft.advancements.*;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import net.reikeb.electrona.init.*;
import net.reikeb.electrona.tileentities.TileNuclearGeneratorController;
import net.reikeb.electrona.world.NuclearExplosion;
import net.reikeb.electrona.world.gamerules.DoBlackholesExist;

import java.util.*;
import java.util.stream.Collectors;

public class NuclearFunction {

    /**
     * Functionment of the Nuclear Generator
     *
     * @param tileEntity      The Tile Entity of the Nuclear Generator Controller
     * @param tileCooler      The Tile Entity of the Cooler below the Nuclear Generator Controller
     * @param slotCooler      The slot of the Cooler
     * @param slotGenerator   The slot of the Nuclear Generator Controller
     * @param electronicPower The energy inside the Nuclear Generator Controller
     * @param temperature     The temperature of the Nuclear Generator
     * @param waterLevel      The water level of the tank inside the Cooler
     */
    public static void nuclearGeneration(TileNuclearGeneratorController tileEntity, TileEntity tileCooler, ItemStack slotCooler, ItemStack slotGenerator, double electronicPower, int temperature, int waterLevel) {
        if ((slotCooler.getItem() == ItemInit.URANIUM_BAR.get())
                || (slotCooler.getItem() == ItemInit.URANIUM_DUAL_BAR.get())
                || (slotCooler.getItem() == ItemInit.URANIUM_QUAD_BAR.get())) {
            if ((tileEntity.getTileData().getBoolean("powered"))
                    && (tileEntity.getTileData().getBoolean("UBIn"))) {
                if (slotCooler.hurt(1, new Random(), null)) {
                    slotCooler.shrink(1);
                    slotCooler.setDamageValue(0);
                }
                if (slotGenerator.hurt(1, new Random(), null)) {
                    slotGenerator.shrink(1);
                    slotGenerator.setDamageValue(0);
                }
                if (temperature <= 500) {
                    tileEntity.getTileData().putInt("temperature", (temperature + 1));
                    if (electronicPower <= 9950) {
                        tileEntity.getTileData().putDouble("ElectronicPower", (electronicPower + 2.5));
                    } else if ((electronicPower > 9950) && (electronicPower < 10000)) {
                        tileEntity.getTileData().putDouble("ElectronicPower", (electronicPower + 0.05));
                    }
                    if (waterLevel >= 500) {
                        FluidFunction.drainWater(tileCooler, 40);
                    }
                } else if (temperature <= 1500) {
                    tileEntity.getTileData().putInt("temperature", (temperature + 5));
                    if (electronicPower <= 9900) {
                        tileEntity.getTileData().putDouble("ElectronicPower", (electronicPower + 5));
                    } else if ((electronicPower > 9900) && (electronicPower <= 9950)) {
                        tileEntity.getTileData().putDouble("ElectronicPower", (electronicPower + 2.5));
                    } else if ((electronicPower > 9950) && (electronicPower < 10000)) {
                        tileEntity.getTileData().putDouble("ElectronicPower", (electronicPower + 0.05));
                    }
                    if (waterLevel >= 800) {
                        FluidFunction.drainWater(tileCooler, 50);
                    }
                } else if (temperature <= 2400) {
                    tileEntity.getTileData().putInt("temperature", (temperature + 8));
                    if (electronicPower <= 9500) {
                        tileEntity.getTileData().putDouble("ElectronicPower", (electronicPower + 25));
                    } else if ((electronicPower > 9500) && (electronicPower <= 9900)) {
                        tileEntity.getTileData().putDouble("ElectronicPower", (electronicPower + 5));
                    } else if ((electronicPower > 9900) && (electronicPower <= 9950)) {
                        tileEntity.getTileData().putDouble("ElectronicPower", (electronicPower + 2.5));
                    } else if ((electronicPower > 9950) && (electronicPower < 10000)) {
                        tileEntity.getTileData().putDouble("ElectronicPower", (electronicPower + 0.05));
                    }
                    if (waterLevel >= 1000) {
                        FluidFunction.drainWater(tileCooler, 80);
                    }
                } else {
                    if (waterLevel >= 1000) {
                        FluidFunction.drainWater(tileCooler, 80);
                    } else {
                        tileEntity.getTileData().putInt("temperature", (temperature + 5));
                    }
                    if (electronicPower <= 9500) {
                        tileEntity.getTileData().putDouble("ElectronicPower", (electronicPower + 25));
                    } else if ((electronicPower > 9500) && (electronicPower <= 9900)) {
                        tileEntity.getTileData().putDouble("ElectronicPower", (electronicPower + 5));
                    } else if ((electronicPower > 9900) && (electronicPower <= 9950)) {
                        tileEntity.getTileData().putDouble("ElectronicPower", (electronicPower + 2.5));
                    } else if ((electronicPower > 9950) && (electronicPower < 10000)) {
                        tileEntity.getTileData().putDouble("ElectronicPower", (electronicPower + 0.05));
                    }
                }
                if ((waterLevel < 800) && (temperature >= 2000)) {
                    tileEntity.getTileData().putBoolean("alert", true);
                }
                if ((waterLevel < 800) && (temperature >= 2800)) {
                    if (tileEntity.getLevel() == null) return;
                    World world = tileEntity.getLevel();
                    BlockPos pos = tileEntity.getBlockPos();
                    world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    world.setBlock(tileCooler.getBlockPos(), Blocks.AIR.defaultBlockState(), 3);
                    new NuclearExplosion(world, pos.getX(), pos.getY(), pos.getZ(), 20);
                    if ((Math.random() < 0.45) && world.getLevelData().getGameRules().getBoolean(DoBlackholesExist.DO_BLACK_HOLES_EXIST)) {
                        world.setBlock(pos, BlockInit.SINGULARITY.get().defaultBlockState(), 3);
                        advancementInevitableFunction(world, pos);
                    }
                }
                if ((waterLevel > 400) && (temperature >= 2000)) {
                    FluidFunction.drainWater(tileCooler, 40);
                    tileEntity.getTileData().putInt("temperature", (temperature - 10));
                } else if ((waterLevel > 300) && (temperature >= 1000)) {
                    FluidFunction.drainWater(tileCooler, 30);
                    tileEntity.getTileData().putInt("temperature", (temperature - 5));
                } else if ((waterLevel > 200) && (temperature >= 50)) {
                    FluidFunction.drainWater(tileCooler, 20);
                    tileEntity.getTileData().putInt("temperature", (int) (temperature - 2.5));
                } else if ((waterLevel >= 1) && (temperature >= 1)) {
                    FluidFunction.drainWater(tileCooler, 1);
                    tileEntity.getTileData().putInt("temperature", temperature - 1);
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
    public static void advancementInevitableFunction(World world, BlockPos pos) {
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        {
            List<Entity> _entfound = world.getEntitiesOfClass(Entity.class,
                    new AxisAlignedBB(x - 5, y - 5, z - 5,
                            x + 5, y + 5, z + 5), null)
                    .stream().sorted(new Object() {
                        Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                            return Comparator.comparing(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                        }
                    }.compareDistOf(x, y, z)).collect(Collectors.toList());
            for (Entity entityiterator : _entfound) {
                if (entityiterator instanceof ServerPlayerEntity) {
                    Advancement advancement = ((ServerPlayerEntity) entityiterator).server.getAdvancements().getAdvancement(new ResourceLocation("electrona:i_am_inevitable"));
                    if (advancement == null) System.out.println("Advancement I Am... Inevitable! seems to be null");
                    if (advancement == null) return;
                    AdvancementProgress advancementProgress = ((ServerPlayerEntity) entityiterator).getAdvancements().getOrStartProgress(advancement);
                    if (!advancementProgress.isDone()) {
                        for (String criteria : advancementProgress.getRemainingCriteria()) {
                            ((ServerPlayerEntity) entityiterator).getAdvancements().award(advancement, criteria);
                        }
                    }
                }
            }
        }
    }
}
