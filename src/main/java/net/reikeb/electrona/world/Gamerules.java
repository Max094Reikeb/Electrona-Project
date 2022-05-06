package net.reikeb.electrona.world;

import net.minecraft.world.level.GameRules;

public class Gamerules {

    public static final GameRules.Key<GameRules.BooleanValue> DO_BLACK_HOLES_EXIST = GameRules
            .register("doBlackholesExist", GameRules.Category.MISC, GameRules.BooleanValue.create(true));
    public static final GameRules.Key<GameRules.BooleanValue> DO_NUCLEAR_BOMBS_EXPLODE = GameRules
            .register("doNuclearBombsExplode", GameRules.Category.MISC, GameRules.BooleanValue.create(true));
}
