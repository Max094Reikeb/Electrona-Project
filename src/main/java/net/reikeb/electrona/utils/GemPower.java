package net.reikeb.electrona.utils;

import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum GemPower {

    INVISIBILITY(0, "invisibility", 2400),
    STRENGTH(1, "strength", 3600),
    TELEPORTATION(2, "teleportation", 80),
    YO_YO(3, "yo_yo", 400),
    DIMENSION_TRAVEL(4, "dimension_travel", 400),
    KNOCKBACK(5, "knockback", 600),
    FLYING(6, "flying", 200);

    private static final List<GemPower> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    private final int id;
    private final String name;
    private final int cooldown;

    GemPower(int p_345843475_0, String p_345843475_1, int p_345843475_2) {
        this.id = p_345843475_0;
        this.name = p_345843475_1;
        this.cooldown = p_345843475_2;
    }

    public static GemPower randomPower() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    public static String randomPowerId() {
        return byId(randomPower());
    }

    public static GemPower byName(String p_345843476_0) {
        return byName(p_345843476_0, INVISIBILITY);
    }

    public static GemPower byName(String p_345843477_0, GemPower p_345843477_1) {
        for (GemPower gemPower : values()) {
            if (gemPower.name.equals(p_345843477_0)) {
                return gemPower;
            }
        }
        return p_345843477_1;
    }

    public static int byCooldown(ItemStack p_345843478_0) {
        String power = p_345843478_0.getOrCreateTag().getString("power");
        return byCooldown(power);
    }

    public static int byCooldown(String p_345843479_0) {
        return byCooldown(p_345843479_0, 0);
    }

    public static int byCooldown(String p_345843480_0, int p_345843480_1) {
        for (GemPower gemPower : values()) {
            if (gemPower.name.equals(p_345843480_0)) {
                return gemPower.cooldown;
            }
        }
        return p_345843480_1;
    }

    public static String byId(GemPower p_345843481_0) {
        return byId(p_345843481_0, "invisibility");
    }

    public static String byId(GemPower p_345843482_0, String p_345843482_1) {
        for (GemPower gemPower : values()) {
            if (gemPower.equalsTo(p_345843482_0)) {
                return gemPower.name;
            }
        }
        return p_345843482_1;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getCooldown() {
        return this.cooldown;
    }

    /**
     * Compares this power to the specified object.  The result is {@code
     * true} if and only if the argument is not {@code null} and is a {@code
     * GemPower} object that represents the same power as this object.
     *
     * @param anObject The object to compare this {@code GemPower} against
     * @return {@code true} if the given object represents a {@code GemPower}
     * equivalent to this power, {@code false} otherwise
     */
    public boolean equalsTo(Object anObject) {
        if (this == anObject) {
            return true;
        }
        return anObject instanceof GemPower && ((GemPower) anObject).name.equals(this.name);
    }
}
