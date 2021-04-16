package net.reikeb.electrona.world;

import net.minecraft.block.*;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;

import net.minecraftforge.common.MinecraftForge;

import net.reikeb.electrona.events.local.NuclearExplosionEvent;
import net.reikeb.electrona.init.*;
import net.reikeb.electrona.utils.ElectronaUtils;

import java.util.*;

/**
 * Credits: rodolphito
 * https://github.com/rodolphito/Rival-Rebels-Mod/blob/master/main/java/assets/rivalrebels/common/explosion/NuclearExplosion.java
 */
public class NuclearExplosion {

    public static Block[] prblocks = {
            Blocks.COAL_ORE,
            Blocks.IRON_ORE,
            Blocks.REDSTONE_ORE,
            Blocks.GOLD_ORE,
            Blocks.LAPIS_ORE,
            Blocks.DIAMOND_ORE,
            Blocks.EMERALD_ORE,
    };

    public static Block[] pgblocks = {
            Blocks.STONE,
            Blocks.COBBLESTONE,
            Blocks.DIRT,
    };

    public NuclearExplosion(World world, int x, int y, int z, int strength) {
        if (!world.isClientSide) {
            if (!MinecraftForge.EVENT_BUS.post(new NuclearExplosionEvent.Start(world, this))) {
                createHole(world, x, y, z, strength);
                pushAndHurtEntities(world, x, y, z, strength);
                fixLag(world, x, y, z, strength);
            }
        }
    }

    private void createHole(World world, int x, int y, int z, int radius) {
        ITagCollection<Block> tagCollection = BlockTags.getAllTags();
        ITag<Block> logTag, plankTag;
        logTag = tagCollection.getTagOrEmpty(new ResourceLocation("minecraft", "logs_that_burn"));
        plankTag = tagCollection.getTagOrEmpty(new ResourceLocation("minecraft", "planks"));

        Random rand = new Random();
        int halfradius = radius / 2;
        int onepointfiveradius = halfradius * 3;
        int AOC = radius;
        int onepointfiveradiussqrd = onepointfiveradius * onepointfiveradius;
        int twoAOC = AOC * 2;

        for (int X = -onepointfiveradius; X <= onepointfiveradius; X++) {
            int xx = x + X;
            int XX = X * X;
            for (int Z = -onepointfiveradius; Z <= onepointfiveradius; Z++) {
                int ZZ = Z * Z + XX;
                int zz = z + Z;
                for (int Y = -onepointfiveradius; Y <= onepointfiveradius; Y++) {
                    int YY = Y * Y + ZZ;
                    int yy = y + Y;
                    if (YY < onepointfiveradiussqrd) {
                        Block block = world.getBlockState(new BlockPos(xx, yy, zz)).getBlock();
                        if (block != Blocks.AIR) {
                            int dist = (int) Math.sqrt(YY);
                            if (dist < radius && block != Blocks.BEDROCK) {
                                int varrand = 1 + dist - halfradius;
                                if (dist < halfradius) {
                                    world.setBlockAndUpdate(new BlockPos(xx, yy, zz), Blocks.AIR.defaultBlockState());
                                    block = Blocks.AIR;
                                } else if (varrand > 0) {
                                    int randomness = halfradius - varrand / 2;
                                    if (block == Blocks.WATER || block == Blocks.LAVA) {
                                        world.setBlockAndUpdate(new BlockPos(xx, yy, zz), Blocks.AIR.defaultBlockState());
                                        block = Blocks.AIR;
                                    } else if (block == Blocks.STONE && rand.nextInt(randomness) < randomness / 2) {
                                        world.setBlockAndUpdate(new BlockPos(xx, yy, zz), Blocks.COBBLESTONE.defaultBlockState());
                                        block = Blocks.COBBLESTONE;
                                    } else if ((block == Blocks.GRASS_BLOCK) || (block == Blocks.DIRT)) {
                                        world.setBlockAndUpdate(new BlockPos(xx, yy, zz), BlockInit.RADIOACTIVE_DIRT.get().defaultBlockState());
                                    } else if ((rand.nextInt(varrand) == 0 || rand.nextInt(varrand / 2 + 1) == 0)) {
                                        world.setBlockAndUpdate(new BlockPos(xx, yy, zz), Blocks.AIR.defaultBlockState());
                                        block = Blocks.AIR;
                                    }
                                }
                            }
                            if (dist < onepointfiveradius && block != Blocks.AIR && block != Blocks.BEDROCK) {
                                if (Y >= twoAOC || (dist < onepointfiveradius && Y >= AOC)) {
                                    world.setBlockAndUpdate(new BlockPos(xx, yy, zz), Blocks.AIR.defaultBlockState());
                                } else if (logTag.contains(world.getBlockState(new BlockPos(xx, yy - 1, zz)).getBlock())
                                        || plankTag.contains(world.getBlockState(new BlockPos(xx, yy - 1, zz)).getBlock())) {
                                    world.setBlockAndUpdate(new BlockPos(xx, yy, zz), Blocks.FIRE.defaultBlockState());
                                } else if ((block == Blocks.GRASS_BLOCK) || (block == Blocks.DIRT)) {
                                    world.setBlockAndUpdate(new BlockPos(xx, yy, zz), BlockInit.RADIOACTIVE_DIRT.get().defaultBlockState());
                                }
                            }
                        }
                    }
                }
            }
        }
        ElectronaUtils.playSound(world, new BlockPos(x, y, z), SoundsInit.NUCLEAR_EXPLOSION.get(), SoundCategory.WEATHER);
    }

