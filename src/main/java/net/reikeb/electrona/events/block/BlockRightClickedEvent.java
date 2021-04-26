package net.reikeb.electrona.events.block;

import net.minecraft.block.Blocks;
import net.minecraft.enchantment.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

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
        PlayerEntity entity = event.getPlayer();

        // Strip Chardwood Log into Oak Log
        ItemStack stack = event.getHand() == Hand.MAIN_HAND ? entity.getMainHandItem() : entity.getOffhandItem();
        if ((stack.getItem() instanceof AxeItem)
                && (event.getWorld().getBlockState(new BlockPos(event.getPos())).getBlock() == BlockInit.CHARDWOOD_LOG.get())) {
            event.getWorld().setBlockAndUpdate(event.getPos(), Blocks.STRIPPED_OAK_LOG.defaultBlockState());
            int unbreakingLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack);
            int damageValue = ((9 - (3 * unbreakingLevel)) + (unbreakingLevel == 0 ? 0 : 3));
            if (!entity.isCreative()) stack.hurtAndBreak(damageValue, entity, (player) ->
                    player.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
        }

        // Enchantments triggered
        EnchantmentFunction.thundering(entity.level, event.getPos(), entity, event.getHand());
    }
}
