package net.reikeb.electrona.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

import net.minecraftforge.api.distmarker.*;

import java.util.Random;

public class RadioactiveFallout extends RisingParticle {

    protected RadioactiveFallout(ClientWorld world, double x, double y, double z, double vx, double vy, double vz, float n, IAnimatedSprite spriteSet) {
        super(world, x, y, z, 0.1F, -0.1F, 0.1F, vx, vy, vz, n, spriteSet, 0.0F, 20, -5.0E-4D, false);
        this.rCol = 0.7294118F;
        this.gCol = 0.69411767F;
        this.bCol = 0.7607843F;
    }

    @OnlyIn(Dist.CLIENT)
    public static class RadioactiveFalloutFactory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite sprites;

        public RadioactiveFalloutFactory(IAnimatedSprite sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(BasicParticleType particleType, ClientWorld world, double x, double y, double z, double vx, double vy, double vz) {
            Random random = world.random;
            double d0 = (double) random.nextFloat() * -1.9D * (double) random.nextFloat() * 0.1D;
            double d1 = (double) random.nextFloat() * -0.5D * (double) random.nextFloat() * 0.1D * 5.0D;
            double d2 = (double) random.nextFloat() * -1.9D * (double) random.nextFloat() * 0.1D;
            return new RadioactiveFallout(world, x, y, z, d0, d1, d2, 1.0F, this.sprites);
        }
    }
}
