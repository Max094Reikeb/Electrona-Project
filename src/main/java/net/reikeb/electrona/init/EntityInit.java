package net.reikeb.electrona.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.entity.EnergeticLightningBolt;
import net.reikeb.electrona.entity.RadioactiveZombie;

import java.util.function.Supplier;

public class EntityInit {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Electrona.MODID);

    public static final RegistryObject<EntityType<RadioactiveZombie>> RADIOACTIVE_ZOMBIE = register("radioactive_zombie", () -> EntityType.Builder.of(RadioactiveZombie::new, MobCategory.MONSTER).sized(0.7F, 1.0F));
    public static final RegistryObject<EntityType<EnergeticLightningBolt>> ENERGETIC_LIGHTNING_BOLT = register("energetic_lightning_bolt", () -> EntityType.Builder.of(EnergeticLightningBolt::new, MobCategory.MISC).noSave().sized(0.0F, 0.0F).clientTrackingRange(16).updateInterval(Integer.MAX_VALUE));

    private static <T extends net.minecraft.world.entity.Entity> RegistryObject<EntityType<T>> register(String name, Supplier<EntityType.Builder<T>> builder) {
        return ENTITIES.register(name, () -> builder.get().build(name));
    }
}
