package net.reikeb.electrona.gempower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.reikeb.electrona.init.GemInit;
import net.reikeb.electrona.init.GemPowerInit;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.electrona.misc.vm.TeleporterFunction;
import net.reikeb.maxilib.utils.Utils;

public class UsePower {

    public UsePower(Level level, Player player, ItemStack stack) {

        GemObject gemObject = PowerUtils.getGem(stack);
        boolean flag = false;
        int cooldown = 0;

        if (gemObject.equals(GemInit.INVISIBILITY.get())) {
            player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 600, 0, false, false, false));
            flag = true;
            cooldown = GemPowerInit.INVISIBILITY.get().getCooldown();
        } else if (gemObject.equals(GemInit.STRENGTH.get())) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 2, false, false, false));
            flag = true;
            cooldown = GemPowerInit.STRENGTH.get().getCooldown();
        } else if (gemObject.equals(GemInit.TELEPORTATION.get())) {
            HitResult rayTraceResult = Utils.lookAt(player, 100D, 1F, false);
            Vec3 location = rayTraceResult.getLocation();
            int stepX = 0;
            int stepY = 1;
            int stepZ = 0;
            if ((rayTraceResult instanceof BlockHitResult)
                    && (!(level.getBlockState(new BlockPos(location).above()).getMaterial() == Material.AIR))) {
                Direction rayTraceDirection = ((BlockHitResult) rayTraceResult).getDirection();
                stepX = rayTraceDirection.getStepX();
                stepY = rayTraceDirection.getStepY();
                stepZ = rayTraceDirection.getStepZ();
            }
            double tx = location.x() + stepX;
            double ty = location.y() + stepY;
            double tz = location.z() + stepZ;
            BlockPos teleportPos = new BlockPos(tx, ty, tz);
            player.fallDistance = 0;
            TeleporterFunction.teleport(level, player.blockPosition(), teleportPos, player);
            flag = true;
            cooldown = GemPowerInit.TELEPORTATION.get().getCooldown();
        } else if (gemObject.equals(GemInit.YO_YO.get())) {
            if (player.isShiftKeyDown()) {
                stack.getOrCreateTag().putDouble("powerYoYoX", player.getX());
                stack.getOrCreateTag().putDouble("powerYoYoY", player.getY());
                stack.getOrCreateTag().putDouble("powerYoYoZ", player.getZ());
                stack.getOrCreateTag().putBoolean("powerYoYo", true);
                if (!level.isClientSide) {
                    player.displayClientMessage(new TranslatableComponent("message.electrona.yoyo_location_saved"), true);
                }
                flag = true;
                cooldown = GemPowerInit.YO_YO.get().getCooldown();
            } else {
                if (stack.getOrCreateTag().getBoolean("powerYoYo")) {
                    double posX = stack.getOrCreateTag().getDouble("powerYoYoX");
                    double posY = stack.getOrCreateTag().getDouble("powerYoYoY");
                    double posZ = stack.getOrCreateTag().getDouble("powerYoYoZ");
                    BlockPos teleportPos = new BlockPos(posX, posY, posZ);
                    player.fallDistance = 0;
                    TeleporterFunction.teleport(level, player.blockPosition(), teleportPos, player);
                    flag = true;
                    cooldown = GemPowerInit.YO_YO.get().getCooldown();
                } else {
                    if (!level.isClientSide) {
                        player.displayClientMessage(new TranslatableComponent("message.electrona.yoyo_not_setup"), true);
                    }
                }
            }
        } else if (gemObject.equals(GemInit.DIMENSION_TRAVEL.get())) {
            if (stack.getOrCreateTag().getBoolean("dimensionTravel")) {
                BlockPos playerPos = player.blockPosition();
                String dimension = "";
                if (stack.getOrCreateTag().getString("dimension").equals("nether")) {
                    dimension = "minecraft:the_nether";
                } else if (stack.getOrCreateTag().getString("dimension").equals("end")) {
                    dimension = "minecraft:the_end";
                }
                if (level instanceof ServerLevel serverLevel) {
                    ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dimension));
                    if ((!dimension.equals("")) && (serverLevel.getServer().getLevel(key) != null)) {
                        ResourceKey<Level> newKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, Keys.OVERWORLD);
                        ServerLevel keyLevel = serverLevel.getServer().getLevel(key);
                        ServerLevel newLevel = serverLevel.getServer().getLevel(newKey);
                        if (level == newLevel) {
                            if (!player.level.isClientSide && player instanceof ServerPlayer serverPlayer) {
                                if (keyLevel != null) {
                                    player.fallDistance = 0;
                                    {
                                        serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0));
                                        serverPlayer.teleportTo(keyLevel, keyLevel.getSharedSpawnPos().getX(), keyLevel.getSharedSpawnPos().getY() + 1, keyLevel.getSharedSpawnPos().getZ(), player.yRot, player.xRot);
                                        serverPlayer.connection.send(new ClientboundPlayerAbilitiesPacket(player.abilities));
                                        for (MobEffectInstance effectInstance : player.getActiveEffects()) {
                                            serverPlayer.connection.send(new ClientboundUpdateMobEffectPacket(player.getId(), effectInstance));
                                        }
                                        serverPlayer.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
                                    }
                                    if (stack.getOrCreateTag().getString("dimension").equals("nether")) {
                                        BlockPos teleportPos = new BlockPos(playerPos.getX() / 8, 50, playerPos.getZ() / 8);
                                        TeleporterFunction.teleport(keyLevel, playerPos, teleportPos, player);
                                        Block teleportBlock = keyLevel.getBlockState(teleportPos).getBlock();
                                        Block aboveTpBlock = keyLevel.getBlockState(teleportPos.above()).getBlock();
                                        Block belowTpBlock = keyLevel.getBlockState(teleportPos.below()).getBlock();
                                        if (teleportBlock != Blocks.AIR) {
                                            keyLevel.setBlockAndUpdate(teleportPos, Blocks.AIR.defaultBlockState());
                                        }
                                        if (aboveTpBlock != Blocks.AIR) {
                                            keyLevel.setBlockAndUpdate(teleportPos.above(), Blocks.AIR.defaultBlockState());
                                        }
                                        if (belowTpBlock == Blocks.AIR || belowTpBlock == Blocks.LAVA || belowTpBlock == Blocks.MAGMA_BLOCK) {
                                            keyLevel.setBlockAndUpdate(teleportPos.below(), Blocks.NETHERRACK.defaultBlockState());
                                        }
                                    }
                                    if (stack.getOrCreateTag().getString("dimension").equals("end")) {
                                        BlockPos endPos = keyLevel.getSharedSpawnPos().below();
                                        keyLevel.setBlockAndUpdate(endPos, Blocks.OBSIDIAN.defaultBlockState());
                                        keyLevel.setBlockAndUpdate(endPos.east(), Blocks.OBSIDIAN.defaultBlockState());
                                        keyLevel.setBlockAndUpdate(endPos.north(), Blocks.OBSIDIAN.defaultBlockState());
                                        keyLevel.setBlockAndUpdate(endPos.north().west(), Blocks.OBSIDIAN.defaultBlockState());
                                        keyLevel.setBlockAndUpdate(endPos.north().east(), Blocks.OBSIDIAN.defaultBlockState());
                                        keyLevel.setBlockAndUpdate(endPos.south(), Blocks.OBSIDIAN.defaultBlockState());
                                        keyLevel.setBlockAndUpdate(endPos.south().west(), Blocks.OBSIDIAN.defaultBlockState());
                                        keyLevel.setBlockAndUpdate(endPos.south().east(), Blocks.OBSIDIAN.defaultBlockState());
                                        keyLevel.setBlockAndUpdate(endPos.west(), Blocks.OBSIDIAN.defaultBlockState());
                                    }
                                    flag = true;
                                    cooldown = GemPowerInit.DIMENSION_TRAVEL.get().getCooldown();
                                }
                            }
                        } else if (level == keyLevel) {
                            if (!player.level.isClientSide && player instanceof ServerPlayer serverPlayer) {
                                if (newLevel != null) {
                                    player.fallDistance = 0;
                                    {
                                        serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0));
                                        serverPlayer.teleportTo(newLevel, newLevel.getSharedSpawnPos().getX(), newLevel.getSharedSpawnPos().getY() + 1, newLevel.getSharedSpawnPos().getZ(), player.yRot, player.xRot);
                                        serverPlayer.connection.send(new ClientboundPlayerAbilitiesPacket(player.abilities));
                                        for (MobEffectInstance effectInstance : player.getActiveEffects()) {
                                            serverPlayer.connection.send(new ClientboundUpdateMobEffectPacket(player.getId(), effectInstance));
                                        }
                                        serverPlayer.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
                                    }
                                    if (stack.getOrCreateTag().getString("dimension").equals("nether")) {
                                        BlockPos teleportPos = new BlockPos(playerPos.getX() * 8, 50, playerPos.getZ() * 8);
                                        TeleporterFunction.teleport(newLevel, playerPos, teleportPos, player);
                                        Block teleportBlock = newLevel.getBlockState(teleportPos).getBlock();
                                        Block aboveTpBlock = newLevel.getBlockState(teleportPos.above()).getBlock();
                                        Block belowTpBlock = newLevel.getBlockState(teleportPos.below()).getBlock();
                                        if (teleportBlock != Blocks.AIR) {
                                            newLevel.setBlockAndUpdate(teleportPos, Blocks.AIR.defaultBlockState());
                                        }
                                        if (aboveTpBlock != Blocks.AIR) {
                                            newLevel.setBlockAndUpdate(teleportPos.above(), Blocks.AIR.defaultBlockState());
                                        }
                                        if (belowTpBlock == Blocks.AIR || belowTpBlock == Blocks.LAVA) {
                                            newLevel.setBlockAndUpdate(teleportPos.below(), Blocks.STONE.defaultBlockState());
                                        }
                                        flag = true;
                                        cooldown = GemPowerInit.DIMENSION_TRAVEL.get().getCooldown();
                                    }
                                    flag = true;
                                    cooldown = GemPowerInit.DIMENSION_TRAVEL.get().getCooldown();
                                }
                            }
                        }
                    } else {
                        if (!level.isClientSide) {
                            player.displayClientMessage(new TranslatableComponent("message.electrona.no_dimension_info"), true);
                        }
                    }
                }
            } else {
                if (!level.isClientSide) {
                    player.displayClientMessage(new TranslatableComponent("message.electrona.dimension_travel_not_setup"), true);
                }
            }

        } else if (gemObject.equals(GemInit.KNOCKBACK.get()) && player.isShiftKeyDown()) {
            Utils.forEntitiesInRadius(level, player.blockPosition(), 5, (livingEntity -> {
                if (livingEntity != player) {
                    livingEntity.knockback(5F * 0.5F, Mth.sin(player.yRot * ((float) Math.PI / 180F)), -Mth.cos(player.yRot * ((float) Math.PI / 180F)));
                    player.setDeltaMovement(player.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                }
            }));
            flag = true;
            cooldown = GemPowerInit.KNOCKBACK.get().getCooldown();
        } else if (gemObject.equals(GemInit.FLYING.get())) {
            if (player.abilities.flying) {
                player.abilities.flying = false;
                player.onUpdateAbilities();
                if (!level.isClientSide) {
                    player.displayClientMessage(new TranslatableComponent("message.electrona.flight_disabled"), true);
                }
            } else {
                player.abilities.flying = true;
                player.onUpdateAbilities();
                if (!level.isClientSide) {
                    player.displayClientMessage(new TranslatableComponent("message.electrona.flight_enabled"), true);
                }
            }
            flag = true;
            cooldown = GemPowerInit.FLYING.get().getCooldown();
        }
        if (flag) player.getCooldowns().addCooldown(stack.getItem(), cooldown);
    }

    public static class UseOn {

        public UseOn(Level level, Player player, BlockState state, ItemStack stack) {
            GemObject gemObject = PowerUtils.getGem(stack);
            boolean flag = false;
            int cooldown = 0;

            if (gemObject.equals(GemInit.DIMENSION_TRAVEL.get())) {
                if (state.is(Blocks.OBSIDIAN)) {
                    stack.getOrCreateTag().putBoolean("dimensionTravel", true);
                    stack.getOrCreateTag().putString("dimension", "nether");
                    if (!level.isClientSide) {
                        player.displayClientMessage(new TranslatableComponent("message.electrona.dimension_travel_saved_nether"), true);
                    }
                    flag = true;
                    cooldown = GemPowerInit.DIMENSION_TRAVEL.get().getCooldown();
                } else if (state.is(Blocks.END_STONE)) {
                    stack.getOrCreateTag().putBoolean("dimensionTravel", true);
                    stack.getOrCreateTag().putString("dimension", "end");
                    if (!level.isClientSide) {
                        player.displayClientMessage(new TranslatableComponent("message.electrona.dimension_travel_saved_end"), true);
                    }
                    flag = true;
                    cooldown = GemPowerInit.DIMENSION_TRAVEL.get().getCooldown();
                }
                if (flag) player.getCooldowns().addCooldown(stack.getItem(), cooldown);
            } else {
                new UsePower(level, player, stack);
            }
        }
    }
}
