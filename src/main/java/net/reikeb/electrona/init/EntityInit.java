package net.reikeb.electrona.init;

import net.minecraft.entity.*;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.*;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.entity.*;

public class EntityInit {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES,
            Electrona.MODID);

    public static final EntityType<RadioactiveZombie> RADIOACTIVE_ZOMBIE_TYPE = EntityType.Builder
            .of(RadioactiveZombie::new, EntityClassification.MONSTER)
            .sized(0.7F, 1.8F)
            .build("radioactive_zombie");

    public static final EntityType<EnergeticLightningBolt> ENERGETIC_LIGHTNING_BOLT_TYPE = EntityType.Builder
            .of(EnergeticLightningBolt::new, EntityClassification.MISC)
            .noSave()
            .sized(0.0F, 0.0F)
            .clientTrackingRange(16)
            .updateInterval(Integer.MAX_VALUE)
            .build("energetic_lightning_bolt");

    public static final RegistryObject<EntityType<RadioactiveZombie>> RADIOACTIVE_ZOMBIE = ENTITIES.register("radioactive_zombie", () -> RADIOACTIVE_ZOMBIE_TYPE);
    public static final RegistryObject<EntityType<EnergeticLightningBolt>> ENERGETIC_LIGHTNING_BOLT = ENTITIES.register("energetic_lightning_bolt", () -> ENERGETIC_LIGHTNING_BOLT_TYPE);
}
