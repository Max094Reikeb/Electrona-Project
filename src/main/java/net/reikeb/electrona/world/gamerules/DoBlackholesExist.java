package net.reikeb.electrona.world.gamerules;

import net.minecraft.world.GameRules;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Method;

public class DoBlackholesExist {

    public static final GameRules.RuleKey<GameRules.BooleanValue> DO_BLACK_HOLES_EXIST = GameRules
            .register("doBlackholesExist", GameRules.Category.MISC, create(true));

    public static GameRules.RuleType<GameRules.BooleanValue> create(boolean defaultValue) {
        try {
            Method createGameruleMethod = ObfuscationReflectionHelper.findMethod(GameRules.BooleanValue.class, "func_223567_b", boolean.class);
            createGameruleMethod.setAccessible(true);
            return (GameRules.RuleType<GameRules.BooleanValue>) createGameruleMethod.invoke(null, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
