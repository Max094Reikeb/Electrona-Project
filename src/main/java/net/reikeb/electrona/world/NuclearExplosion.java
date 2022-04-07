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
import net.reikeb.electrona.misc.GameEvents;
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

    private final List<Block> affectedBlocks = new ArrayList<>();
    private final BlockPos blockPos;
    private final Vec3 position;
    private final Level level;
    private final int strength;

    public NuclearExplosion(Level level, BlockPos blockPos, int strength) {
        this(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), strength);
    }

    public NuclearExplosion(Level level, int x, int y, int z, int strength) {
        this.position = new Vec3(x, y, z);
        this.blockPos = new BlockPos(x, y, z);
        this.level = level;
        this.strength = strength;
        if (level.isClientSide) return;
        if (!MinecraftForge.EVENT_BUS.post(new NuclearExplosionEvent.Start(level, this))) {
            createHole();
            pushAndHurtEntities();
            fixLag();
        }
    }

    public BlockPos getBlockPos() {
        return this.getBlockPos();
    }

    public Vec3 getPosition() {
        return this.position;
    }

    public Level getLevel() {
        return this.level;
    }

    public int getStrength() {
        return this.strength;
    }

    private void createHole() {
        Random rand = new Random();
        int halfradius = this.strength / 2;
        int onepointfiveradius = halfradius * 3;
        int onepointfiveradiussqrd = onepointfiveradius * onepointfiveradius;
        int tworadius = this.strength * 2;

        for (int X = -onepointfiveradius; X <= onepointfiveradius; X++) {
            int xx = this.blockPos.getX() + X;
            int XX = X * X;
            for (int Z = -onepointfiveradius; Z <= onepointfiveradius; Z++) {
                int ZZ = Z * Z + XX;
                int zz = this.blockPos.getZ() + Z;
                for (int Y = -onepointfiveradius; Y <= onepointfiveradius; Y++) {
                    int YY = Y * Y + ZZ;
                    int yy = this.blockPos.getY() + Y;
                    if (YY < onepointfiveradiussqrd) {
                        BlockPos blockPos = new BlockPos(xx, yy, zz);
                        Block block = this.level.getBlockState(blockPos).getBlock();
                        if ((!Gravity.isAir(this.level, blockPos)) && (block != Blocks.BEDROCK)) {
                            int dist = (int) Math.sqrt(YY);
                            boolean flag = false;
                            if (dist < this.strength) {
                                flag = true;
                                affectedBlocks.add(block);
                                int varrand = 1 + dist - halfradius;
                                if (dist < halfradius) {
                                    this.level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                                    block = Blocks.AIR;
                                } else if (varrand > 0) {
                                    int randomness = halfradius - varrand / 2;
                                    if (block == Blocks.WATER || block == Blocks.LAVA) {
                                        this.level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                                        block = Blocks.AIR;
                                    } else if (block == Blocks.STONE && rand.nextInt(randomness) < randomness / 2) {
                                        this.level.setBlockAndUpdate(blockPos, Blocks.COBBLESTONE.defaultBlockState());
                                        block = Blocks.COBBLESTONE;
                                    } else if ((block == Blocks.GRASS_BLOCK) || (block == Blocks.DIRT)) {
                                        this.level.setBlockAndUpdate(blockPos, BlockInit.RADIOACTIVE_DIRT.get().defaultBlockState());
                                    } else if ((rand.nextInt(varrand) == 0 || rand.nextInt(varrand / 2 + 1) == 0)) {
                                        this.level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                                        block = Blocks.AIR;
                                    }
                                }
                            }
                            if (dist < onepointfiveradius) {
                                flag = true;
                                affectedBlocks.add(block);
                                if ((Y >= tworadius) || (Y >= this.strength) || (Tags.GLASS.contains(block)) || (Tags.PANES.contains(block))
                                        || (Tags.DOORS.contains(block)) || (block == Blocks.TORCH) || (block == Blocks.WATER)) {
                                    this.level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                                } else if ((Tags.PLANKS.contains(block)) || (Tags.STAIRS.contains(block)) || (Tags.SLABS.contains(block))) {
                                    this.level.setBlockAndUpdate(blockPos, Blocks.FIRE.defaultBlockState());
                                } else if (Tags.LOG_THAT_BURN.contains(block)) {
                                    if (this.level.random.nextFloat() > 0.5) {
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
                                BiomeUtil.setBiomeKeyAtPos(this.level, blockPos, BiomeInit.NUCLEAR);
                            }
                        }
                    }
                }
            }
        }
        this.level.playSound(null, this.blockPos, SoundsInit.NUCLEAR_EXPLOSION.get(),
                SoundSource.WEATHER, 0.6F, 1.0F);
        this.level.gameEvent(GameEvents.NUCLEAR_EXPLOSION, this.blockPos);
        NetworkManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new BiomeUpdatePacket(this.blockPos, BiomeInit.NUCLEAR.location(), this.strength));
    }

    private void pushAndHurtEntities() {
        int diameter = this.strength * 2;
        int onepointfiveradius = (this.strength / 2) * 3;
        double x = this.blockPos.getX();
        double y = this.blockPos.getY();
        double z = this.blockPos.getZ();
        int var3 = Mth.floor(x - (double) onepointfiveradius - 1.0D);
        int var4 = Mth.floor(x + (double) onepointfiveradius + 1.0D);
        int var5 = Mth.floor(y - (double) onepointfiveradius - 1.0D);
        int var28 = Mth.floor(y + (double) onepointfiveradius + 1.0D);
        int var7 = Mth.floor(z - (double) onepointfiveradius - 1.0D);
        int var29 = Mth.floor(z + (double) onepointfiveradius + 1.0D);
        List<Entity> var9 = this.level.getEntities(null, AABB.of(new BoundingBox(var3, var5, var7, var4, var28, var29)));
        MinecraftForge.EVENT_BUS.post(new NuclearExplosionEvent.Detonate(this.level, this, var9, affectedBlocks));

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
                    double var32 = Explosion.getSeenPercent(this.position, entity);
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

    private void fixLag() {
        for (int X = -this.strength; X <= this.strength; X++) {
            int xx = this.blockPos.getX() + X;
            for (int Y = -this.strength; Y <= this.strength; Y++) {
                int yy = this.blockPos.getY() + Y;
                for (int Z = -this.strength; Z <= this.strength; Z++) {
                    int zz = this.blockPos.getZ() + Z;
                    BlockPos blockPos = new BlockPos(xx, yy, zz);
                    if (this.level.getBlockState(blockPos).getBlock() == Blocks.AIR && this.level.getLightEmission(blockPos) == 0) {
                        if (this.level.getBlockState(blockPos.above()).getBlock() != Blocks.AIR
                                && this.level.getBlockState(blockPos.below()).getBlock() != Blocks.AIR
                                && this.level.getBlockState(blockPos.east()).getBlock() != Blocks.AIR
                                && this.level.getBlockState(blockPos.west()).getBlock() != Blocks.AIR
                                && this.level.getBlockState(blockPos.north()).getBlock() != Blocks.AIR
                                && this.level.getBlockState(blockPos.south()).getBlock() != Blocks.AIR) {
                            this.level.setBlockAndUpdate(blockPos, (this.level.random.nextInt(50) == 0 ?
                                    Tags.MINECRAFT_ORES : Tags.NUCLEAR_DEBRIS).getRandomElement(this.level.random).defaultBlockState());
                        }
                    }
                }
            }
        }
    }
}
