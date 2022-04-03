package net.reikeb.electrona.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.world.Gamerules;
import net.reikeb.electrona.world.NuclearExplosion;

public class BombFallingEntity extends FallingBlockEntity {

    boolean isCharged;
    int nuclearCharge;

    public BombFallingEntity(EntityType<? extends FallingBlockEntity> entityType, Level level) {
        super(entityType, level);
    }

    public BombFallingEntity(Level worldIn, double v, double y, double v1, BlockState blockState, boolean isCharged, int nuclearCharge) {
        super(worldIn, v, y, v1, blockState);
        this.isCharged = isCharged;
        this.nuclearCharge = nuclearCharge;
    }

    public boolean causeFallDamage(float height, float p_225503_2_, DamageSource damageSource) {
        if (height >= 3.0F && this.isCharged
                && this.level.getLevelData().getGameRules().getBoolean(Gamerules.DO_NUCLEAR_BOMBS_EXPLODE)) {
            new NuclearExplosion(this.level, this.blockPosition().getX(),
                    this.blockPosition().getY(), this.blockPosition().getZ(), nuclearCharge);
        }

        return super.causeFallDamage(height, p_225503_2_, damageSource);
    }
}
