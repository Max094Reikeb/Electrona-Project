package net.reikeb.electrona.misc.vm;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.reikeb.electrona.gempower.PowerUtils;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.init.ParticleInit;
import net.reikeb.electrona.misc.GameEvents;
import net.reikeb.electrona.misc.Tags;
import net.reikeb.electrona.world.Gamerules;
import net.reikeb.maxilib.utils.Utils;

public class BlackHoleFunction {

    /**
     * Method that handles the Black Hole expansion
     *
     * @param world      The world
     * @param pos        The position of the Hole
     * @param directions The directions the Hole expands
     */
    public static void HoleProcedure(Level world, BlockPos pos, Direction[] directions) {
        if (world.isClientSide) return;

        if (world.getLevelData().getGameRules().getBoolean(Gamerules.DO_BLACK_HOLES_EXIST)) {
            for (Direction dir : directions) {
                BlockState offsetBlockState = world.getBlockState(pos.relative(dir));

                if ((Math.random() >= 0.5) && (!offsetBlockState.is(Tags.STOPS_BLACK_HOLE))) {
                    world.setBlock(pos.relative(dir), BlockInit.HOLE.get().defaultBlockState(), 3);
                }
                if (!world.getBlockState(pos.below()).is(Tags.STOPS_BLACK_HOLE)) {
                    world.setBlock(pos.below(), BlockInit.HOLE.get().defaultBlockState(), 3);
                }
                if (!world.getBlockState(pos.above()).is(Tags.STOPS_BLACK_HOLE)) {
                    world.setBlock(pos.above(), BlockInit.HOLE.get().defaultBlockState(), 3);
                }
            }
        }
        if (BlockInit.SINGULARITY.get() != world.getBlockState(pos).getBlock()) {
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }
    }

    /**
     * Method that handles Singularity's particles
     *
     * @param world The world of the Singularity
     * @param pos   The position of the Singularity
     */
    public static void singularityParticles(Level world, BlockPos pos) {
        if (world.isClientSide) return;

        AreaEffectCloud areaEffectCloudEntity = new AreaEffectCloud(world, pos.getX(), pos.getY(), pos.getZ());

        for (LivingEntity entityiterator : Utils.getLivingEntitiesInRadius(world, pos, 100)) {
            areaEffectCloudEntity.setOwner(entityiterator);
        }

        areaEffectCloudEntity.setParticle(ParticleInit.DARK_MATTER.get());
        areaEffectCloudEntity.setRadius(5.0F);
        areaEffectCloudEntity.setDuration(60);
        areaEffectCloudEntity.setRadiusPerTick((7.0F - areaEffectCloudEntity.getRadius()) / (float) areaEffectCloudEntity.getDuration());
        areaEffectCloudEntity.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 1));

        world.levelEvent(2006, pos, 1);
        world.addFreshEntity(areaEffectCloudEntity);
        world.gameEvent(GameEvents.SINGULARITY, pos);
    }

    /**
     * Method that handles Singularity's delay
     *
     * @param world The world of the Singularity
     * @param pos   The position of the Singularity
     */
    public static void singularityDelay(Level world, BlockPos pos) {
        if (world.isClientSide) return;
        ItemStack stack = new ItemStack(ItemInit.COSMIC_GEM.get(), 1);
        PowerUtils.setRandomPower(stack);
        ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        itemEntity.setPickUpDelay(10);
        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        world.addFreshEntity(itemEntity);
    }
}
