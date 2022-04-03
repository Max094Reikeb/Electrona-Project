package net.reikeb.electrona.misc.vm;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollection;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.init.ParticleInit;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.electrona.utils.GemPower;
import net.reikeb.electrona.world.Gamerules;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        TagCollection<Block> tagCollection = BlockTags.getAllTags();
        Tag<Block> stopsHoleTag = tagCollection.getTagOrEmpty(Keys.STOPS_BLACK_HOLES_TAG);

        if (world.getLevelData().getGameRules().getBoolean(Gamerules.DO_BLACK_HOLES_EXIST)) {
            for (Direction dir : directions) {
                Block offsetBlock = world.getBlockState(pos.relative(dir)).getBlock();
                BlockPos belowPos = new BlockPos(pos.getX(), (pos.getY() - 1), pos.getZ());
                BlockPos abovePos = new BlockPos(pos.getX(), (pos.getY() + 1), pos.getZ());

                if ((Math.random() >= 0.5) && (!stopsHoleTag.contains(offsetBlock))) {
                    world.setBlock(pos.relative(dir), BlockInit.HOLE.get().defaultBlockState(), 3);
                }
                if (!stopsHoleTag.contains(world.getBlockState(belowPos).getBlock())) {
                    world.setBlock(belowPos, BlockInit.HOLE.get().defaultBlockState(), 3);
                }
                if (!stopsHoleTag.contains(world.getBlockState(abovePos).getBlock())) {
                    world.setBlock(abovePos, BlockInit.HOLE.get().defaultBlockState(), 3);
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
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        if (world.isClientSide) return;

        AreaEffectCloud areaEffectCloudEntity = new AreaEffectCloud(world, x, y, z);

        List<LivingEntity> livingEntities = world.getEntitiesOfClass(LivingEntity.class,
                new AABB(x - 100, y - 100, z - 100,
                        x + 100, y + 100, z + 100),
                EntitySelector.LIVING_ENTITY_STILL_ALIVE).stream().sorted(new Object() {
            Comparator<Entity> compareDistOf(double x, double y, double z) {
                return Comparator.comparing(_entcnd -> _entcnd.distanceToSqr(x, y, z));
            }
        }.compareDistOf(x, y, z)).collect(Collectors.toList());
        for (LivingEntity entityiterator : livingEntities) {
            areaEffectCloudEntity.setOwner(entityiterator);
        }

        areaEffectCloudEntity.setParticle(ParticleInit.DARK_MATTER.get());
        areaEffectCloudEntity.setRadius(5.0F);
        areaEffectCloudEntity.setDuration(60);
        areaEffectCloudEntity.setRadiusPerTick((7.0F - areaEffectCloudEntity.getRadius()) / (float) areaEffectCloudEntity.getDuration());
        areaEffectCloudEntity.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 1));

        world.levelEvent(2006, pos, 1);
        world.addFreshEntity(areaEffectCloudEntity);
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
        stack.getOrCreateTag().putString("power", GemPower.randomPowerId());
        ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        itemEntity.setPickUpDelay(10);
        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        world.addFreshEntity(itemEntity);
    }
}
