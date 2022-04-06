package net.reikeb.electrona.world;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.PacketDistributor;

import net.reikeb.electrona.events.local.NuclearExplosionEvent;
import net.reikeb.electrona.init.BiomeInit;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.init.SoundsInit;
import net.reikeb.electrona.misc.DamageSources;
import net.reikeb.electrona.misc.Tags;
import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.BiomeUpdatePacket;
import net.reikeb.electrona.utils.BiomeUtil;
import net.reikeb.electrona.utils.Gravity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    private final List<Block> affectedBlocks = new ArrayList<>();
    private final Vec3 position;

    public NuclearExplosion(Level world, int x, int y, int z, int strength) {
        this.position = new Vec3(x, y, z);
        if (!world.isClientSide) {
            if (!MinecraftForge.EVENT_BUS.post(new NuclearExplosionEvent.Start(world, this))) {
                createHole(world, x, y, z, strength);
                pushAndHurtEntities(world, x, y, z, strength);
                fixLag(world, x, y, z, strength);
            }
        }
    }

    public Vec3 getPosition() {
        return this.position;
    }

    private void createHole(Level world, int x, int y, int z, int radius) {
        Random rand = new Random();
        int halfradius = radius / 2;
        int onepointfiveradius = halfradius * 3;
        int onepointfiveradiussqrd = onepointfiveradius * onepointfiveradius;
        int tworadius = radius * 2;

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
                        BlockPos blockPos = new BlockPos(xx, yy, zz);
                        Block block = world.getBlockState(blockPos).getBlock();
                        if ((!Gravity.isAir(world, blockPos)) && (block != Blocks.BEDROCK)) {
                            int dist = (int) Math.sqrt(YY);
                            boolean flag = false;
                            if (dist < radius) {
                                flag = true;
                                affectedBlocks.add(block);
                                int varrand = 1 + dist - halfradius;
                                if (dist < halfradius) {
                                    world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                                    block = Blocks.AIR;
                                } else if (varrand > 0) {
                                    int randomness = halfradius - varrand / 2;
                                    if (block == Blocks.WATER || block == Blocks.LAVA) {
                                        world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                                        block = Blocks.AIR;
                                    } else if (block == Blocks.STONE && rand.nextInt(randomness) < randomness / 2) {
                                        world.setBlockAndUpdate(blockPos, Blocks.COBBLESTONE.defaultBlockState());
                                        block = Blocks.COBBLESTONE;
                                    } else if ((block == Blocks.GRASS_BLOCK) || (block == Blocks.DIRT)) {
                                        world.setBlockAndUpdate(blockPos, BlockInit.RADIOACTIVE_DIRT.get().defaultBlockState());
                                    } else if ((rand.nextInt(varrand) == 0 || rand.nextInt(varrand / 2 + 1) == 0)) {
                                        world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                                        block = Blocks.AIR;
                                    }
                                }
                            }
                            if (dist < onepointfiveradius) {
                                flag = true;
                                affectedBlocks.add(block);
                                if ((Y >= tworadius) || (Y >= radius) || (Tags.GLASS.contains(block)) || (Tags.PANES.contains(block))
                                        || (Tags.DOORS.contains(block)) || (block == Blocks.TORCH) || (block == Blocks.WATER)) {
                                    world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                                } else if ((Tags.PLANKS.contains(block)) || (Tags.STAIRS.contains(block)) || (Tags.SLABS.contains(block))) {
                                    world.setBlockAndUpdate(blockPos, Blocks.FIRE.defaultBlockState());
                                } else if (Tags.LOG_THAT_BURN.contains(block)) {
                                    if (world.random.nextFloat() > 0.5) {
                                        world.setBlockAndUpdate(blockPos, Blocks.FIRE.defaultBlockState());
                                    } else {
                                        world.setBlockAndUpdate(blockPos, BlockInit.CHARDWOOD_LOG.get().defaultBlockState());
                                    }
                                } else if ((block == Blocks.GRASS_BLOCK) || (block == Blocks.DIRT) || (block == Blocks.DIRT_PATH)) {
                                    world.setBlockAndUpdate(blockPos, BlockInit.RADIOACTIVE_DIRT.get().defaultBlockState());
                                }
                            }
                            if (flag) {
                                Gravity.applyGravity(world, blockPos);
                                BiomeUtil.setBiomeKeyAtPos(world, blockPos, BiomeInit.NUCLEAR);
                            }
                        }
                    }
                }
            }
        }
        world.playSound(null, new BlockPos(x, y, z), SoundsInit.NUCLEAR_EXPLOSION.get(),
                SoundSource.WEATHER, 0.6F, 1.0F);
        NetworkManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new BiomeUpdatePacket(new BlockPos(x, y, z), BiomeInit.NUCLEAR.location(), radius));
    }

    private void pushAndHurtEntities(Level world, int x, int y, int z, int radius) {
        int diameter = radius * 2;
        int onepointfiveradius = (radius / 2) * 3;
        int var3 = Mth.floor(x - (double) onepointfiveradius - 1.0D);
        int var4 = Mth.floor(x + (double) onepointfiveradius + 1.0D);
        int var5 = Mth.floor(y - (double) onepointfiveradius - 1.0D);
        int var28 = Mth.floor(y + (double) onepointfiveradius + 1.0D);
        int var7 = Mth.floor(z - (double) onepointfiveradius - 1.0D);
        int var29 = Mth.floor(z + (double) onepointfiveradius + 1.0D);
        List<Entity> var9 = world.getEntities(null, AABB.of(new BoundingBox(var3, var5, var7, var4, var28, var29)));
        MinecraftForge.EVENT_BUS.post(new NuclearExplosionEvent.Detonate(world, this, var9, affectedBlocks));
        Vec3 var30 = new Vec3(x, y, z);

        for (Entity entity : var9) {
            double var13 = entity.distanceToSqr(x, y, z) / onepointfiveradius;
            if (var13 <= 1.0D) {
                double var15 = entity.getX() - x;
                double var17 = entity.getY() + entity.getEyeHeight() - y;
                double var19 = entity.getZ() - z;
                double var33 = Mth.sqrt((float) (var15 * var15 + var17 * var17 + var19 * var19));

                if (var33 != 0.0D) {
                    var15 /= var33;
                    var17 /= var33;
                    var19 /= var33;
                    double var32 = Explosion.getSeenPercent(var30, entity);
                    double var34 = (1.0D - var13) * var32;
                    if (entity instanceof ItemEntity) entity.discard();
                    if (!(entity instanceof FallingBlockEntity)) {
                        entity.hurt(DamageSources.NUCLEAR_BLAST, (float) (int) ((var34 * var34 + var34) / 2.0D * 8.0D * (double) diameter + 1.0D) * 8);
                    }
                    double var35 = var34;
                    if (entity instanceof LivingEntity) {
                        var35 = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity) entity, var34);
                    }

                    entity.setDeltaMovement(entity.getDeltaMovement().add(var15 * var35 * 8, var17 * var35 * 8, var19 * var35 * 8));
                }
            }
        }
    }

    private void fixLag(Level world, int x, int y, int z, int strength) {
        for (int X = -strength; X <= strength; X++) {
            int xx = x + X;
            for (int Y = -strength; Y <= strength; Y++) {
                int yy = y + Y;
                for (int Z = -strength; Z <= strength; Z++) {
                    int zz = z + Z;
                    BlockPos blockPos = new BlockPos(xx, yy, zz);
                    Block blockID = world.getBlockState(blockPos).getBlock();
                    if (blockID == Blocks.AIR && world.getLightEmission(blockPos) == 0) {
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
                            world.setBlockAndUpdate(blockPos, id.defaultBlockState());
                        }
                    }
                }
            }
        }
    }
}
