package net.reikeb.electrona.events.block;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;

import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.misc.vm.EnchantmentFunction;

@Mod.EventBusSubscriber(modid = Electrona.MODID)
public class BlockMinedEvent {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        ServerPlayer player = (ServerPlayer) event.getPlayer();
        Block eventBlock = event.getState().getBlock();

        if (eventBlock == BlockInit.TIN_ORE.get()) {
            Advancement advancement = player.server.getAdvancements().getAdvancement(new ResourceLocation("electrona:a_whole_new_world"));
            if (advancement == null) System.out.println("Advancement A Whole New World seems to be null");
            if (advancement == null) return;
            AdvancementProgress advancementProgress = player.getAdvancements().getOrStartProgress(advancement);
            if (!advancementProgress.isDone()) {
                for (String criteria : advancementProgress.getRemainingCriteria()) {
                    player.getAdvancements().award(advancement, criteria);
                }
            }
        }

        // Enchantments triggered
        EnchantmentFunction.lumberjackMain(player, player.level, event.getPos(), Direction.values());
        EnchantmentFunction.veinminerMain(player, player.level, event.getPos(), Direction.values());
        EnchantmentFunction.smelting(player.level, event.getPos(), eventBlock, player, player.getUsedItemHand());
    }
}
