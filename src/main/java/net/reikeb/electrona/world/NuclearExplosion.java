package net.reikeb.electrona.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.reikeb.biome_tools.BiomeUtil;
import net.reikeb.electrona.events.local.NuclearExplosionEvent;
import net.reikeb.electrona.init.BiomeInit;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.init.SoundsInit;
import net.reikeb.electrona.misc.DamageSources;
import net.reikeb.electrona.misc.GameEvents;
import net.reikeb.electrona.misc.Tags;
import net.reikeb.maxilib.utils.Gravity;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Credits: SNGTech
 * https://github.com/SNGTech/Beneath-Mod/blob/master/src/main/java/com/sngtech/beneathMod/world/explosions/NuclearExplosion.java
 */
public class NuclearExplosion extends Explosion {

    private final static List<BlockPos> affectedBlockPositions = Lists.newArrayList();
    private final Mode mode;
    private final Random random;
    private final Level level;
    private final double x;
    private final double y;
    private final double z;
    private final Entity source;
    private final float size;
    private final Map<Player, Vec3> playerKnockbackMap = Maps.newHashMap();
    private final Vec3 position;

    @OnlyIn(Dist.CLIENT)
    public NuclearExplosion(Level level, @Nullable Entity entity, BlockPos pos, float size) {
        this(level, entity, pos.getX(), pos.getY(), pos.getZ(), size, Mode.DESTROY);
    }

    @OnlyIn(Dist.CLIENT)
    public NuclearExplosion(Level level, @Nullable Entity entity, double x, double y, double z, float size, List<BlockPos> affectedPositions) {
        this(level, entity, x, y, z, size, Mode.DESTROY, affectedPositions);
    }

    @OnlyIn(Dist.CLIENT)
    public NuclearExplosion(Level level, @Nullable Entity entity, double x, double y, double z, float size, Mode mode, List<BlockPos> affectedPositions) {
        this(level, entity, x, y, z, size, mode);
        NuclearExplosion.affectedBlockPositions.addAll(affectedPositions);
    }

    public NuclearExplosion(Level level, @Nullable Entity entity, double x, double y, double z, float size, Mode mode) {
        super(level, entity, x, y, z, size, affectedBlockPositions);
        this.random = level.random;
        this.level = level;
        this.source = entity;
        this.size = size;
        this.x = x;
        this.y = y;
        this.z = z;
        this.mode = mode;
        this.position = new Vec3(this.x, this.y, this.z);

        if (MinecraftForge.EVENT_BUS.post(new NuclearExplosionEvent.Start(level, this))) return;
        explode();
    }

