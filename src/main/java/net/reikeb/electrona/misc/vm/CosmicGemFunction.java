package net.reikeb.electrona.misc.vm;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import net.reikeb.electrona.utils.*;

import java.util.*;
import java.util.stream.Collectors;

public class CosmicGemFunction {

    /**
     * Use the power of the Gem
     *
     * @param world        The world of the user
     * @param playerEntity The user of the Gem
     * @param stack        The Gem as ItemStack
     * @return Returns true if the Gem was used with success
     */
    public static boolean use(World world, PlayerEntity playerEntity, ItemStack stack) {
        if (GemPower.INVISIBILITY.equalsTo(getPower(stack))) {
            playerEntity.addEffect(new EffectInstance(Effects.INVISIBILITY, 600, 0, false, false, false));
            return true;
        } else if (GemPower.STRENGTH.equalsTo(getPower(stack))) {
            playerEntity.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 600, 2, false, false, false));
            return true;

        } else if (GemPower.TELEPORTATION.equalsTo(getPower(stack))) {
            RayTraceResult rayTraceResult = ElectronaUtils.lookAt(playerEntity, 100D, 1F, false);
            Vector3d location = rayTraceResult.getLocation();
            int stepX = 0;
            int stepY = 1;
            int stepZ = 0;
            if ((rayTraceResult instanceof BlockRayTraceResult)
                    && (!(world.getBlockState(new BlockPos(location).above()).getMaterial() == Material.AIR))) {
                Direction rayTraceDirection = ((BlockRayTraceResult) rayTraceResult).getDirection();
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
                    playerEntity.displayClientMessage(new TranslationTextComponent("message.electrona.yoyo_location_saved"), true);
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
                        playerEntity.displayClientMessage(new TranslationTextComponent("message.electrona.yoyo_not_setup"), true);
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
                if (world instanceof ServerWorld) {
                    RegistryKey<World> key = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dimension));
                    if ((!dimension.equals("")) && (((ServerWorld) world).getServer().getLevel(key) != null)) {
                        RegistryKey<World> newKey = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("minecraft:overworld"));
                        ServerWorld _newWorld = ((ServerWorld) world).getServer().getLevel(key);
                        ServerWorld _defaultWorld = ((ServerWorld) world).getServer().getLevel(newKey);
                        if (world == _defaultWorld) {
                            if (!playerEntity.level.isClientSide && playerEntity instanceof ServerPlayerEntity) {
                                if (_newWorld != null) {
                                    playerEntity.fallDistance = 0;
                                    {
                                        ((ServerPlayerEntity) playerEntity).connection
                                                .send(new SChangeGameStatePacket(SChangeGameStatePacket.WIN_GAME, 0));
                                        ((ServerPlayerEntity) playerEntity).teleportTo(_newWorld, _newWorld.getSharedSpawnPos().getX(),
                                                _newWorld.getSharedSpawnPos().getY() + 1, _newWorld.getSharedSpawnPos().getZ(), playerEntity.yRot,
                                                playerEntity.xRot);
                                        ((ServerPlayerEntity) playerEntity).connection
                                                .send(new SPlayerAbilitiesPacket(playerEntity.abilities));
                                        for (EffectInstance effectinstance : playerEntity.getActiveEffects()) {
                                            ((ServerPlayerEntity) playerEntity).connection
                                                    .send(new SPlayEntityEffectPacket(playerEntity.getId(), effectinstance));
                                        }
                                        ((ServerPlayerEntity) playerEntity).connection.send(new SPlaySoundEventPacket(1032, BlockPos.ZERO, 0, false));
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
                            if (!playerEntity.level.isClientSide && playerEntity instanceof ServerPlayerEntity) {
                                if (_defaultWorld != null) {
                                    playerEntity.fallDistance = 0;
                                    {
                                        ((ServerPlayerEntity) playerEntity).connection
                                                .send(new SChangeGameStatePacket(SChangeGameStatePacket.WIN_GAME, 0));
                                        ((ServerPlayerEntity) playerEntity).teleportTo(_defaultWorld, _defaultWorld.getSharedSpawnPos().getX(),
                                                _defaultWorld.getSharedSpawnPos().getY() + 1, _defaultWorld.getSharedSpawnPos().getZ(), playerEntity.yRot,
                                                playerEntity.xRot);
                                        ((ServerPlayerEntity) playerEntity).connection
                                                .send(new SPlayerAbilitiesPacket(playerEntity.abilities));
                                        for (EffectInstance effectinstance : playerEntity.getActiveEffects()) {
                                            ((ServerPlayerEntity) playerEntity).connection
                                                    .send(new SPlayEntityEffectPacket(playerEntity.getId(), effectinstance));
                                        }
                                        ((ServerPlayerEntity) playerEntity).connection.send(new SPlaySoundEventPacket(1032, BlockPos.ZERO, 0, false));
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
                            playerEntity.displayClientMessage(new TranslationTextComponent("message.electrona.no_dimension_info"), true);
                        }
                        return false;
                    }
                }
            } else {
                if (!world.isClientSide) {
                    playerEntity.displayClientMessage(new TranslationTextComponent("message.electrona.dimension_travel_not_setup"), true);
                }
                return false;
            }

        } else if (GemPower.KNOCKBACK.equalsTo(getPower(stack)) && playerEntity.isShiftKeyDown()) {
            int x = playerEntity.blockPosition().getX();
            int y = playerEntity.blockPosition().getY();
            int z = playerEntity.blockPosition().getZ();
            boolean flag = false;
            {
                List<Entity> _entfound = world.getEntitiesOfClass(Entity.class,
                        new AxisAlignedBB(x - 5, y - 5, z - 5,
                                x + 5, y + 5, z + 5),
                        null).stream().sorted(new Object() {
                    Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                        return Comparator.comparing(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                    }
                }.compareDistOf(x, y, z)).collect(Collectors.toList());
                for (Entity entity : _entfound) {
                    if ((entity instanceof LivingEntity) && (entity != playerEntity)) {
                        flag = true;
                        ((LivingEntity) entity).knockback(5F * 0.5F, MathHelper.sin(playerEntity.yRot * ((float) Math.PI / 180F)), -MathHelper.cos(playerEntity.yRot * ((float) Math.PI / 180F)));
                        playerEntity.setDeltaMovement(playerEntity.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                    }
                }
                return flag;
            }
        } else if (GemPower.FLYING.equalsTo(getPower(stack))) {
            if (playerEntity.abilities.flying) {
                playerEntity.abilities.flying = false;
                playerEntity.onUpdateAbilities();
                if (!world.isClientSide) {
                    playerEntity.displayClientMessage(new TranslationTextComponent("message.electrona.flight_disabled"), true);
                }
            } else {
                playerEntity.abilities.flying = true;
                playerEntity.onUpdateAbilities();
                if (!world.isClientSide) {
                    playerEntity.displayClientMessage(new TranslationTextComponent("message.electrona.flight_enabled"), true);
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
    public static boolean useOn(World world, BlockState state, PlayerEntity playerEntity, ItemStack stack) {
        if (GemPower.DIMENSION_TRAVEL.equalsTo(getPower(stack))) {
            if (state.is(Blocks.OBSIDIAN)) {
                stack.getOrCreateTag().putBoolean("dimensionTravel", true);
                stack.getOrCreateTag().putString("dimension", "nether");
                if (!world.isClientSide) {
                    playerEntity.displayClientMessage(new TranslationTextComponent("message.electrona.dimension_travel_saved_nether"), true);
                }
                return true;
            } else if (state.is(Blocks.END_STONE)) {
                stack.getOrCreateTag().putBoolean("dimensionTravel", true);
                stack.getOrCreateTag().putString("dimension", "end");
                if (!world.isClientSide) {
                    playerEntity.displayClientMessage(new TranslationTextComponent("message.electrona.dimension_travel_saved_end"), true);
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
