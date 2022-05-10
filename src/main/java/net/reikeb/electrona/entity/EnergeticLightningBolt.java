package net.reikeb.electrona.entity;

import com.google.common.collect.Sets;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.reikeb.electrona.advancements.TTriggers;
import net.reikeb.electrona.blockentities.EnergeticLightningRodBlockEntity;
import net.reikeb.electrona.events.local.EntityStruckByEnergeticLightningEvent;
import net.reikeb.electrona.misc.DamageSources;
import net.reikeb.electrona.misc.GameEvents;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class EnergeticLightningBolt extends Entity {

    private final Set<Entity> hitEntities = Sets.newHashSet();
    public long seed;
    private int life;
    private int flashes;
    private boolean visualOnly;
    @Nullable
    private ServerPlayer cause;
    private int blocksSetOnFire;

    public EnergeticLightningBolt(EntityType<? extends EnergeticLightningBolt> entityType, Level level) {
        super(entityType, level);
        this.noCulling = true;
        this.life = 2;
        this.seed = this.random.nextLong();
        this.flashes = this.random.nextInt(3) + 1;
    }

    public void setVisualOnly(boolean isVisual) {
        this.visualOnly = isVisual;
    }

    public SoundSource getSoundSource() {
        return SoundSource.WEATHER;
    }

    public void setCause(@Nullable ServerPlayer serverPlayer) {
        this.cause = serverPlayer;
    }

    private BlockPos getStrikePosition() {
        Vec3 vec3 = this.position();
        return new BlockPos(vec3.x, vec3.y - 1.0E-6D, vec3.z);
    }

    private void powerEnergeticLightningRod() {
        BlockEntity blockEntity = this.level.getBlockEntity(this.getStrikePosition().above());
        if (blockEntity instanceof EnergeticLightningRodBlockEntity energeticLightningRodBlockEntity) {
            energeticLightningRodBlockEntity.struckByLightning();
        }
    }

    public void tick() {
        super.tick();
        if (this.life == 2) {
            if (this.level.isClientSide()) {
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 10000.0F, 0.8F + this.random.nextFloat() * 0.2F, false);
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.WEATHER, 2.0F, 0.5F + this.random.nextFloat() * 0.2F, false);
            } else {
                Difficulty difficulty = this.level.getDifficulty();
                if (difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD) {
                    this.spawnFire(4);
                }

                this.powerEnergeticLightningRod();
                this.gameEvent(GameEvents.ENERGETIC_LIGHTNING_STRIKE);
            }
        }

        --this.life;
        if (this.life < 0) {
            if (this.flashes == 0) {
                if (this.level instanceof ServerLevel serverLevel) {
                    List<Entity> list = this.level.getEntities(this,
                            new AABB(this.getX() - 15.0D, this.getY() - 15.0D,
                                    this.getZ() - 15.0D, this.getX() + 15.0D,
                                    this.getY() + 6.0D + 15.0D, this.getZ() + 15.0D),
                            (entity) -> entity.isAlive() && !this.hitEntities.contains(entity));

                    for (ServerPlayer serverPlayer : serverLevel.getPlayers((serverPlayer) -> serverPlayer.distanceTo(this) < 256.0F)) {
                        TTriggers.ENERGETIC_LIGHTNING_STRIKE.trigger(serverPlayer, this, list);
                    }
                }
                this.discard();
            } else if (this.life < -this.random.nextInt(10)) {
                --this.flashes;
                this.life = 1;
                this.seed = this.random.nextLong();
                this.spawnFire(0);
            }
        }

        if (this.life >= 0) {
            if (!(this.level instanceof ServerLevel)) {
                this.level.setSkyFlashTime(2);
            } else if (!this.visualOnly) {
                List<Entity> list = this.level.getEntities(this, new AABB(this.getX() - 3.0D, this.getY() - 3.0D, this.getZ() - 3.0D, this.getX() + 3.0D, this.getY() + 6.0D + 3.0D, this.getZ() + 3.0D), Entity::isAlive);

                for (Entity entity : list) {
                    if (!MinecraftForge.EVENT_BUS.post(new EntityStruckByEnergeticLightningEvent(entity, this))) {
                        entity.hurt(DamageSources.ENERGETIC_LIGHTNING_BOLT, 5.0F);
                    }
                }

                this.hitEntities.addAll(list);
                if (this.cause != null) {
                    CriteriaTriggers.CHANNELED_LIGHTNING.trigger(this.cause, list);
                }
            }
        }

    }

    private void spawnFire(int fireLevel) {
        if (!this.visualOnly && !this.level.isClientSide && this.level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            BlockPos blockPos = this.blockPosition();
            BlockState blockState = BaseFireBlock.getState(this.level, blockPos);
            if (this.level.getBlockState(blockPos).isAir() && blockState.canSurvive(this.level, blockPos)) {
                this.level.setBlockAndUpdate(blockPos, blockState);
                ++this.blocksSetOnFire;
            }

            for (int i = 0; i < fireLevel; ++i) {
                BlockPos pos = blockPos.offset(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);
                blockState = BaseFireBlock.getState(this.level, pos);
                if (this.level.getBlockState(pos).isAir() && blockState.canSurvive(this.level, pos)) {
                    this.level.setBlockAndUpdate(pos, blockState);
                    ++this.blocksSetOnFire;
                }
            }

        }
    }

    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderAtSqrDistance(double minDist) {
        double d0 = 64.0D * getViewScale();
        return minDist < d0 * d0;
    }

    protected void defineSynchedData() {
    }

    protected void readAdditionalSaveData(CompoundTag nbt) {
    }

    protected void addAdditionalSaveData(CompoundTag nbt) {
    }

    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    public int getBlocksSetOnFire() {
        return this.blocksSetOnFire;
    }

    public Stream<Entity> getHitEntities() {
        return this.hitEntities.stream().filter(Entity::isAlive);
    }
}
