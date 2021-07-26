package net.reikeb.electrona.events.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.misc.vm.EnchantmentFunction;

@Mod.EventBusSubscriber(modid = Electrona.MODID)
public class BlockRightClickedEvent {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player entity = event.getPlayer();

        // Strip Chardwood Log into Oak Log
        ItemStack stack = event.getHand() == InteractionHand.MAIN_HAND ? entity.getMainHandItem() : entity.getOffhandItem();
        if ((stack.getItem() instanceof AxeItem)
                && (event.getWorld().getBlockState(new BlockPos(event.getPos())).getBlock() == BlockInit.CHARDWOOD_LOG.get())) {
            event.getWorld().setBlockAndUpdate(event.getPos(), Blocks.STRIPPED_OAK_LOG.defaultBlockState());
            int unbreakingLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack);
            int damageValue = ((9 - (3 * unbreakingLevel)) + (unbreakingLevel == 0 ? 0 : 3));
            if (!entity.isCreative()) stack.hurtAndBreak(damageValue, entity, (player) ->
                    player.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }

        // Enchantments triggered
        EnchantmentFunction.thundering(entity.level, event.getPos(), entity, event.getHand());
    }
}
