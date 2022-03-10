package net.reikeb.electrona.world.gen.injection;

import net.reikeb.electrona.init.BiomeInit;
import net.reikeb.electrona.world.gen.ConfiguredStructures;

public final class Ruins {

    private Ruins() {
    }

    public static void addRuins(BiomeInjection.BiomeInjectionHelper event) {
        if (event.biomeKey.equals(BiomeInit.NUCLEAR_BIOME_KEY)) {
            event.addStructure(ConfiguredStructures.CONFIGURED_RUINS);
        }
    }
}
