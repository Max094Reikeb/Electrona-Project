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
     * @param player     The player who cuts the tree
     * @param world      The world
     * @param pos        The position of the broken block
     * @param directions The directions to get neighbour blocks
     */
    public static void lumberjackMain(ServerPlayer player, Level world, BlockPos pos, Direction[] directions) {
        if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.LUMBERJACK.get(), player.getMainHandItem()) > 0) {
            for (Direction dir : directions) {
                BlockPos otherPos = pos.relative(dir);
                if (Tags.LOGS.contains(world.getBlockState(otherPos).getBlock())) {
                    Block.dropResources(world.getBlockState(otherPos), world, otherPos);
                    world.destroyBlock(otherPos, false);
                    lumberjackDef(world, otherPos, directions);
                }
            }
        }
    }

    /**
     * Second Lumberjack enchantment function
     *
     * @param world      The world
     * @param pos        The position of the neighbour blocks
     * @param directions The directions to get the neighbour blocks
     */
    public static void lumberjackDef(Level world, BlockPos pos, Direction[] directions) {
        for (Direction dir : directions) {
            BlockPos otherPos = pos.relative(dir);
            if (Tags.LOGS.contains(world.getBlockState(otherPos).getBlock())) {
                Block.dropResources(world.getBlockState(otherPos), world, otherPos);
                world.destroyBlock(otherPos, false);
                lumberjackDef(world, otherPos, directions);
            }
        }
    }

    /**
     * Main Veinminer enchantment function
     *
     * @param player     The player who mines the ore
     * @param world      The world
     * @param pos        The position of the broken block
     * @param directions The directions to get neighbour blocks
     */
    public static void veinminerMain(ServerPlayer player, Level world, BlockPos pos, Direction[] directions) {
        if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.VEINMINER.get(), player.getMainHandItem()) > 0) {
            for (Direction dir : directions) {
                BlockPos otherPos = pos.relative(dir);
                if ((Tags.MINECRAFT_ORES.contains(world.getBlockState(otherPos).getBlock()))
                        || (Tags.FORGE_ORES.contains(world.getBlockState(otherPos).getBlock()))
                        || (Tags.ELECTRONA_ORES.contains(world.getBlockState(otherPos).getBlock()))) {
                    Block.dropResources(world.getBlockState(otherPos), world, otherPos);
                    world.destroyBlock(otherPos, false);
                    veinminerDef(world, otherPos, directions);
                }
            }
        }
    }

    /**
     * Second Veinminer enchantment function
     *
     * @param world      The world
     * @param pos        The position of the neighbour blocks
     * @param directions The directions to get the neighbour blocks
     */
    public static void veinminerDef(Level world, BlockPos pos, Direction[] directions) {
        for (Direction dir : directions) {
            BlockPos otherPos = pos.relative(dir);
            if ((Tags.MINECRAFT_ORES.contains(world.getBlockState(otherPos).getBlock()))
                    || (Tags.FORGE_ORES.contains(world.getBlockState(otherPos).getBlock()))
                    || (Tags.ELECTRONA_ORES.contains(world.getBlockState(otherPos).getBlock()))) {
                Block.dropResources(world.getBlockState(otherPos), world, otherPos);
                world.destroyBlock(otherPos, false);
                veinminerDef(world, otherPos, directions);
            }
        }
    }

    /**
     * Thundering enchantment function
     *
     * @param world  The world
     * @param pos    The position of the clicked block
     * @param player The player who clicks on the block
     * @param handIn The hand of the player
     */
    public static void thundering(Level world, BlockPos pos, Player player, InteractionHand handIn) {
        ItemStack stack = handIn == InteractionHand.MAIN_HAND ? player.getMainHandItem() : player.getOffhandItem();
        if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.THUNDERING.get(), stack) > 0) {
            if (world instanceof ServerLevel) {
                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(world);
                int unbreakingLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack);
                int damageValue = ((9 - (3 * unbreakingLevel)) + (unbreakingLevel == 0 ? 0 : 3));
                if (lightning == null) return;
                lightning.moveTo(Vec3.atBottomCenterOf(pos));
                lightning.setVisualOnly(false);
                world.addFreshEntity(lightning);
                if (!player.isCreative()) stack.hurtAndBreak(damageValue, player, (entity) ->
                        entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            }
        }
    }

    /**
     * Smelting enchantment function
     *
     * @param world  The world
     * @param pos    The position of the mined block
     * @param block  The mined block
     * @param player The player who mines the block
     * @param handIn The hand with the used item
     */
    public static void smelting(Level world, BlockPos pos, Block block, Player player, InteractionHand handIn) {
        ItemStack stack = handIn == InteractionHand.MAIN_HAND ? player.getMainHandItem() : player.getOffhandItem();
        ItemStack result = world.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(new ItemStack(block)), world).isPresent() ?
                world.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(new ItemStack(block)), world).get().getResultItem().copy() :
                ItemStack.EMPTY;
        if (result == ItemStack.EMPTY || result.equals(new ItemStack(Blocks.AIR, 1))) return;

        if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SMELTING.get(), stack) > 0) {
            world.destroyBlock(pos, false);
            if (world instanceof ServerLevel) {
                ItemEntity entityToSpawn = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), result);
                entityToSpawn.setPickUpDelay(10);
                world.addFreshEntity(entityToSpawn);
            }
            int unbreakingLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack);
            int damageValue = ((9 - (3 * unbreakingLevel)) + (unbreakingLevel == 0 ? 0 : 3));
            if (!player.isCreative()) stack.hurtAndBreak(damageValue, player, (entity) ->
                    entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
    }
}
