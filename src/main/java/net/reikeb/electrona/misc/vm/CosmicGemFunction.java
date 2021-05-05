package net.reikeb.electrona.misc.vm;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.*;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import net.reikeb.electrona.utils.ElectronaUtils;
import net.reikeb.electrona.utils.GemPower;

public class CosmicGemFunction {

    /**
     * Use the power of the Gem
     *
     * @param world        The world of the user
     * @param playerEntity The user of the Gem
     * @param stack        The Gem as ItemStack
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
                    TeleporterFunction.teleport(world, playerEntity.blockPosition(), teleportPos, playerEntity);
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
