package net.reikeb.electrona.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.*;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.gempower.GemPower;

import java.util.Objects;
import java.util.function.Supplier;

public class GemPowerInit {

    public static final DeferredRegister<GemPower> GEM_POWER_DEFERRED_REGISTER = DeferredRegister.create(GemPower.class, Electrona.MODID);

    public static Supplier<IForgeRegistry<GemPower>> GEM_POWER_REGISTRY = GEM_POWER_DEFERRED_REGISTER.makeRegistry("gempower", () ->
            new RegistryBuilder<GemPower>().setMaxID(Integer.MAX_VALUE - 1).onAdd((owner, stage, id, obj, oldObj) ->
                    Electrona.LOGGER.info("Gem Power added: " + getName(obj).toString() + " ")
            ).setDefaultKey(Electrona.RL("empty")));

    public static final RegistryObject<GemPower> INVISIBILITY = register("invisibility", 2400);
    public static final RegistryObject<GemPower> STRENGTH = register("strength", 3600);
    public static final RegistryObject<GemPower> TELEPORTATION = register("teleportation", 80);
    public static final RegistryObject<GemPower> YO_YO = register("yo_yo", 400);
    public static final RegistryObject<GemPower> DIMENSION_TRAVEL = register("dimension_travel", 400);
    public static final RegistryObject<GemPower> KNOCKBACK = register("knockback", 600);
    public static final RegistryObject<GemPower> FLYING = register("flying", 200);

    public static <T extends IForgeRegistryEntry<?>> ResourceLocation getName(T type) {
        return Objects.requireNonNull(type.getRegistryName());
    }

    public static RegistryObject<GemPower> register(String name, int cooldown) {
        return GEM_POWER_DEFERRED_REGISTER.register(name, () -> new GemPower(cooldown));
    }
}
