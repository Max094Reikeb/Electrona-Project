package net.reikeb.electrona.villages;

import net.minecraft.world.entity.ai.village.poi.PoiType;

import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import net.reikeb.electrona.Electrona;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class POIFixup {

    private static final Method blockStatesInjector = ObfuscationReflectionHelper.findMethod(PoiType.class, "m_27367_",
            PoiType.class);

    public static void fixup() {
        try {
            blockStatesInjector.invoke(null, Villagers.ENGINEER_POI.get());
        } catch (IllegalAccessException | InvocationTargetException e) {
            Electrona.LOGGER.catching(e);
        }
    }
}
