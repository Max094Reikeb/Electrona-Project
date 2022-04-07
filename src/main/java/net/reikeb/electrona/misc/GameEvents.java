package net.reikeb.electrona.misc;

import net.minecraft.core.Registry;
import net.minecraft.world.level.gameevent.GameEvent;

public class GameEvents {

    public static GameEvent ENERGETIC_LIGHTNING_STRIKE;
    public static GameEvent NUCLEAR_EXPLOSION;
    public static GameEvent SINGULARITY;
    public static GameEvent TELEPORTER_USE;

    public static void setupGameEvents() {
        ENERGETIC_LIGHTNING_STRIKE = register("energetic_lightning_strike");
        NUCLEAR_EXPLOSION = register("nuclear_explosion");
        SINGULARITY = register("singularity");
        TELEPORTER_USE = register("teleporter_use");
    }

    private static GameEvent register(String name) {
        return Registry.register(Registry.GAME_EVENT, name, new GameEvent(name, 16));
    }
}