    public static float func_222259_a(Vec3 vec3, Entity entity) {
        AABB axisalignedbb = entity.getBoundingBox();
        double d0 = 1.0D / ((axisalignedbb.maxX - axisalignedbb.minX) * 2.0D + 1.0D);
        double d1 = 1.0D / ((axisalignedbb.maxY - axisalignedbb.minY) * 2.0D + 1.0D);
        double d2 = 1.0D / ((axisalignedbb.maxZ - axisalignedbb.minZ) * 2.0D + 1.0D);
        double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
        double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;
        if (!(d0 < 0.0D) && !(d1 < 0.0D) && !(d2 < 0.0D)) {
            int i = 0;
            int j = 0;

            for (float f = 0.0F; f <= 1.0F; f = (float) ((double) f + d0)) {
                for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float) ((double) f1 + d1)) {
                    for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float) ((double) f2 + d2)) {
                        double d5 = Mth.lerp(f, axisalignedbb.minX, axisalignedbb.maxX);
                        double d6 = Mth.lerp(f1, axisalignedbb.minY, axisalignedbb.maxY);
                        double d7 = Mth.lerp(f2, axisalignedbb.minZ, axisalignedbb.maxZ);
                        Vec3 vec3d = new Vec3(d5 + d3, d6, d7 + d4);
                        if (entity.level.clip(new ClipContext(vec3d, vec3, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getType() == BlockHitResult.Type.MISS) {
                            ++i;
                        }
                        ++j;
                    }
                }
            }
            return (float) i / (float) j;
        } else {
            return 0.0F;
        }
    }

    /**
     * Does the first part of the explosion (destroy blocks)
     */
    public void explode() {
        this.level.gameEvent(GameEvents.NUCLEAR_EXPLOSION, new BlockPos(this.position));
        Set<BlockPos> set = Sets.newHashSet();

        for (int j = 0; j < 50; ++j) {
            for (int k = 0; k < 35; ++k) {
                for (int l = 0; l < 45; ++l) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d0 = (float) j / 15.0F * 2.0F - 1.0F;
                        double d1 = (float) k / 15.0F * 2.0F - 1.0F;
                        double d2 = (float) l / 15.0F * 2.0F - 1.0F;
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 = d0 / d3;
                        d1 = d1 / d3;
                        d2 = d2 / d3;
                        float f = this.size * (0.7F + this.random.nextFloat() * 0.6F);
                        double d4 = this.x;
                        double d6 = this.y;
                        double d8 = this.z;

                        for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                            BlockPos blockpos = new BlockPos(d4, d6, d8);
                            BlockState blockstate = this.level.getBlockState(blockpos);
                            FluidState ifluidstate = this.level.getFluidState(blockpos);

                            if (!blockstate.isAir() || !ifluidstate.isEmpty()) {
                                float f2 = Math.max(blockstate.getExplosionResistance(this.level, blockpos, this), ifluidstate.getExplosionResistance(this.level, blockpos, this));
                                if (this.source != null) {
                                    f2 = this.source.getBlockExplosionResistance(this, this.level, blockpos, blockstate, ifluidstate, f2);
                                }
                                f -= (f2 + 0.3F) * 0.3F;
                            }

                            if (f > 0.0F && (this.source == null || this.source.shouldBlockExplode(this, this.level, blockpos, blockstate, f))) {
                                set.add(blockpos);
                                BiomeUtil.setBiomeAtPos(this.level, blockpos, BiomeInit.NUCLEAR, Registry::get);
                            }
                            d4 += d0 * (double) 0.3F;
                            d6 += d1 * (double) 0.3F;
                            d8 += d2 * (double) 0.3F;
                        }
                    }
                }
            }
        }

        affectedBlockPositions.addAll(set);
        float f3 = this.size * 2.0F;
        double k1 = Math.floor(this.x - (double) f3 - 1.0D);
        double l1 = Math.floor(this.x + (double) f3 + 1.0D);
        double i2 = Math.floor(this.y - (double) f3 - 1.0D);
        double i1 = Math.floor(this.y + (double) f3 + 1.0D);
        double j2 = Math.floor(this.z - (double) f3 - 1.0D);
        double j1 = Math.floor(this.z + (double) f3 + 1.0D);
        List<Entity> list = this.level.getEntities(this.source, new AABB(k1, i2, j2, l1, i1, j1));

        MinecraftForge.EVENT_BUS.post(new NuclearExplosionEvent.Detonate(this.level, this, list, affectedBlockPositions));

        for (Entity entity : list) {
            if (!entity.ignoreExplosion()) {
                double d12 = Math.sqrt(entity.distanceToSqr(this.position)) / f3;
                if (d12 <= 1.0D) {
                    double d5 = entity.getX() - this.x;
                    double d7 = entity.getY() + (double) entity.getEyeHeight() - this.y;
                    double d9 = entity.getZ() - this.z;
                    double d13 = Math.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
                    if (d13 != 0.0D) {
                        d5 = d5 / d13;
                        d7 = d7 / d13;
                        d9 = d9 / d13;
                        double d14 = func_222259_a(this.position, entity);
                        double d10 = (1.0D - d12) * d14;
                        entity.hurt(this.getDamageSource(), (float) ((int) ((d10 * d10 + d10) / 2.0D * 7.0D * (double) f3 + 1.0D)));
                        double d11 = d10;
                        if (entity instanceof LivingEntity) {
                            d11 = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity) entity, d10);
                        }

                        entity.setDeltaMovement(entity.getDeltaMovement().add(d5 * d11, d7 * d11, d9 * d11));
                        if (entity instanceof Player playerentity) {
                            if (!playerentity.isSpectator() && (!playerentity.isCreative() || !playerentity.abilities.flying)) {
                                this.playerKnockbackMap.put(playerentity, new Vec3(d5 * d10, d7 * d10, d9 * d10));
                            }
                        }
                    }
                }
            }
        }
        effects();
    }

    /**
     * Does the second part of the explosion (radioactivity and effects)
     */
    public void effects() {
        float halfradius = this.size / 2;
        float onepointfiveradius = halfradius * 3;
        float onepointfiveradiussqrd = onepointfiveradius * onepointfiveradius;
        float tworadius = this.size * 2;
        for (float X = -onepointfiveradius; X <= onepointfiveradius; X++) {
            double xx = this.x + X;
            float XX = X * X;
            for (float Z = -onepointfiveradius; Z <= onepointfiveradius; Z++) {
                float ZZ = Z * Z + XX;
                double zz = this.z + Z;
                for (float Y = -onepointfiveradius; Y <= onepointfiveradius; Y++) {
                    float YY = Y * Y + ZZ;
                    double yy = this.y + Y;
                    if (YY < onepointfiveradiussqrd) {
                        BlockPos blockPos = new BlockPos(xx, yy, zz);
                        BlockState blockState = this.level.getBlockState(blockPos);
                        Block block = blockState.getBlock();
                        if ((!Gravity.isAir(this.level, blockPos)) && (block != Blocks.BEDROCK)) {
                            int dist = (int) Math.sqrt(YY);
                            boolean flag = false;
                            if (dist < this.size) {
                                flag = true;
                                float varrand = 1 + dist - halfradius;
                                if ((varrand > 0) && (dist > halfradius)) {
                                    float randomness = halfradius - varrand / 2;
                                    if (block == Blocks.WATER || block == Blocks.LAVA) {
                                        this.level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                                    } else if (block == Blocks.STONE && this.random.nextFloat(randomness) < randomness / 2) {
                                        this.level.setBlockAndUpdate(blockPos, Blocks.COBBLESTONE.defaultBlockState());
                                    } else if ((block == Blocks.GRASS_BLOCK) || (block == Blocks.DIRT)) {
                                        this.level.setBlockAndUpdate(blockPos, BlockInit.RADIOACTIVE_DIRT.get().defaultBlockState());
                                    }
                                }
                            }
                            if (dist < onepointfiveradius) {
                                flag = true;
                                if ((Y >= tworadius) || (Y >= this.size) || (blockState.is(Tags.GLASS)) || (blockState.is(Tags.PANES))
                                        || (blockState.is(Tags.DOORS)) || (block == Blocks.TORCH) || (block == Blocks.WATER)) {
                                    this.level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                                } else if ((blockState.is(Tags.PLANKS)) || (blockState.is(Tags.STAIRS)) || (blockState.is(Tags.SLABS))) {
                                    this.level.setBlockAndUpdate(blockPos, Blocks.FIRE.defaultBlockState());
                                } else if (blockState.is(Tags.LOGS)) {
                                    if (this.random.nextFloat() > 0.5) {
                                        this.level.setBlockAndUpdate(blockPos, Blocks.FIRE.defaultBlockState());
                                    } else {
                                        this.level.setBlockAndUpdate(blockPos, BlockInit.CHARDWOOD_LOG.get().defaultBlockState());
                                    }
                                } else if ((block == Blocks.GRASS_BLOCK) || (block == Blocks.DIRT) || (block == Blocks.DIRT_PATH)) {
                                    this.level.setBlockAndUpdate(blockPos, BlockInit.RADIOACTIVE_DIRT.get().defaultBlockState());
                                }
                            }
                            if (flag) {
                                Gravity.applyGravity(this.level, blockPos);
                            }
                        }
                    }
                }
            }
        }
        cleanUp(false);
    }

    /**
     * Does the third part of the explosion (sound, particles, drop spawn)
     */
    public void cleanUp(boolean spawnParticles) {
        this.level.playSound(null, new BlockPos(this.position), SoundsInit.NUCLEAR_EXPLOSION.get(), SoundSource.BLOCKS, 0.6F, 1.0F);
        if (this.mode == Mode.NONE) return;

        for (BlockPos blockpos : affectedBlockPositions) {
            BlockState blockstate = this.level.getBlockState(blockpos);
            Block block = blockstate.getBlock();
            if (spawnParticles) {
                double d0 = (float) blockpos.getX() + this.random.nextFloat();
                double d1 = (float) blockpos.getY() + this.random.nextFloat();
                double d2 = (float) blockpos.getZ() + this.random.nextFloat();
                double d3 = d0 - this.x;
                double d4 = d1 - this.y;
                double d5 = d2 - this.z;
                double d6 = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                d3 = d3 / d6;
                d4 = d4 / d6;
                d5 = d5 / d6;
                double d7 = 0.5D / (d6 / (double) this.size + 0.1D);
                d7 = d7 * (double) (this.random.nextFloat() * this.random.nextFloat() + 0.3F);
                d3 = d3 * d7;
                d4 = d4 * d7;
                d5 = d5 * d7;
                this.level.addParticle(ParticleTypes.POOF, (d0 + this.x) / 2.0D, (d1 + this.y) / 2.0D, (d2 + this.z) / 2.0D, d3, d4, d5);
                this.level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, d3, d4, d5);
            }

            if (!blockstate.isAir()) {
                if (this.level instanceof ServerLevel && blockstate.canDropFromExplosion(this.level, blockpos, this)) {
                    BlockEntity blockEntity = blockstate.hasBlockEntity() ? this.level.getBlockEntity(blockpos) : null;
                    LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerLevel) this.level)).withRandom(this.random).withParameter(LootContextParams.ORIGIN, this.position).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity);
                    if (this.mode == Mode.DESTROY) {
                        lootcontext$builder.withParameter(LootContextParams.EXPLOSION_RADIUS, this.size);
                    }
                }
                this.level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 3);
                block.canDropFromExplosion(block.defaultBlockState(), this.level, blockpos, this);
            }
        }
    }

    public DamageSource getDamageSource() {
        return DamageSources.NUCLEAR_BLAST;
    }

    public Map<Player, Vec3> getPlayerKnockbackMap() {
        return this.playerKnockbackMap;
    }

    /**
     * Returns either the entity that placed the explosive block, the entity that caused the explosion or null.
     */
    @Nullable
    public LivingEntity getExplosivePlacedBy() {
        if (this.source == null) {
            return null;
        } else if (this.source instanceof PrimedTnt) {
            return ((PrimedTnt) this.source).getOwner();
        } else {
            return this.source instanceof LivingEntity ? (LivingEntity) this.source : null;
        }
    }

    public void clearAffectedBlockPositions() {
        affectedBlockPositions.clear();
    }

    public List<BlockPos> getAffectedBlockPositions() {
        return affectedBlockPositions;
    }

    public Vec3 getPosition() {
        return this.position;
    }

    public static enum Mode {
        NONE,
        BREAK,
        DESTROY;
    }
}