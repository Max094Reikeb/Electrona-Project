package net.reikeb.electrona.init;

import net.minecraft.particles.*;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.*;

import net.reikeb.electrona.Electrona;

public class ParticleInit {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES,
            Electrona.MODID);

    public static final RegistryObject<BasicParticleType> DARK_MATTER = PARTICLES.register("dark_matter", () -> new BasicParticleType(false));
    public static final RegistryObject<BasicParticleType> RADIOACTIVE_FALLOUT = PARTICLES.register("radioactive_fallout", () -> new BasicParticleType(false));
}
