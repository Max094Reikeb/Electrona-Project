package net.reikeb.electrona.gempower;

import net.minecraft.nbt.CompoundTag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GemPowerInstance {

    private static final Logger LOGGER = LogManager.getLogger();
    private final GemPower gemPower;
    private final int cooldown;

    public GemPowerInstance(GemPower gemPower) {
        this(gemPower, 0);
    }

    public GemPowerInstance(GemPower gemPower, int cooldown) {
        this.gemPower = gemPower;
        this.cooldown = cooldown;
    }

    public GemPowerInstance(GemPowerInstance gemPowerInstance) {
        this.gemPower = gemPowerInstance.gemPower;
        this.cooldown = gemPowerInstance.cooldown;
    }

    public GemPower getGemPower() {
        return this.gemPower == null ? null : this.gemPower.delegate.get();
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public String getDescriptionId() {
        return this.gemPower.getDescriptionId();
    }

    public String toString() {
        return this.getDescriptionId();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof GemPowerInstance gemPowerInstance)) {
            return false;
        } else {
            return this.cooldown == gemPowerInstance.cooldown && this.gemPower.equals(gemPowerInstance.gemPower);
        }
    }

    public static GemPowerInstance load(CompoundTag nbt) {
        GemPower gemPower = GemPower.byId(nbt.getString("Id"));
        return gemPower == null ? null : loadSpecifiedEffect(gemPower, nbt);
    }

    public CompoundTag save(CompoundTag nbt) {
        nbt.putString("Id", GemPower.getId(this.getGemPower()));
        nbt.putInt("Cooldown", this.getCooldown());
        return nbt;
    }

    private static GemPowerInstance loadSpecifiedEffect(GemPower gemPower, CompoundTag nbt) {
        int m = nbt.getInt("Cooldown");
        return new GemPowerInstance(gemPower, m);
    }
}