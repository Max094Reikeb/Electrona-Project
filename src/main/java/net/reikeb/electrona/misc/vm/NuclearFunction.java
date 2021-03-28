package net.reikeb.electrona.misc.vm;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.tileentities.TileNuclearGeneratorController;

import java.util.Random;

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
                    tileEntity.explodes();
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
}
