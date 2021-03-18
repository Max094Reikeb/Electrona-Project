package net.reikeb.electrona.containers.sync;

import net.minecraft.util.IIntArray;

import net.reikeb.electrona.tileentities.TileCompressor;

public class CompressorSyncData implements IIntArray {
    private final TileCompressor te;

    public CompressorSyncData(TileCompressor te) {
        this.te = te;
    }

    @Override
    public int get(int index) {
        switch (index) {
            case 0:
                return (int) this.te.electronicPower;
            case 1:
                return this.te.compressingTime;
            case 2:
                return this.te.currentCompressingTime;
            default:
                return 0;
        }
    }

    @Override
    public void set(int index, int value) {
        switch (index) {
            case 0:
                this.te.electronicPower = value;
                break;
            case 1:
                this.te.compressingTime = value;
                break;
            case 2:
                this.te.currentCompressingTime = value;
                break;
            default:
                break;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}