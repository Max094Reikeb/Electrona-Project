package net.reikeb.electrona.init;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.reikeb.electrona.Electrona;

public class ParticleInit {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES,
            Electrona.MODID);

    public static final RegistryObject<SimpleParticleType> DARK_MATTER = PARTICLES.register("dark_matter", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> GRAVITORIUM = PARTICLES.register("gravitorium", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> RADIOACTIVE_FALLOUT = PARTICLES.register("radioactive_fallout", () -> new SimpleParticleType(false));
}
