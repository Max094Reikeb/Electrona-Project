package net.reikeb.electrona.misc.vm;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.reikeb.electrona.blockentities.DimensionLinkerBlockEntity;
import net.reikeb.electrona.blockentities.TeleporterBlockEntity;
import net.reikeb.electrona.events.local.TeleporterUseEvent;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.misc.GameEvents;

import java.util.Collections;

public class TeleporterFunction {

    /**
     * This method is the function of the Teleporter
     *
     * @param level  The level of the Teleporter
     * @param pos    The position of the Teleporter
     * @param entity The entity which travels through the Teleporter
     */
    public static void stepOnTeleporter(Level level, BlockPos pos, Entity entity) {
        if (!(level.getBlockEntity(pos) instanceof TeleporterBlockEntity teleporterBlockEntity)) return;
        boolean isTeleporter = false;
        boolean autoDeletion = teleporterBlockEntity.isAutoDeletion() == 1;
        double teleportXCo = teleporterBlockEntity.getTeleportX();
        double teleportYCo = teleporterBlockEntity.getTeleportY();
        double teleportZCo = teleporterBlockEntity.getTeleportZ();
        BlockPos teleportPos = new BlockPos(teleportXCo, teleportYCo, teleportZCo);
        double electronicPower = teleporterBlockEntity.getElectronicPower();
        if (electronicPower >= 1000) {
            if (BlockInit.DIMENSION_LINKER.get() == level.getBlockState(pos.below()).getBlock()) {
                BlockEntity blockEntityBelow = level.getBlockEntity(pos.below());
                if (!(blockEntityBelow instanceof DimensionLinkerBlockEntity dimensionLinkerBlockEntity)) return;
                String dimension = dimensionLinkerBlockEntity.getDimensionID();
                if (level instanceof ServerLevel serverLevel) {
                    ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dimension));
                    if ((!dimension.equals("")) && (serverLevel.getServer().getLevel(key) != null)) {
                        if ((BlockInit.TELEPORTER.get() == (serverLevel.getServer().getLevel(key)
                                .getBlockState(new BlockPos((int) (teleportXCo - 0.5), (int) teleportYCo, (int) (teleportZCo - 0.5)))).getBlock())) {
                            isTeleporter = true;
                        }
                    } else {
                        if ((entity instanceof Player player) && !entity.level.isClientSide) {
                            player.displayClientMessage(new TranslatableComponent("message.electrona.no_dimension_info"),
                                    true);
                        }
                    }
                }
                if (isTeleporter) {
                    ResourceKey<Level> newKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dimension));
                    ServerLevel _newLevel = ((ServerLevel) level).getServer().getLevel(newKey);
                    if (level != _newLevel) {
                        if (!MinecraftForge.EVENT_BUS.post(new TeleporterUseEvent.Pre(level, _newLevel, pos, teleportPos, entity))) {
                            {
                                if (!entity.level.isClientSide && (entity instanceof ServerPlayer serverPlayer)) {
                                    if (_newLevel != null) {
                                        serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0));
                                        serverPlayer.teleportTo(_newLevel, _newLevel.getSharedSpawnPos().getX(), _newLevel.getSharedSpawnPos().getY() + 1,
                                                _newLevel.getSharedSpawnPos().getZ(), entity.yRot, entity.xRot);
                                        serverPlayer.connection.send(new ClientboundPlayerAbilitiesPacket(serverPlayer.abilities));
                                        for (MobEffectInstance effectinstance : serverPlayer.getActiveEffects()) {
                                            serverPlayer.connection.send(new ClientboundUpdateMobEffectPacket(entity.getId(), effectinstance));
                                        }
                                        serverPlayer.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
                                    }
                                }
                            }
                            teleport(_newLevel, pos, teleportPos, entity);
                            MinecraftForge.EVENT_BUS.post(new TeleporterUseEvent.Post(level, _newLevel, pos, teleportPos, entity));
                            if (!level.isClientSide()) {
                                EnergyFunction.drainEnergy(teleporterBlockEntity, 1000);
                                if (autoDeletion) {
                                    teleporterBlockEntity.setTeleportX(0);
                                    teleporterBlockEntity.setTeleportY(0);
                                    teleporterBlockEntity.setTeleportZ(0);
                                }
                                level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 3);
                            }
                        }
                    }
                } else {
                    if ((entity instanceof Player player) && !entity.level.isClientSide) {
                        player.displayClientMessage(new TranslatableComponent("message.electrona.no_world_coos_info"), true);
                    }
                }
            } else if ((BlockInit.TELEPORTER.get() == (level.getBlockState(new
                    BlockPos((int) (teleportXCo - 0.5), (int) teleportYCo, (int) (teleportZCo - 0.5)))).getBlock())) {
                if (!MinecraftForge.EVENT_BUS.post(new TeleporterUseEvent.Pre(level, level, pos, teleportPos, entity))) {
                    teleport(level, pos, teleportPos, entity);
                    MinecraftForge.EVENT_BUS.post(new TeleporterUseEvent.Post(level, level, pos, teleportPos, entity));
                    if (!level.isClientSide()) {
                        EnergyFunction.drainEnergy(teleporterBlockEntity, 1000);
                        if (autoDeletion) {
                            teleporterBlockEntity.setTeleportX(0);
                            teleporterBlockEntity.setTeleportY(0);
                            teleporterBlockEntity.setTeleportZ(0);
                        }
                        level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 3);
                    }
                }
            } else {
                if ((entity instanceof Player player) && !entity.level.isClientSide) {
                    player.displayClientMessage(new TranslatableComponent("message.electrona.no_coos_info"), true);
                }
            }
        } else {
            if ((entity instanceof Player player) && !entity.level.isClientSide) {
                player.displayClientMessage(new TranslatableComponent("message.electrona.not_enough_power_info"), true);
            }
        }
    }

    /**
     * Method that teleports an entity
     *
     * @param level       The departure level
     * @param pos         The departure position
     * @param teleportPos The arrival position
     * @param entity      The entity that is teleported
     */
    public static void teleport(Level level, BlockPos pos, BlockPos teleportPos, Entity entity) {
        double teleportXCo = teleportPos.getX();
        double teleportYCo = teleportPos.getY();
        double teleportZCo = teleportPos.getZ();
        {
            entity.teleportTo(teleportXCo, teleportYCo, teleportZCo);
            if (entity instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.teleport(teleportXCo, teleportYCo, teleportZCo, entity.yRot, entity.xRot,
                        Collections.emptySet());
            }
        }
        entity.gameEvent(GameEvents.TELEPORTER_USE, teleportPos);
        SoundEvent teleportSound = SoundEvents.ENDERMAN_TELEPORT;
        teleportParticles(level, pos, 300);
        teleportParticles(level, teleportPos, 300);
        level.playSound(null, pos, teleportSound, SoundSource.NEUTRAL,
                0.6F, 1.0F);
        level.playSound(null, teleportPos, teleportSound, SoundSource.NEUTRAL,
                0.6F, 1.0F);
    }

    /**
     * Method that set the coodinates of the Teleporter into the Teleport Saver
     *
     * @param pos    The position of the Teleporter
     * @param player The player who right clicks
     */
    public static void rightClick(BlockPos pos, Player player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() == ItemInit.TELEPORT_SAVER.get()) {
            player.closeContainer();
            stack.getOrCreateTag().putDouble("teleportX", (Math.floor(pos.getX()) + 0.5));
            stack.getOrCreateTag().putDouble("teleportY", Math.ceil(pos.getY()));
            stack.getOrCreateTag().putDouble("teleportZ", (Math.floor(pos.getZ()) + 0.5));
            stack.getOrCreateTag().putBoolean("linked", true);
            if (!player.level.isClientSide()) {
                player.displayClientMessage(
                        new TextComponent((new TranslatableComponent("item.electrona.teleport_saver.linked_info").getString())), true);
            }
        }
    }

    /**
     * Method that teleports the player with the Portable Teleporter
     *
     * @param level  The level of the player
     * @param player The player
     * @param hand   The hand of the player that holds the Portable Teleporter
     * @return boolean indicates if everything went good
     */
    public static boolean teleportPortable(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        double electronicPower = stack.getOrCreateTag().getDouble("ElectronicPower");
        double teleportXCo = stack.getOrCreateTag().getDouble("teleportX");
        double teleportYCo = stack.getOrCreateTag().getDouble("teleportY");
        double teleportZCo = stack.getOrCreateTag().getDouble("teleportZ");
        BlockPos pos = new BlockPos(player.getX(), player.getY(), player.getZ());
        BlockPos teleportPos = new BlockPos(teleportXCo, teleportYCo, teleportZCo);
        if (electronicPower >= 500) {
            if (BlockInit.TELEPORTER.get() == (level.getBlockState(new
                    BlockPos((int) (teleportXCo - 0.5), (int) teleportYCo, (int) (teleportZCo - 0.5)))).getBlock()) {
                if (!MinecraftForge.EVENT_BUS.post(new TeleporterUseEvent.Pre(level, level, pos, teleportPos, player))) {
                    teleport(level, pos, teleportPos, player);
                    MinecraftForge.EVENT_BUS.post(new TeleporterUseEvent.Post(level, level, pos, teleportPos, player));
                    if (!player.isCreative())
                        stack.getOrCreateTag().putDouble("ElectronicPower", (electronicPower - 500));
                    return true;
                }
            } else {
                if (!player.level.isClientSide) {
                    player.displayClientMessage(new TranslatableComponent("message.electrona.no_coos_info"), true);
                }
            }
        } else {
            if (!player.level.isClientSide) {
                player.displayClientMessage(new TranslatableComponent("message.electrona.not_enough_power_info"), true);
            }
        }
        return false;
    }

    /**
     * Method that handles particle spawning
     *
     * @param level  The level of the teleport
     * @param pos    The position where to spawn particles
     * @param number The number of particles to spawn
     */
    public static void teleportParticles(Level level, BlockPos pos, int number) {
        for (int l = 0; l < number; l++) {
            double d0 = (pos.getX() + level.random.nextFloat());
            double d1 = (pos.getY() + level.random.nextFloat());
            double d2 = (pos.getZ() + level.random.nextFloat());
            double d3 = (level.random.nextFloat() - 0.2D) * 0.5D;
            double d4 = (level.random.nextFloat() - 0.2D) * 0.5D;
            double d5 = (level.random.nextFloat() - 0.2D) * 0.5D;
            level.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }
    }
}
