package net.reikeb.electrona.events.block;

import net.minecraft.entity.player.PlayerEntity;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.misc.vm.EnchantmentFunction;

@Mod.EventBusSubscriber(modid = Electrona.MODID)
public class BlockRightClickedEvent {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity entity = event.getPlayer();

        // Enchantments triggered
        EnchantmentFunction.thundering(entity.level, event.getPos(), entity, event.getHand());
    }
}
