package net.reikeb.electrona.misc.vm;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import net.minecraftforge.registries.ForgeRegistries;

import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.tileentities.TileTeleporter;

import java.util.Collections;
import java.util.Objects;

public class TeleporterFunction {

    /**
     * This method is the function of the Teleporter
     *
     * @param world  The world of the Teleporter
     * @param pos    The position of the Teleporter
     * @param entity The entity which travels through the Teleporter
     */
    public static void function(World world, BlockPos pos, Entity entity) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        BlockState state = world.getBlockState(pos);
        BlockPos posBelow = new BlockPos(x, (y - 1), z);
        TileEntity tile = world.getBlockEntity(pos);
        if (!(tile instanceof TileTeleporter)) return;
        TileTeleporter tileEntity = (TileTeleporter) tile;
        boolean isTeleporter = false;
        boolean autoDeletion = tileEntity.getTileData().getBoolean("autoDeletion");
        String dimension = new Object() {
            public String getValue(IWorld world, BlockPos pos, String tag) {
                TileEntity tileEntity = world.getBlockEntity(pos);
                if (tileEntity != null)
                    return tileEntity.getTileData().getString(tag);
                return "";
            }
        }.getValue(world, new BlockPos((int) x, (int) (y - 1), (int) z), "dimensionID");
        double teleportXCo = tileEntity.getTileData().getDouble("teleportX");
        double teleportYCo = tileEntity.getTileData().getDouble("teleportY");
        double teleportZCo = tileEntity.getTileData().getDouble("teleportZ");
        double electronicPower = tileEntity.getTileData().getDouble("ElectronicPower");
        if (electronicPower >= 1000) {
            /*
            if ((DimensionLinkerBlock.block.getBlock() == (world.getBlockState(new BlockPos((int) x, (int) (y - 1), (int) z))).getBlock())) {
                if (world instanceof ServerWorld) {
                    RegistryKey<World> key = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dimension));
                    if ((key != null) && (!dimension.equals("")) && ((ServerWorld) world).getServer().getLevel(key) != null) {
                        if ((TeleporterBlock.block.getBlock() == (((ServerWorld) world).getServer().getLevel(key)
                                .getBlockState(new BlockPos((int) (teleportXCo - 0.5), (int) teleportYCo, (int) (teleportZCo - 0.5)))).getBlock())) {
                            isTeleporter = true;
                        }
                    } else {
                        if (entity instanceof PlayerEntity && !entity.level.isClientSide) {
                            ((PlayerEntity) entity).displayClientMessage(new TranslationTextComponent("electrona.block.teleporter.no_dimension_info"),
                                    true);
                        }
                    }
                }
                if ((isTeleporter)) {
                    RegistryKey<World> newKey = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dimension));
                    RegistryKey<World> defaultKey = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("minecraft:overworld"));
                    IWorld _newWorld = ((ServerWorld) world).getServer().getLevel(newKey);
                    IWorld _defaultWorld = ((ServerWorld) world).getServer().getLevel(defaultKey);
                    if (!((world instanceof World ? ((World) world) : _defaultWorld) == _newWorld)) {
                        {
                            ServerWorld nextWorld = (ServerWorld) _newWorld;
                            if (!entity.level.isClientSide && entity instanceof ServerPlayerEntity) {
                                if (nextWorld != null) {
                                    ((ServerPlayerEntity) entity).connection
                                            .send(new SChangeGameStatePacket(SChangeGameStatePacket.WIN_GAME, 0));
                                    ((ServerPlayerEntity) entity).teleportTo(nextWorld, nextWorld.getSharedSpawnPos().getX(),
                                            nextWorld.getSharedSpawnPos().getY() + 1, nextWorld.getSharedSpawnPos().getZ(), entity.yRot,
                                            entity.xRot);
                                    ((ServerPlayerEntity) entity).connection
                                            .send(new SPlayerAbilitiesPacket(((ServerPlayerEntity) entity).abilities));
                                    for (EffectInstance effectinstance : ((ServerPlayerEntity) entity).getActiveEffects()) {
                                        ((ServerPlayerEntity) entity).connection
                                                .send(new SPlayEntityEffectPacket(entity.getId(), effectinstance));
                                    }
                                    ((ServerPlayerEntity) entity).connection.send(new SPlaySoundEventPacket(1032, BlockPos.ZERO, 0, false));
                                }
                            }
                        }
                    }
                    {
                        entity.teleportTo(teleportXCo, teleportYCo, teleportZCo);
                        if (entity instanceof ServerPlayerEntity) {
                            ((ServerPlayerEntity) entity).connection.teleport(teleportXCo, teleportYCo, teleportZCo, entity.yRot,
                                    entity.xRot, Collections.emptySet());
                        }
                    }
                    if (!world.isClientSide()) {
                        TileEntity _tileEntity = world.getBlockEntity(pos);
                        if (_tileEntity != null)
                            _tileEntity.getTileData().putDouble("ElectronicPower", (electronicPower - 1000));
                        if (world instanceof World)
                            ((World) world).sendBlockUpdated(pos, state, state, 3);
                    }
                    if (world instanceof World && !world.isClientSide()) {
                        ((World) world).playSound(null, pos,
                                Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.enderman.teleport"))),
                                SoundCategory.NEUTRAL, (float) 5, (float) 5);
                    } else {
                        ((World) world).playLocalSound(x, y, z,
                                Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.enderman.teleport"))),
                                SoundCategory.NEUTRAL, (float) 5, (float) 5, false);
                    }
                    if (world instanceof World && !world.isClientSide()) {
                        ((World) world).playSound(null, new BlockPos((int) (teleportXCo - 0.5), (int) teleportYCo, (int) (teleportZCo - 0.5)),
                                Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.enderman.teleport"))),
                                SoundCategory.NEUTRAL, (float) 5, (float) 5);
                    } else {
                        ((World) world).playLocalSound((teleportXCo - 0.5), teleportYCo, (teleportZCo - 0.5),
                                Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.enderman.teleport"))),
                                SoundCategory.NEUTRAL, (float) 5, (float) 5, false);
                    }
                    if (autoDeletion) {
                        if (!world.isClientSide()) {
                            TileEntity _tileEntity = world.getBlockEntity(pos);
                            if (_tileEntity != null)
                                _tileEntity.getTileData().putDouble("teleportX", 0);
                            if (world instanceof World)
                                ((World) world).sendBlockUpdated(pos, state, state, 3);
                        }
                        if (!world.isClientSide()) {
                            TileEntity _tileEntity = world.getBlockEntity(pos);
                            if (_tileEntity != null)
                                _tileEntity.getTileData().putDouble("teleportY", 0);
                            if (world instanceof World)
                                ((World) world).sendBlockUpdated(pos, state, state, 3);
                        }
                        if (!world.isClientSide()) {
                            TileEntity _tileEntity = world.getBlockEntity(pos);
                            if (_tileEntity != null)
                                _tileEntity.getTileData().putDouble("teleportZ", 0);
                            if (world instanceof World)
                                ((World) world).sendBlockUpdated(pos, state, state, 3);
                        }
                    }
                } else {
                    if (entity instanceof PlayerEntity && !entity.level.isClientSide) {
                        ((PlayerEntity) entity).displayClientMessage(new TranslationTextComponent("electrona.block.teleporter.no_world_coos_info"),
                                true);
                    }
                }
            } else {*/
            if ((BlockInit.TELEPORTER.get() == (world.getBlockState(new
                    BlockPos((int) (teleportXCo - 0.5), (int) teleportYCo, (int) (teleportZCo - 0.5)))).getBlock())) {
                {
                    entity.teleportTo(teleportXCo, teleportYCo, teleportZCo);
                    if (entity instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity) entity).connection.teleport(teleportXCo, teleportYCo, teleportZCo, entity.yRot,
                                entity.xRot, Collections.emptySet());
                    }
                }
                if (!world.isClientSide()) {
                    tileEntity.getTileData().putDouble("ElectronicPower", (electronicPower - 1000));
                    world.sendBlockUpdated(pos, state, state, 3);
                    world.playSound(null, pos,
                            Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.enderman.teleport"))),
                            SoundCategory.NEUTRAL, (float) 5, (float) 5);
                    world.playSound(null, new BlockPos((int) (teleportXCo - 0.5), (int) teleportYCo, (int) (teleportZCo - 0.5)),
                            Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.enderman.teleport"))),
                            SoundCategory.NEUTRAL, (float) 5, (float) 5);
                } else {
                    world.playLocalSound(x, y, z,
                            Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.enderman.teleport"))),
                            SoundCategory.NEUTRAL, (float) 5, (float) 5, false);
                    world.playLocalSound((teleportXCo - 0.5), teleportYCo, (teleportZCo - 0.5),
                            Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.enderman.teleport"))),
                            SoundCategory.NEUTRAL, (float) 5, (float) 5, false);
                }
                if (autoDeletion && !world.isClientSide()) {
                    tileEntity.getTileData().putDouble("teleportX", 0);
                    tileEntity.getTileData().putDouble("teleportY", 0);
                    tileEntity.getTileData().putDouble("teleportZ", 0);
                    world.sendBlockUpdated(pos, state, state, 3);
                }
            } else {
                if (entity instanceof PlayerEntity && !entity.level.isClientSide) {
                    ((PlayerEntity) entity).displayClientMessage(new TranslationTextComponent("electrona.block.teleporter.no_coos_info"), true);
                }
            }
            //}
        } else {
            if (entity instanceof PlayerEntity && !entity.level.isClientSide) {
                ((PlayerEntity) entity).displayClientMessage(new TranslationTextComponent("electrona.block.teleporter.not_enough_power_info"), true);
            }
        }
    }

    /**
     * Method that set the coodinates of the Teleporter into the Teleport Saver
     *
     * @param pos    The position of the Teleporter
     * @param player The player who right clicks
     */
    public static void rightClick(BlockPos pos, PlayerEntity player) {
        ItemStack stack = player.getMainHandItem().getStack();
        if (stack.getItem() == ItemInit.TELEPORT_SAVER.get()) {
            player.closeContainer();
            stack.getOrCreateTag().putDouble("teleportX", (Math.floor(pos.getX()) + 0.5));
            stack.getOrCreateTag().putDouble("teleportY", Math.ceil(pos.getY()));
            stack.getOrCreateTag().putDouble("teleportZ", (Math.floor(pos.getZ()) + 0.5));
            stack.getOrCreateTag().putBoolean("linked", true);
            if (!player.level.isClientSide()) {
                player.displayClientMessage(
                        new StringTextComponent((new TranslationTextComponent("item.electrona.teleport_saver.linked_info").getString())), true);
            }
        }
    }
}
