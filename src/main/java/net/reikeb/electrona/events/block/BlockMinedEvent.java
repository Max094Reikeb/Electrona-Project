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
import net.reikeb.maxilib.utils.Utils;

@Mod.EventBusSubscriber(modid = Electrona.MODID)
public class BlockMinedEvent {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        ServerPlayer serverPlayer = (ServerPlayer) event.getPlayer();
        Block eventBlock = event.getState().getBlock();

        if (eventBlock == BlockInit.TIN_ORE.get()) {
            Advancement advancement = serverPlayer.server.getAdvancements().getAdvancement(Keys.A_WHOLE_NEW_WORLD_ADVANCEMENT);
            Utils.awardAdvancement(serverPlayer, advancement, "A Whole New World");
        }

        // Enchantments triggered
        EnchantmentFunction.lumberjackMain(serverPlayer, serverPlayer.level, event.getPos(), Direction.values());
        EnchantmentFunction.veinminerMain(serverPlayer, serverPlayer.level, event.getPos(), Direction.values());
        EnchantmentFunction.smelting(serverPlayer.level, event.getPos(), eventBlock, serverPlayer, serverPlayer.getUsedItemHand());
    }
}
