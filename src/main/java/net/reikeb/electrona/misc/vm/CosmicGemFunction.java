package net.reikeb.electrona.misc.vm;

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
import net.minecraft.world.entity.LivingEntity;
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
import net.reikeb.electrona.misc.Keys;
import net.reikeb.electrona.utils.GemPower;
import net.reikeb.maxilib.utils.Utils;

public class CosmicGemFunction {

    /**
     * Use the power of the Gem
     *
     * @param world        The world of the user
     * @param playerEntity The user of the Gem
     * @param stack        The Gem as ItemStack
     * @return Returns true if the Gem was used with success
     */
    public static boolean use(Level world, Player playerEntity, ItemStack stack) {
        if (GemPower.INVISIBILITY.equalsTo(getPower(stack))) {
            playerEntity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 600, 0, false, false, false));
            return true;
        } else if (GemPower.STRENGTH.equalsTo(getPower(stack))) {
            playerEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 2, false, false, false));
            return true;

        } else if (GemPower.TELEPORTATION.equalsTo(getPower(stack))) {
            HitResult rayTraceResult = Utils.lookAt(playerEntity, 100D, 1F, false);
            Vec3 location = rayTraceResult.getLocation();
            int stepX = 0;
            int stepY = 1;
            int stepZ = 0;
            if ((rayTraceResult instanceof BlockHitResult)
                    && (!(world.getBlockState(new BlockPos(location).above()).getMaterial() == Material.AIR))) {
                Direction rayTraceDirection = ((BlockHitResult) rayTraceResult).getDirection();
                stepX = rayTraceDirection.getStepX();
                stepY = rayTraceDirection.getStepY();
                stepZ = rayTraceDirection.getStepZ();
            }
            double tx = location.x() + stepX;
            double ty = location.y() + stepY;
            double tz = location.z() + stepZ;
            BlockPos teleportPos = new BlockPos(tx, ty, tz);
            playerEntity.fallDistance = 0;
            TeleporterFunction.teleport(world, playerEntity.blockPosition(), teleportPos, playerEntity);
            return true;

        } else if (GemPower.YO_YO.equalsTo(getPower(stack))) {
            if (playerEntity.isShiftKeyDown()) {
                stack.getOrCreateTag().putDouble("powerYoYoX", playerEntity.getX());
                stack.getOrCreateTag().putDouble("powerYoYoY", playerEntity.getY());
                stack.getOrCreateTag().putDouble("powerYoYoZ", playerEntity.getZ());
                stack.getOrCreateTag().putBoolean("powerYoYo", true);
                if (!world.isClientSide) {
                    playerEntity.displayClientMessage(new TranslatableComponent("message.electrona.yoyo_location_saved"), true);
                }
                return true;
            } else {
                if (stack.getOrCreateTag().getBoolean("powerYoYo")) {
                    double posX = stack.getOrCreateTag().getDouble("powerYoYoX");
                    double posY = stack.getOrCreateTag().getDouble("powerYoYoY");
                    double posZ = stack.getOrCreateTag().getDouble("powerYoYoZ");
                    BlockPos teleportPos = new BlockPos(posX, posY, posZ);
                    playerEntity.fallDistance = 0;
                    TeleporterFunction.teleport(world, playerEntity.blockPosition(), teleportPos, playerEntity);
                    return true;
                } else {
                    if (!world.isClientSide) {
                        playerEntity.displayClientMessage(new TranslatableComponent("message.electrona.yoyo_not_setup"), true);
                    }
                    return false;
                }
            }

        } else if (GemPower.DIMENSION_TRAVEL.equalsTo(getPower(stack))) {
            if (stack.getOrCreateTag().getBoolean("dimensionTravel")) {
                BlockPos playerPos = playerEntity.blockPosition();
                String dimension = "";
                if (stack.getOrCreateTag().getString("dimension").equals("nether")) {
                    dimension = "minecraft:the_nether";
                } else if (stack.getOrCreateTag().getString("dimension").equals("end")) {
                    dimension = "minecraft:the_end";
                }
                if (world instanceof ServerLevel) {
                    ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dimension));
                    if ((!dimension.equals("")) && (((ServerLevel) world).getServer().getLevel(key) != null)) {
                        ResourceKey<Level> newKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, Keys.OVERWORLD);
                        ServerLevel _newWorld = ((ServerLevel) world).getServer().getLevel(key);
                        ServerLevel _defaultWorld = ((ServerLevel) world).getServer().getLevel(newKey);
                        if (world == _defaultWorld) {
                            if (!playerEntity.level.isClientSide && playerEntity instanceof ServerPlayer) {
                                if (_newWorld != null) {
                                    playerEntity.fallDistance = 0;
                                    {
                                        ((ServerPlayer) playerEntity).connection
                                                .send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0));
                                        ((ServerPlayer) playerEntity).teleportTo(_newWorld, _newWorld.getSharedSpawnPos().getX(),
                                                _newWorld.getSharedSpawnPos().getY() + 1, _newWorld.getSharedSpawnPos().getZ(), playerEntity.yRot,
                                                playerEntity.xRot);
                                        ((ServerPlayer) playerEntity).connection
                                                .send(new ClientboundPlayerAbilitiesPacket(playerEntity.abilities));
                                        for (MobEffectInstance effectinstance : playerEntity.getActiveEffects()) {
                                            ((ServerPlayer) playerEntity).connection
                                                    .send(new ClientboundUpdateMobEffectPacket(playerEntity.getId(), effectinstance));
                                        }
                                        ((ServerPlayer) playerEntity).connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
                                    }
                                    if (stack.getOrCreateTag().getString("dimension").equals("nether")) {
                                        BlockPos teleportPos = new BlockPos(playerPos.getX() / 8, 50, playerPos.getZ() / 8);
                                        TeleporterFunction.teleport(_newWorld, playerPos, teleportPos, playerEntity);
                                        Block teleportBlock = _newWorld.getBlockState(teleportPos).getBlock();
                                        Block aboveTpBlock = _newWorld.getBlockState(teleportPos.above()).getBlock();
                                        Block belowTpBlock = _newWorld.getBlockState(teleportPos.below()).getBlock();
                                        if (teleportBlock != Blocks.AIR) {
                                            _newWorld.setBlockAndUpdate(teleportPos, Blocks.AIR.defaultBlockState());
                                        }
                                        if (aboveTpBlock != Blocks.AIR) {
                                            _newWorld.setBlockAndUpdate(teleportPos.above(), Blocks.AIR.defaultBlockState());
                                        }
                                        if (belowTpBlock == Blocks.AIR || belowTpBlock == Blocks.LAVA
                                                || belowTpBlock == Blocks.MAGMA_BLOCK) {
                                            _newWorld.setBlockAndUpdate(teleportPos.below(), Blocks.NETHERRACK.defaultBlockState());
                                        }
                                    }
                                    if (stack.getOrCreateTag().getString("dimension").equals("end")) {
                                        BlockPos endPos = _newWorld.getSharedSpawnPos().below();
                                        _newWorld.setBlockAndUpdate(endPos, Blocks.OBSIDIAN.defaultBlockState());
                                        _newWorld.setBlockAndUpdate(endPos.east(), Blocks.OBSIDIAN.defaultBlockState());
                                        _newWorld.setBlockAndUpdate(endPos.north(), Blocks.OBSIDIAN.defaultBlockState());
                                        _newWorld.setBlockAndUpdate(endPos.north().west(), Blocks.OBSIDIAN.defaultBlockState());
                                        _newWorld.setBlockAndUpdate(endPos.north().east(), Blocks.OBSIDIAN.defaultBlockState());
                                        _newWorld.setBlockAndUpdate(endPos.south(), Blocks.OBSIDIAN.defaultBlockState());
                                        _newWorld.setBlockAndUpdate(endPos.south().west(), Blocks.OBSIDIAN.defaultBlockState());
                                        _newWorld.setBlockAndUpdate(endPos.south().east(), Blocks.OBSIDIAN.defaultBlockState());
                                        _newWorld.setBlockAndUpdate(endPos.west(), Blocks.OBSIDIAN.defaultBlockState());
                                    }
                                    return true;
                                }
                            }
                        } else if (world == _newWorld) {
                            if (!playerEntity.level.isClientSide && playerEntity instanceof ServerPlayer) {
                                if (_defaultWorld != null) {
                                    playerEntity.fallDistance = 0;
                                    {
                                        ((ServerPlayer) playerEntity).connection
                                                .send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0));
                                        ((ServerPlayer) playerEntity).teleportTo(_defaultWorld, _defaultWorld.getSharedSpawnPos().getX(),
                                                _defaultWorld.getSharedSpawnPos().getY() + 1, _defaultWorld.getSharedSpawnPos().getZ(), playerEntity.yRot,
                                                playerEntity.xRot);
                                        ((ServerPlayer) playerEntity).connection
                                                .send(new ClientboundPlayerAbilitiesPacket(playerEntity.abilities));
                                        for (MobEffectInstance effectinstance : playerEntity.getActiveEffects()) {
                                            ((ServerPlayer) playerEntity).connection
                                                    .send(new ClientboundUpdateMobEffectPacket(playerEntity.getId(), effectinstance));
                                        }
                                        ((ServerPlayer) playerEntity).connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
                                    }
                                    if (stack.getOrCreateTag().getString("dimension").equals("nether")) {
                                        BlockPos teleportPos = new BlockPos(playerPos.getX() * 8, 50, playerPos.getZ() * 8);
                                        TeleporterFunction.teleport(_defaultWorld, playerPos, teleportPos, playerEntity);
                                        Block teleportBlock = _defaultWorld.getBlockState(teleportPos).getBlock();
                                        Block aboveTpBlock = _defaultWorld.getBlockState(teleportPos.above()).getBlock();
                                        Block belowTpBlock = _defaultWorld.getBlockState(teleportPos.below()).getBlock();
                                        if (teleportBlock != Blocks.AIR) {
                                            _defaultWorld.setBlockAndUpdate(teleportPos, Blocks.AIR.defaultBlockState());
                                        }
                                        if (aboveTpBlock != Blocks.AIR) {
                                            _defaultWorld.setBlockAndUpdate(teleportPos.above(), Blocks.AIR.defaultBlockState());
                                        }
                                        if (belowTpBlock == Blocks.AIR || belowTpBlock == Blocks.LAVA) {
                                            _defaultWorld.setBlockAndUpdate(teleportPos.below(), Blocks.STONE.defaultBlockState());
                                        }
                                        return true;
                                    }
                                    return true;
                                }
                            }
                        }
                    } else {
                        if (!world.isClientSide) {
                            playerEntity.displayClientMessage(new TranslatableComponent("message.electrona.no_dimension_info"), true);
                        }
                        return false;
                    }
                }
            } else {
                if (!world.isClientSide) {
                    playerEntity.displayClientMessage(new TranslatableComponent("message.electrona.dimension_travel_not_setup"), true);
                }
                return false;
            }

        } else if (GemPower.KNOCKBACK.equalsTo(getPower(stack)) && playerEntity.isShiftKeyDown()) {
            boolean flag = false;
            for (LivingEntity entityiterator : Utils.getLivingEntitiesInRadius(world, playerEntity.blockPosition(), 5)) {
                if (entityiterator != playerEntity) {
                    flag = true;
                    entityiterator.knockback(5F * 0.5F, Mth.sin(playerEntity.yRot * ((float) Math.PI / 180F)), -Mth.cos(playerEntity.yRot * ((float) Math.PI / 180F)));
                    playerEntity.setDeltaMovement(playerEntity.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                }
            }
            return flag;
        } else if (GemPower.FLYING.equalsTo(getPower(stack))) {
            if (playerEntity.abilities.flying) {
                playerEntity.abilities.flying = false;
                playerEntity.onUpdateAbilities();
                if (!world.isClientSide) {
                    playerEntity.displayClientMessage(new TranslatableComponent("message.electrona.flight_disabled"), true);
                }
            } else {
                playerEntity.abilities.flying = true;
                playerEntity.onUpdateAbilities();
                if (!world.isClientSide) {
                    playerEntity.displayClientMessage(new TranslatableComponent("message.electrona.flight_enabled"), true);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Uses the power of the Gem when clicked on a block
     *
     * @param world        The world of the user
     * @param state        The BlockState of the clicked block
     * @param playerEntity The user of the Gem
     * @param stack        The Gem as ItemStack
     * @return Returns true if the Gem was used with success
     */
    public static boolean useOn(Level world, BlockState state, Player playerEntity, ItemStack stack) {
        if (GemPower.DIMENSION_TRAVEL.equalsTo(getPower(stack))) {
            if (state.is(Blocks.OBSIDIAN)) {
                stack.getOrCreateTag().putBoolean("dimensionTravel", true);
                stack.getOrCreateTag().putString("dimension", "nether");
                if (!world.isClientSide) {
                    playerEntity.displayClientMessage(new TranslatableComponent("message.electrona.dimension_travel_saved_nether"), true);
                }
                return true;
            } else if (state.is(Blocks.END_STONE)) {
                stack.getOrCreateTag().putBoolean("dimensionTravel", true);
                stack.getOrCreateTag().putString("dimension", "end");
                if (!world.isClientSide) {
                    playerEntity.displayClientMessage(new TranslatableComponent("message.electrona.dimension_travel_saved_end"), true);
                }
                return true;
            }
            return false;
        } else {
            use(world, playerEntity, stack);
        }
        return false;
    }

    /**
     * Set the Gem's power
     *
     * @param stack The Gem as ItemStack
     * @param name  The power's ID
     */
    public static void setPower(ItemStack stack, String name) {
        stack.getOrCreateTag().putString("power", name);
    }

    /**
     * Get a Gem's power
     *
     * @param stack The Gem as ItemStack
     * @return The GemPower name
     */
    public static GemPower getPower(ItemStack stack) {
        String id = stack.getOrCreateTag().getString("power");
        return GemPower.byName(id);
    }
}
