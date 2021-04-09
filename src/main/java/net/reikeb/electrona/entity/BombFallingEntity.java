package net.reikeb.electrona.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.world.*;

import net.reikeb.electrona.world.NuclearExplosion;

public class BombFallingEntity extends FallingBlockEntity {

    boolean isCharged;

    public BombFallingEntity(EntityType<? extends FallingBlockEntity> p_i50218_1_, World p_i50218_2_) {
        super(p_i50218_1_, p_i50218_2_);
    }

    public BombFallingEntity(World worldIn, double v, double y, double v1, BlockState blockState, boolean isCharged) {
        super(worldIn, v, y, v1, blockState);
        this.isCharged = isCharged;
    }

    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        if (p_225503_1_ >= 3.0F && this.isCharged) {
            new NuclearExplosion(this.level, this.blockPosition().getX(),
                    this.blockPosition().getY(), this.blockPosition().getZ(), 84);
        }

        return super.causeFallDamage(p_225503_1_, p_225503_2_);
    }
}
