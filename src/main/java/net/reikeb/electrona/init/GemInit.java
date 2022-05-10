package net.reikeb.electrona.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.*;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.gempower.GemObject;
import net.reikeb.electrona.gempower.GemPowerInstance;

import java.util.Objects;
import java.util.function.Supplier;

public class GemInit {

    public static final DeferredRegister<GemObject> GEM_DEFERRED_REGISTER = DeferredRegister.create(GemObject.class, Electrona.MODID);

    public static Supplier<IForgeRegistry<GemObject>> GEM_REGISTRY = GEM_DEFERRED_REGISTER.makeRegistry("gem", () ->
            new RegistryBuilder<GemObject>().setMaxID(Integer.MAX_VALUE - 1).setDefaultKey(Electrona.RL("empty"))
    );

    public static final RegistryObject<GemObject> EMPTY = GEM_DEFERRED_REGISTER.register("empty", GemObject::new);
    public static final RegistryObject<GemObject> INVISIBILITY = GEM_DEFERRED_REGISTER.register("invisibility", () -> new GemObject(Lazy.of(() -> new GemPowerInstance(GemPowerInit.INVISIBILITY.get(), 2400))));
    public static final RegistryObject<GemObject> STRENGTH = GEM_DEFERRED_REGISTER.register("strength", () -> new GemObject(Lazy.of(() -> new GemPowerInstance(GemPowerInit.STRENGTH.get(), 3600))));
    public static final RegistryObject<GemObject> TELEPORTATION = GEM_DEFERRED_REGISTER.register("teleportation", () -> new GemObject(Lazy.of(() -> new GemPowerInstance(GemPowerInit.TELEPORTATION.get(), 80))));
    public static final RegistryObject<GemObject> YO_YO = GEM_DEFERRED_REGISTER.register("yo_yo", () -> new GemObject(Lazy.of(() -> new GemPowerInstance(GemPowerInit.YO_YO.get(), 400))));
    public static final RegistryObject<GemObject> DIMENSION_TRAVEL = GEM_DEFERRED_REGISTER.register("dimension_travel", () -> new GemObject(Lazy.of(() -> new GemPowerInstance(GemPowerInit.DIMENSION_TRAVEL.get(), 400))));
    public static final RegistryObject<GemObject> KNOCKBACK = GEM_DEFERRED_REGISTER.register("knockback", () -> new GemObject(Lazy.of(() -> new GemPowerInstance(GemPowerInit.KNOCKBACK.get(), 600))));
    public static final RegistryObject<GemObject> FLYING = GEM_DEFERRED_REGISTER.register("flying", () -> new GemObject(Lazy.of(() -> new GemPowerInstance(GemPowerInit.FLYING.get(), 200))));

    public static <T extends IForgeRegistryEntry<?>> ResourceLocation getName(T type) {
        return Objects.requireNonNull(type.getRegistryName());
    }
}
