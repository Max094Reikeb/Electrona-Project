package net.reikeb.electrona.advancements;

import net.minecraft.advancements.CriteriaTriggers;

import net.reikeb.electrona.advancements.critereon.EnergeticLightningStrikeTrigger;

public class TTriggers {

    public static final TriggerBase FIRST_COMPRESSION = new TriggerBase("first_compression");
    public static final TriggerBase A_NEW_MECHANIC = new TriggerBase("a_new_mechanic");
    public static final EnergeticLightningStrikeTrigger ENERGETIC_LIGHTNING_STRIKE = new EnergeticLightningStrikeTrigger();

    public static void init() {
        CriteriaTriggers.register(FIRST_COMPRESSION);
        CriteriaTriggers.register(A_NEW_MECHANIC);
        CriteriaTriggers.register(ENERGETIC_LIGHTNING_STRIKE);
    }
}
