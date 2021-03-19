package net.reikeb.electrona.containers.sync;

import net.minecraft.util.IIntArray;

import net.reikeb.electrona.tileentities.TileBattery;

public class BatterySyncData implements IIntArray {
    private final TileBattery te;

    public BatterySyncData(TileBattery te) {
        this.te = te;
    }

    @Override
    public int get(int index) {
        if (index == 0) {
            return (int) this.te.electronicPower;
        }
        return 0;
    }

    @Override
    public void set(int index, int value) {
        if (index == 0) {
            this.te.electronicPower = value;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
