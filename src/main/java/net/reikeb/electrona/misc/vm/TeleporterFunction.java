package net.reikeb.electrona.misc.vm;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.*;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.*;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;

import net.reikeb.electrona.events.local.TeleporterUseEvent;
import net.reikeb.electrona.init.*;
import net.reikeb.electrona.tileentities.*;
import net.reikeb.electrona.utils.ElectronaUtils;

import java.util.*;

public class TeleporterFunction {

    /**
     * This method is the function of the Teleporter
     *
     * @param world  The world of the Teleporter
     * @param pos    The position of the Teleporter
     * @param entity The entity which travels through the Teleporter
     */
    public static void stepOnTeleporter(World world, BlockPos pos, Entity entity) {
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
        double teleportXCo = tileEntity.getTileData().getDouble("teleportX");
        double teleportYCo = tileEntity.getTileData().getDouble("teleportY");
        double teleportZCo = tileEntity.getTileData().getDouble("teleportZ");
        BlockPos teleportPos = new BlockPos(teleportXCo, teleportYCo, teleportZCo);
        double electronicPower = tileEntity.getTileData().getDouble("ElectronicPower");
        if (electronicPower >= 1000) {
            if (BlockInit.DIMENSION_LINKER.get() == world.getBlockState(posBelow).getBlock()) {
                TileEntity tileBelow = world.getBlockEntity(posBelow);
                if (!(tileBelow instanceof TileDimensionLinker)) return;
                TileDimensionLinker tileEntityBelow = (TileDimensionLinker) tileBelow;
                String dimension = tileEntityBelow.getTileData().getString("dimensionID");
                if (world instanceof ServerWorld) {
                    RegistryKey<World> key = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dimension));
                    if ((key != null) && (!dimension.equals("")) && ((ServerWorld) world).getServer().getLevel(key) != null) {
                        if ((BlockInit.TELEPORTER.get() == (((ServerWorld) world).getServer().getLevel(key)
                                .getBlockState(new BlockPos((int) (teleportXCo - 0.5), (int) teleportYCo, (int) (teleportZCo - 0.5)))).getBlock())) {
                            isTeleporter = true;
                        }
                    } else {
                        if (entity instanceof PlayerEntity && !entity.level.isClientSide) {
                            ((PlayerEntity) entity).displayClientMessage(new TranslationTextComponent("message.electrona.no_dimension_info"),
                                    true);
                        }
                    }
                }
                if (isTeleporter) {
                    RegistryKey<World> newKey = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dimension));
                    RegistryKey<World> defaultKey = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("minecraft:overworld"));
                    ServerWorld _newWorld = ((ServerWorld) world).getServer().getLevel(newKey);
                    IWorld _defaultWorld = ((ServerWorld) world).getServer().getLevel(defaultKey);
                    if (!((world instanceof World ? world : _defaultWorld) == _newWorld)) {
                        if (!MinecraftForge.EVENT_BUS.post(new TeleporterUseEvent.Pre(world, _newWorld, pos, teleportPos, entity))) {
                            {
                                ServerWorld nextWorld = _newWorld;
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
                            teleport(world, _newWorld, pos, teleportPos, state, tileEntity, entity, autoDeletion, electronicPower);
                        }
                    }
                } else {
                    if (entity instanceof PlayerEntity && !entity.level.isClientSide) {
                        ((PlayerEntity) entity).displayClientMessage(new TranslationTextComponent("message.electrona.no_world_coos_info"), true);
                    }
                }
            } else if ((BlockInit.TELEPORTER.get() == (world.getBlockState(new
                    BlockPos((int) (teleportXCo - 0.5), (int) teleportYCo, (int) (teleportZCo - 0.5)))).getBlock())) {
                if (!MinecraftForge.EVENT_BUS.post(new TeleporterUseEvent.Pre(world, world, pos, teleportPos, entity))) {
                    teleport(world, world, pos, teleportPos, state, tileEntity, entity, autoDeletion, electronicPower);
                }
            } else {
                if (entity instanceof PlayerEntity && !entity.level.isClientSide) {
                    ((PlayerEntity) entity).displayClientMessage(new TranslationTextComponent("message.electrona.no_coos_info"), true);
                }
            }
        } else {
            if (entity instanceof PlayerEntity && !entity.level.isClientSide) {
                ((PlayerEntity) entity).displayClientMessage(new TranslationTextComponent("message.electrona.not_enough_power_info"), true);
            }
        }
    }

    /**
     * Method that teleports the player
     *
     * @param world           The departure world
     * @param teleportWorld   The arrival world
     * @param pos             The departure position
     * @param teleportPos     The arrival position
     * @param state           The state of the Teleporter
     * @param tileEntity      The Tile Entity of the Teleporter
     * @param entity          The entity that is teleported
     * @param autoDeletion    Gets if the auto-deletion is activated or not
     * @param electronicPower The energy of the Teleporter
     */
    public static void teleport(World world, World teleportWorld, BlockPos pos, BlockPos teleportPos, BlockState state, TileTeleporter tileEntity, Entity entity, boolean autoDeletion, double electronicPower) {
        double teleportXCo = teleportPos.getX();
        double teleportYCo = teleportPos.getY();
        double teleportZCo = teleportPos.getZ();
        {
            entity.teleportTo(teleportXCo, teleportYCo, teleportZCo);
            if (entity instanceof ServerPlayerEntity) {
                ((ServerPlayerEntity) entity).connection.teleport(teleportXCo, teleportYCo, teleportZCo, entity.yRot,
                        entity.xRot, Collections.emptySet());
            }
        }
        MinecraftForge.EVENT_BUS.post(new TeleporterUseEvent.Post(world, teleportWorld, pos, teleportPos, entity));
        if (!world.isClientSide()) {
            tileEntity.getTileData().putDouble("ElectronicPower", (electronicPower - 1000));
            if (autoDeletion) {
                tileEntity.getTileData().putDouble("teleportX", 0);
                tileEntity.getTileData().putDouble("teleportY", 0);
                tileEntity.getTileData().putDouble("teleportZ", 0);
            }
            world.sendBlockUpdated(pos, state, state, 3);
        }
        ElectronaUtils.playSound(world, pos, ForgeRegistries.SOUND_EVENTS
                .getValue(new ResourceLocation("entity.enderman.teleport")), SoundCategory.NEUTRAL);
        teleportParticles(world, pos, 300);
        ElectronaUtils.playSound(world, teleportPos, ForgeRegistries.SOUND_EVENTS
                .getValue(new ResourceLocation("entity.enderman.teleport")), SoundCategory.NEUTRAL);
        teleportParticles(world, teleportPos, 300);
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

    /**
     * Method that teleports the player with the Portable Teleporter
     *
     * @param worldIn  The world of the player
     * @param playerIn The player
     * @param handIn   The hand of the player that holds the Portable Teleporter
     * @return boolean indicates if everything went good
     */
    public static boolean teleportPortable(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        double electronicPower = stack.getOrCreateTag().getDouble("ElectronicPower");
        double teleportXCo = stack.getOrCreateTag().getDouble("teleportX");
        double teleportYCo = stack.getOrCreateTag().getDouble("teleportY");
        double teleportZCo = stack.getOrCreateTag().getDouble("teleportZ");
        BlockPos pos = new BlockPos(playerIn.getX(), playerIn.getY(), playerIn.getZ());
        BlockPos teleportPos = new BlockPos(teleportXCo, teleportYCo, teleportZCo);
        if (electronicPower >= 500) {
            if (BlockInit.TELEPORTER.get() == (worldIn.getBlockState(new
                    BlockPos((int) (teleportXCo - 0.5), (int) teleportYCo, (int) (teleportZCo - 0.5)))).getBlock()) {
                if (!MinecraftForge.EVENT_BUS.post(new TeleporterUseEvent.Pre(worldIn, worldIn, pos, teleportPos, playerIn))) {
                    {
                        playerIn.teleportTo(teleportXCo, teleportYCo, teleportZCo);
                        if (playerIn instanceof ServerPlayerEntity) {
                            ((ServerPlayerEntity) playerIn).connection.teleport(teleportXCo, teleportYCo, teleportZCo, playerIn.yRot,
                                    playerIn.xRot, Collections.emptySet());
                        }
                    }
                    MinecraftForge.EVENT_BUS.post(new TeleporterUseEvent.Post(worldIn, worldIn, pos, teleportPos, playerIn));
                    if (!playerIn.isCreative()) stack.getOrCreateTag().putDouble("ElectronicPower", (electronicPower - 500));
                    ElectronaUtils.playSound(worldIn, pos, ForgeRegistries.SOUND_EVENTS
                            .getValue(new ResourceLocation("entity.enderman.teleport")), SoundCategory.NEUTRAL);
                    teleportParticles(worldIn, pos, 300);
                    ElectronaUtils.playSound(worldIn, teleportPos, ForgeRegistries.SOUND_EVENTS
                            .getValue(new ResourceLocation("entity.enderman.teleport")), SoundCategory.NEUTRAL);
                    teleportParticles(worldIn, teleportPos, 300);
                    return true;
                }
            } else {
                if (!playerIn.level.isClientSide) {
                    playerIn.displayClientMessage(new TranslationTextComponent("message.electrona.no_coos_info"), true);
                }
            }
        } else {
            if (!playerIn.level.isClientSide) {
                playerIn.displayClientMessage(new TranslationTextComponent("message.electrona.not_enough_power_info"), true);
            }
        }
        return false;
    }

    /**
     * Method that handles particle spawning
     *
     * @param world  The world of the teleport
     * @param pos    The position where to spawn particles
     * @param number The number of particles to spawn
     */
    public static void teleportParticles(World world, BlockPos pos, int number) {
        for (int l = 0; l < number; l++) {
            double d0 = (pos.getX() + world.random.nextFloat());
            double d1 = (pos.getY() + world.random.nextFloat());
            double d2 = (pos.getZ() + world.random.nextFloat());
            double d3 = (world.random.nextFloat() - 0.2D) * 0.5D;
            double d4 = (world.random.nextFloat() - 0.2D) * 0.5D;
            double d5 = (world.random.nextFloat() - 0.2D) * 0.5D;
            world.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }
    }
}
