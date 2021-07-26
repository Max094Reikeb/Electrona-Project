package net.reikeb.electrona.entity;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import net.reikeb.electrona.events.local.EntityStruckByEnergeticLightningEvent;
import net.reikeb.electrona.misc.DamageSources;

import javax.annotation.Nullable;
import java.util.List;

public class EnergeticLightningBolt extends Entity {

    private int life;
    public long seed;
    private int flashes;
    private boolean visualOnly;
    @Nullable
    private ServerPlayer cause;

    public EnergeticLightningBolt(EntityType<? extends EnergeticLightningBolt> entityType, Level world) {
        super(entityType, world);
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

    public void setCause(@Nullable ServerPlayer p_204809_1_) {
        this.cause = p_204809_1_;
    }

    public void tick() {
        super.tick();
        if (this.life == 2) {
            Difficulty difficulty = this.level.getDifficulty();
            if (difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD) {
                this.spawnFire(4);
            }

            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 10000.0F, 0.8F + this.random.nextFloat() * 0.2F);
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.WEATHER, 2.0F, 0.5F + this.random.nextFloat() * 0.2F);
        }

        --this.life;
        if (this.life < 0) {
            if (this.flashes == 0) {
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
                double d0 = 3.0D;
                List<Entity> list = this.level.getEntities(this, new AABB(this.getX() - 3.0D, this.getY() - 3.0D, this.getZ() - 3.0D, this.getX() + 3.0D, this.getY() + 6.0D + 3.0D, this.getZ() + 3.0D), Entity::isAlive);

                for (Entity entity : list) {
                    if (!MinecraftForge.EVENT_BUS.post(new EntityStruckByEnergeticLightningEvent(entity, this))) {
                        entity.hurt(DamageSources.ENERGETIC_LIGHTNING_BOLT, 5.0F);
                    }
                }

                if (this.cause != null) {
                    CriteriaTriggers.CHANNELED_LIGHTNING.trigger(this.cause, list);
                }
            }
        }

    }

    private void spawnFire(int fireLevel) {
        if (!this.visualOnly && !this.level.isClientSide && this.level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            BlockPos blockpos = this.blockPosition();
            BlockState blockstate = BaseFireBlock.getState(this.level, blockpos);
            if (this.level.getBlockState(blockpos).isAir() && blockstate.canSurvive(this.level, blockpos)) {
                this.level.setBlockAndUpdate(blockpos, blockstate);
            }

            for (int i = 0; i < fireLevel; ++i) {
                BlockPos blockpos1 = blockpos.offset(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);
                blockstate = BaseFireBlock.getState(this.level, blockpos1);
                if (this.level.getBlockState(blockpos1).isAir() && blockstate.canSurvive(this.level, blockpos1)) {
                    this.level.setBlockAndUpdate(blockpos1, blockstate);
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
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
