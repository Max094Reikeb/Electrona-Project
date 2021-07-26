package net.reikeb.electrona.init;

import net.minecraft.core.particles.*;

import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.*;

import net.reikeb.electrona.Electrona;

public class ParticleInit {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES,
            Electrona.MODID);

    public static final RegistryObject<SimpleParticleType> DARK_MATTER = PARTICLES.register("dark_matter", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> GRAVITORIUM = PARTICLES.register("gravitorium", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> RADIOACTIVE_FALLOUT = PARTICLES.register("radioactive_fallout", () -> new SimpleParticleType(false));
}
