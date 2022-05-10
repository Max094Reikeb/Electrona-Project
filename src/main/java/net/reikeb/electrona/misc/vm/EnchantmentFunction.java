package net.reikeb.electrona.misc.vm;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.reikeb.electrona.init.EnchantmentInit;
import net.reikeb.electrona.misc.Tags;

public class EnchantmentFunction {

    /**
     * Main Lumberjack enchantment function
     *
     * @param serverPlayer The serverPlayer who cuts the tree
     * @param level        The level
     * @param pos          The position of the broken block
     * @param directions   The directions to get neighbour blocks
     */
    public static void lumberjackMain(ServerPlayer serverPlayer, Level level, BlockPos pos, Direction[] directions) {
        if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.LUMBERJACK.get(), serverPlayer.getMainHandItem()) > 0) {
            for (Direction dir : directions) {
                BlockPos otherPos = pos.relative(dir);
                if (level.getBlockState(otherPos).is(Tags.LOGS)) {
                    Block.dropResources(level.getBlockState(otherPos), level, otherPos);
                    level.destroyBlock(otherPos, false);
                    lumberjackDef(level, otherPos, directions);
                }
            }
        }
    }

    /**
     * Second Lumberjack enchantment function
     *
     * @param level      The level
     * @param pos        The position of the neighbour blocks
     * @param directions The directions to get the neighbour blocks
     */
    public static void lumberjackDef(Level level, BlockPos pos, Direction[] directions) {
        for (Direction dir : directions) {
            BlockPos otherPos = pos.relative(dir);
            if (level.getBlockState(otherPos).is(Tags.LOGS)) {
                Block.dropResources(level.getBlockState(otherPos), level, otherPos);
                level.destroyBlock(otherPos, false);
                lumberjackDef(level, otherPos, directions);
            }
        }
    }

    /**
     * Main Veinminer enchantment function
     *
     * @param serverPlayer The serverPlayer who mines the ore
     * @param level        The level
     * @param pos          The position of the broken block
     * @param directions   The directions to get neighbour blocks
     */
    public static void veinminerMain(ServerPlayer serverPlayer, Level level, BlockPos pos, Direction[] directions) {
        if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.VEINMINER.get(), serverPlayer.getMainHandItem()) > 0) {
            for (Direction dir : directions) {
                BlockPos otherPos = pos.relative(dir);
                if ((level.getBlockState(otherPos).is(Tags.MINECRAFT_ORES)) ||
                        (level.getBlockState(otherPos).is(Tags.FORGE_ORES)) ||
                        (level.getBlockState(otherPos).is(Tags.ELECTRONA_ORES))) {
                    Block.dropResources(level.getBlockState(otherPos), level, otherPos);
                    level.destroyBlock(otherPos, false);
                    veinminerDef(level, otherPos, directions);
                }
            }
        }
    }

    /**
     * Second Veinminer enchantment function
     *
     * @param level      The level
     * @param pos        The position of the neighbour blocks
     * @param directions The directions to get the neighbour blocks
     */
    public static void veinminerDef(Level level, BlockPos pos, Direction[] directions) {
        for (Direction dir : directions) {
            BlockPos otherPos = pos.relative(dir);
            if ((level.getBlockState(otherPos).is(Tags.MINECRAFT_ORES)) ||
                    (level.getBlockState(otherPos).is(Tags.FORGE_ORES)) ||
                    (level.getBlockState(otherPos).is(Tags.ELECTRONA_ORES))) {
                Block.dropResources(level.getBlockState(otherPos), level, otherPos);
                level.destroyBlock(otherPos, false);
                veinminerDef(level, otherPos, directions);
            }
        }
    }

    /**
     * Thundering enchantment function
     *
     * @param level  The level
     * @param pos    The position of the clicked block
     * @param player The player who clicks on the block
     * @param hand   The hand of the player
     */
    public static void thundering(Level level, BlockPos pos, Player player, InteractionHand hand) {
        ItemStack stack = hand == InteractionHand.MAIN_HAND ? player.getMainHandItem() : player.getOffhandItem();
        if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.THUNDERING.get(), stack) > 0) {
            if (level instanceof ServerLevel) {
                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
                int unbreakingLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack);
                int damageValue = ((9 - (3 * unbreakingLevel)) + (unbreakingLevel == 0 ? 0 : 3));
                if (lightning == null) return;
                lightning.moveTo(Vec3.atBottomCenterOf(pos));
                lightning.setVisualOnly(false);
                level.addFreshEntity(lightning);
                if (!player.isCreative()) stack.hurtAndBreak(damageValue, player, (entity) ->
                        entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            }
        }
    }

    /**
     * Smelting enchantment function
     *
     * @param level  The level
     * @param pos    The position of the mined block
     * @param block  The mined block
     * @param player The player who mines the block
     * @param hand   The hand with the used item
     */
    public static void smelting(Level level, BlockPos pos, Block block, Player player, InteractionHand hand) {
        ItemStack stack = hand == InteractionHand.MAIN_HAND ? player.getMainHandItem() : player.getOffhandItem();
        ItemStack result = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(new ItemStack(block)), level).isPresent() ?
                level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(new ItemStack(block)), level).get().getResultItem().copy() :
                ItemStack.EMPTY;
        if (result == ItemStack.EMPTY || result.equals(new ItemStack(Blocks.AIR, 1))) return;

        if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SMELTING.get(), stack) > 0) {
            level.destroyBlock(pos, false);
            if (level instanceof ServerLevel) {
                ItemEntity entityToSpawn = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), result);
                entityToSpawn.setPickUpDelay(10);
                level.addFreshEntity(entityToSpawn);
            }
            int unbreakingLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack);
            int damageValue = ((9 - (3 * unbreakingLevel)) + (unbreakingLevel == 0 ? 0 : 3));
            if (!player.isCreative()) stack.hurtAndBreak(damageValue, player, (entity) ->
                    entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
    }
}
