package net.reikeb.electrona.advancements;

import net.minecraft.advancements.CriteriaTriggers;

public class TTriggers {

    public static final TriggerBase FIRST_COMPRESSION = new TriggerBase("first_compression");

    public static void init() {
        CriteriaTriggers.register(FIRST_COMPRESSION);
    }
}
