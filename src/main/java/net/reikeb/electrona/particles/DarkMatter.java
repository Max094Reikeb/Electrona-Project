package net.reikeb.electrona.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

import net.minecraftforge.api.distmarker.*;

public class DarkMatter extends SpriteTexturedParticle {

    private final IAnimatedSprite spriteSet;
    private float angularVelocity;
    private float angularAcceleration;

    protected DarkMatter(ClientWorld world, double x, double y, double z, double vx, double vy, double vz, IAnimatedSprite spriteSet) {
        super(world, x, y, z);
        this.spriteSet = spriteSet;
        this.setSize((float) 0.4, (float) 0.4);
        this.quadSize *= (float) 1;
        this.lifetime = (int) Math.max(1, 10 + (this.random.nextInt(30) - 15));
        this.gravity = (float) 0;
        this.hasPhysics = true;
        this.xd = vx * 1;
        this.yd = vy * 1;
        this.zd = vz * 1;
        this.angularVelocity = (float) 0.1;
        this.angularAcceleration = (float) 0.1;
        this.pickSprite(spriteSet);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        super.tick();
        this.oRoll = this.roll;
        this.roll += this.angularVelocity;
        this.angularVelocity += this.angularAcceleration;
    }

    @OnlyIn(Dist.CLIENT)
    public static class DarkMatterParticleFactory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public DarkMatterParticleFactory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new DarkMatter(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}
