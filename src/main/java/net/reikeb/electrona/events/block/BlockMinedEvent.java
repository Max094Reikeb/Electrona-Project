package net.reikeb.electrona.events.block;

import net.minecraft.advancements.Advancement;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;

import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.electrona.misc.vm.EnchantmentFunction;
import net.reikeb.electrona.utils.ElectronaUtils;

@Mod.EventBusSubscriber(modid = Electrona.MODID)
public class BlockMinedEvent {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        ServerPlayer player = (ServerPlayer) event.getPlayer();
        Block eventBlock = event.getState().getBlock();

        if (eventBlock == BlockInit.TIN_ORE.get()) {
            Advancement advancement = player.server.getAdvancements().getAdvancement(Keys.A_WHOLE_NEW_WORLD_ADVANCEMENT);
            ElectronaUtils.awardAdvancement(player, advancement, "A Whole New World");
        }

        // Enchantments triggered
        EnchantmentFunction.lumberjackMain(player, player.level, event.getPos(), Direction.values());
        EnchantmentFunction.veinminerMain(player, player.level, event.getPos(), Direction.values());
        EnchantmentFunction.smelting(player.level, event.getPos(), eventBlock, player, player.getUsedItemHand());
    }
}
