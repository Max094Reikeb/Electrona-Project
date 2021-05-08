package net.reikeb.electrona.entity;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;

import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.NetworkHooks;

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
    private ServerPlayerEntity cause;

    public EnergeticLightningBolt(EntityType<? extends EnergeticLightningBolt> entityType, World world) {
        super(entityType, world);
        this.noCulling = true;
        this.life = 2;
        this.seed = this.random.nextLong();
        this.flashes = this.random.nextInt(3) + 1;
    }

    public void setVisualOnly(boolean isVisual) {
        this.visualOnly = isVisual;
    }

    public SoundCategory getSoundSource() {
        return SoundCategory.WEATHER;
    }

    public void setCause(@Nullable ServerPlayerEntity p_204809_1_) {
        this.cause = p_204809_1_;
    }

    public void tick() {
        super.tick();
        if (this.life == 2) {
            Difficulty difficulty = this.level.getDifficulty();
            if (difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD) {
                this.spawnFire(4);
            }

            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 10000.0F, 0.8F + this.random.nextFloat() * 0.2F);
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundCategory.WEATHER, 2.0F, 0.5F + this.random.nextFloat() * 0.2F);
        }

        --this.life;
        if (this.life < 0) {
            if (this.flashes == 0) {
                this.remove();
            } else if (this.life < -this.random.nextInt(10)) {
                --this.flashes;
                this.life = 1;
                this.seed = this.random.nextLong();
                this.spawnFire(0);
            }
        }

        if (this.life >= 0) {
            if (!(this.level instanceof ServerWorld)) {
                this.level.setSkyFlashTime(2);
            } else if (!this.visualOnly) {
                double d0 = 3.0D;
                List<Entity> list = this.level.getEntities(this, new AxisAlignedBB(this.getX() - 3.0D, this.getY() - 3.0D, this.getZ() - 3.0D, this.getX() + 3.0D, this.getY() + 6.0D + 3.0D, this.getZ() + 3.0D), Entity::isAlive);

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
            BlockState blockstate = AbstractFireBlock.getState(this.level, blockpos);
            if (this.level.getBlockState(blockpos).isAir() && blockstate.canSurvive(this.level, blockpos)) {
                this.level.setBlockAndUpdate(blockpos, blockstate);
            }

            for (int i = 0; i < fireLevel; ++i) {
                BlockPos blockpos1 = blockpos.offset(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);
                blockstate = AbstractFireBlock.getState(this.level, blockpos1);
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

    protected void readAdditionalSaveData(CompoundNBT nbt) {
    }

    protected void addAdditionalSaveData(CompoundNBT nbt) {
    }

    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
