package net.reikeb.electrona.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

import net.minecraftforge.api.distmarker.*;

public class Gravitorium extends SpriteTexturedParticle {

    private final double xStart;
    private final double yStart;
    private final double zStart;

    private Gravitorium(ClientWorld world, double x, double y, double z, double vx, double vy, double vz) {
        super(world, x, y, z);
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
        this.xStart = x;
        this.yStart = y;
        this.zStart = z;
        this.xo = x + vx;
        this.yo = y + vy;
        this.zo = z + vz;
        this.x = this.xo;
        this.y = this.yo;
        this.z = this.zo;
        this.quadSize = 0.1F * (this.random.nextFloat() * 0.5F + 0.2F);
        float f = this.random.nextFloat() * 0.6F + 0.4F;
        this.rCol = 0.9F * f;
        this.gCol = 0.9F * f;
        this.bCol = f;
        this.hasPhysics = false;
        this.lifetime = (int) (Math.random() * 10.0D) + 30;
    }

    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().move(x, y, z));
        this.setLocationFromBoundingbox();
    }

    public int getLightColor(float color) {
        int i = super.getLightColor(color);
        float f = (float) this.age / (float) this.lifetime;
        f = f * f;
        f = f * f;
        int j = i & 255;
        int k = i >> 16 & 255;
        k = k + (int) (f * 15.0F * 16.0F);
        if (k > 240) {
            k = 240;
        }

        return j | k << 16;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            float f = (float) this.age / (float) this.lifetime;
            f = 1.0F - f;
            float f1 = 1.0F - f;
            f1 = f1 * f1;
            f1 = f1 * f1;
            this.x = this.xStart + this.xd * (double) f;
            this.y = this.yStart + this.yd * (double) f - (double) (f1 * 1.2F);
            this.z = this.zStart + this.zd * (double) f;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class GravitoriumParticleFactory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite sprite;

        public GravitoriumParticleFactory(IAnimatedSprite sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(BasicParticleType type, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Gravitorium gravitorium = new Gravitorium(world, x, y, z, xSpeed, ySpeed, zSpeed);
            gravitorium.pickSprite(this.sprite);
            return gravitorium;
        }
    }
}
