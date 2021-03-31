package net.reikeb.electrona.villages;

import net.minecraft.village.PointOfInterestType;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class POIFixup {

    private static final Method blockStatesInjector = ObfuscationReflectionHelper.findMethod(PointOfInterestType.class, "registerBlockStates",
            PointOfInterestType.class);

    public static void fixup() {
        try {
            blockStatesInjector.invoke(null, Villagers.ENGINEER_POI.get());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