    private void pushAndHurtEntities(World world, int x, int y, int z, int radius) {
        int diameter = radius * 2;
        int onepointfiveradius = (radius / 2) * 3;
        int var3 = MathHelper.floor(x - (double) onepointfiveradius - 1.0D);
        int var4 = MathHelper.floor(x + (double) onepointfiveradius + 1.0D);
        int var5 = MathHelper.floor(y - (double) onepointfiveradius - 1.0D);
        int var28 = MathHelper.floor(y + (double) onepointfiveradius + 1.0D);
        int var7 = MathHelper.floor(z - (double) onepointfiveradius - 1.0D);
        int var29 = MathHelper.floor(z + (double) onepointfiveradius + 1.0D);
        List<Entity> var9 = world.getEntities(null, AxisAlignedBB.of(new MutableBoundingBox(var3, var5, var7, var4, var28, var29)));
        MinecraftForge.EVENT_BUS.post(new NuclearExplosionEvent.Detonate(world, this, var9));
        Vector3d var30 = new Vector3d(x, y, z);

        for (Entity entity : var9) {
            double var13 = entity.distanceToSqr(x, y, z) / onepointfiveradius;
            if (var13 <= 1.0D) {
                double var15 = entity.getX() - x;
                double var17 = entity.getY() + entity.getEyeHeight() - y;
                double var19 = entity.getZ() - z;
                double var33 = MathHelper.sqrt(var15 * var15 + var17 * var17 + var19 * var19);

                if (var33 != 0.0D) {
                    var15 /= var33;
                    var17 /= var33;
                    var19 /= var33;
                    double var32 = Explosion.getSeenPercent(var30, entity);
                    double var34 = (1.0D - var13) * var32;
                    if (entity instanceof FallingBlockEntity) entity.remove();
                    if (entity instanceof ItemEntity) entity.remove();
                    entity.hurt(new DamageSource("nuclear_blast"), (float) (int) ((var34 * var34 + var34) / 2.0D * 8.0D * (double) diameter + 1.0D) * 8);
                    double var35 = var34;
                    if (entity instanceof LivingEntity) {
                        var35 = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity) entity, var34);
                    }

                    entity.setDeltaMovement(entity.getDeltaMovement().add(var15 * var35 * 8, var17 * var35 * 8, var19 * var35 * 8));
                }
            }
        }
    }

    private void fixLag(World world, int x, int y, int z, int strength) {
        for (int X = -strength; X <= strength; X++) {
            int xx = x + X;
            for (int Y = -strength; Y <= strength; Y++) {
                int yy = y + Y;
                for (int Z = -strength; Z <= strength; Z++) {
                    int zz = z + Z;
                    Block blockID = world.getBlockState(new BlockPos(xx, yy, zz)).getBlock();
                    if (blockID == Blocks.AIR && world.getLightEmission(new BlockPos(xx, yy, zz)) == 0) {
                        if (world.getBlockState(new BlockPos(xx, yy + 1, zz)).getBlock() != Blocks.AIR
                                && world.getBlockState(new BlockPos(xx, yy - 1, zz)).getBlock() != Blocks.AIR
                                && world.getBlockState(new BlockPos(xx + 1, yy, zz)).getBlock() != Blocks.AIR
                                && world.getBlockState(new BlockPos(xx - 1, yy, zz)).getBlock() != Blocks.AIR
                                && world.getBlockState(new BlockPos(xx, yy, zz + 1)).getBlock() != Blocks.AIR
                                && world.getBlockState(new BlockPos(xx, yy, zz - 1)).getBlock() != Blocks.AIR) {
                            int r = world.random.nextInt(50);
                            Block id;
                            if (r == 0) {
                                id = prblocks[world.random.nextInt(prblocks.length)];
                            } else {
                                id = pgblocks[world.random.nextInt(pgblocks.length)];
                            }
                            world.setBlockAndUpdate(new BlockPos(xx, yy, zz), id.defaultBlockState());
                        }
                    }
                }
            }
        }
    }
}
