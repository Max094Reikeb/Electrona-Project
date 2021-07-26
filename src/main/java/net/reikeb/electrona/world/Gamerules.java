package net.reikeb.electrona.world;

import net.minecraft.world.level.GameRules;

import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Method;

public class Gamerules {

    public static final GameRules.Key<GameRules.BooleanValue> DO_BLACK_HOLES_EXIST = GameRules
            .register("doBlackholesExist", GameRules.Category.MISC, create(true));
    public static final GameRules.Key<GameRules.BooleanValue> DO_NUCLEAR_BOMBS_EXPLODE = GameRules
            .register("doNuclearBombsExplode", GameRules.Category.MISC, create(true));

    public static GameRules.Type<GameRules.BooleanValue> create(boolean defaultValue) {
        try {
            Method createGameruleMethod = ObfuscationReflectionHelper.findMethod(GameRules.BooleanValue.class, "m_46250_", boolean.class);
            createGameruleMethod.setAccessible(true);
            return (GameRules.Type<GameRules.BooleanValue>) createGameruleMethod.invoke(null, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
