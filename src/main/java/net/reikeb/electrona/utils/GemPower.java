package net.reikeb.electrona.utils;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.*;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import net.minecraftforge.registries.ForgeRegistries;

import net.reikeb.electrona.misc.vm.TeleporterFunction;

import java.util.*;

public enum GemPower {

    NOT_SET(-1, "", 0),
    INVISIBILITY(0, "invisibility", 2400),
    STRENGTH(1, "strength", 3600),
    TELEPORTATION(2, "teleportation", 80),
    YO_YO(3, "yo_yo", 400);

    private final int id;
    private final String name;
    private final int cooldown;

    private static final List<GemPower> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    GemPower(int p_345843475_0, String p_345843475_1, int p_345843475_2) {
        this.id = p_345843475_0;
        this.name = p_345843475_1;
        this.cooldown = p_345843475_2;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public static GemPower randomPower() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    public static String randomPowerId() {
        return byId(randomPower());
    }

    public static GemPower byName(String p_345843476_0) {
        return byName(p_345843476_0, INVISIBILITY);
    }

    public static GemPower byName(String p_345843477_0, GemPower p_345843477_1) {
        for (GemPower gemPower : values()) {
            if (gemPower.name.equals(p_345843477_0)) {
                return gemPower;
            }
        }
        return p_345843477_1;
    }

    public static int byCooldown(ItemStack p_345843478_0) {
        String power = p_345843478_0.getOrCreateTag().getString("power");
        return byCooldown(power);
    }

    public static int byCooldown(String p_345843479_0) {
        return byCooldown(p_345843479_0, 0);
    }

    public static int byCooldown(String p_345843480_0, int p_345843480_1) {
        for (GemPower gemPower : values()) {
            if (gemPower.name.equals(p_345843480_0)) {
                return gemPower.cooldown;
            }
        }
        return p_345843480_1;
    }

    public static String byId(GemPower p_345843481_0) {
        return byId(p_345843481_0, "invisibility");
    }

    public static String byId(GemPower p_345843482_0, String p_345843482_1) {
        for (GemPower gemPower : values()) {
            if (gemPower == p_345843482_0) {
                return gemPower.name;
            }
        }
        return p_345843482_1;
    }

    /**********************************************
     * Player use functions
     */

    /**
     * Use the power of the Gem
     *
     * @param world        The world of the user
     * @param playerEntity The user of the Gem
     * @param stack        The Gem as ItemStack
     */
    public static boolean use(World world, PlayerEntity playerEntity, ItemStack stack) {
        if (INVISIBILITY == get(stack)) {
            playerEntity.addEffect(new EffectInstance(Effects.INVISIBILITY, 600, 0, false, false, false));
            return true;
        } else if (STRENGTH == get(stack)) {
            playerEntity.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 600, 2, false, false, false));
            return true;
        } else if (TELEPORTATION == get(stack)) {
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
            teleport(world, playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), tx, ty, tz);
            return true;
        } else if (YO_YO == get(stack)) {
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
                    teleport(world, playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), posX, posY, posZ);
                    return true;
                } else {
                    if (!world.isClientSide) {
                        playerEntity.displayClientMessage(new TranslationTextComponent("message.electrona.yoyo_not_setup"), true);
                    }
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Method to teleport a player
     *
     * @param world        The world
     * @param playerEntity The teleported player
     * @param x            The current x coordinate
     * @param y            The current y coordinate
     * @param z            The current z coordinate
     * @param tx           The teleportation x coordinate
     * @param ty           The teleportation y coordinate
     * @param tz           The teleportation z coordinate
     */
    public static void teleport(World world, PlayerEntity playerEntity, double x, double y, double z, double tx, double ty, double tz) {
        BlockPos pos = new BlockPos(x, y, z);
        BlockPos teleportPos = new BlockPos(tx, ty, tz);
        {
            playerEntity.teleportTo(tx, ty, tz);
            if (playerEntity instanceof ServerPlayerEntity) {
                ((ServerPlayerEntity) playerEntity).connection.teleport(tx, ty, tz,
                        playerEntity.yRot, playerEntity.xRot, Collections.emptySet());
            }
        }
        SoundEvent teleportSound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.enderman.teleport"));
        TeleporterFunction.teleportParticles(world, pos, 300);
        TeleporterFunction.teleportParticles(world, teleportPos, 300);
        if (teleportSound == null) return;
        world.playSound(null, pos, teleportSound, SoundCategory.NEUTRAL,
                0.6F, 1.0F);
        world.playSound(null, teleportPos, teleportSound, SoundCategory.NEUTRAL,
                0.6F, 1.0F);
    }

    /**
     * Set the Gem's power
     *
     * @param stack The Gem as ItemStack
     * @param name  The power's name
     */
    public static void set(ItemStack stack, String name) {
        stack.getOrCreateTag().putString("power", name);
    }

    /**
     * Get a Gem's power
     *
     * @param stack The Gem as ItemStack
     * @return The GemPower name
     */
    public static GemPower get(ItemStack stack) {
        String id = stack.getOrCreateTag().getString("power");
        return byName(id);
    }
}
