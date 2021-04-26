package net.reikeb.electrona.init;

import net.minecraft.entity.*;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.*;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.entity.RadioactiveZombie;

public class EntityInit {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES,
            Electrona.MODID);

    public static final EntityType<RadioactiveZombie> RADIOACTIVE_ZOMBIE_TYPE = EntityType.Builder
            .of(RadioactiveZombie::new, EntityClassification.MONSTER)
            .sized(0.7F, 1.8F)
            .build("radioactive_zombie");

    public static final RegistryObject<EntityType<RadioactiveZombie>> RADIOACTIVE_ZOMBIE = ENTITIES.register("radioactive_zombie", () -> RADIOACTIVE_ZOMBIE_TYPE);
}
