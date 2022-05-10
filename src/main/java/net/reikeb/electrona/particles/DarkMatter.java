package net.reikeb.electrona.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DarkMatter extends TextureSheetParticle {

    private final float angularAcceleration;
    private float angularVelocity;

    protected DarkMatter(ClientLevel clientLevel, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(clientLevel, x, y, z);
        this.setSize((float) 0.4, (float) 0.4);
        this.quadSize *= (float) 1;
        this.lifetime = Math.max(1, 10 + (this.random.nextInt(30) - 15));
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
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        super.tick();
        this.oRoll = this.roll;
        this.roll += this.angularVelocity;
        this.angularVelocity += this.angularAcceleration;
    }

    @OnlyIn(Dist.CLIENT)
    public static class DarkMatterParticleFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public DarkMatterParticleFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new DarkMatter(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}
