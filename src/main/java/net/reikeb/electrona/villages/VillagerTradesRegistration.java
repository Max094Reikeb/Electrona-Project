package net.reikeb.electrona.villages;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.init.ItemInit;

import java.util.List;

@Mod.EventBusSubscriber(modid = Electrona.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VillagerTradesRegistration {

    @SubscribeEvent
    public static void registerTrades(VillagerTradesEvent event) {
        Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
        if (event.getType() != Villagers.ENGINEER.get()) return;
        /**
         * Price: 8 Tin Ingots
         * Price2: None
         * Sale: 2 Emeralds
         * Max trades: 15
         * XP: 2
         * Price multiplier: 0.05F
         */
        trades.get(1).add(new BasicItemListing(new ItemStack(ItemInit.TIN_INGOT.get(), 8),
                new ItemStack(Items.EMERALD, 2), 15, 2, 0.05F)
        );
        /**
         * Price: 3 Uranium Ore
         * Price2: None
         * Sale: 5 Emeralds
         * Max trades: 10
         * XP: 4
         * Price multiplier: 0.05F
         */
        trades.get(2).add(new BasicItemListing(new ItemStack(BlockInit.URANIUM_ORE.get(), 3),
                new ItemStack(Items.EMERALD, 5), 10, 4, 0.05F)
        );
        /**
         * Price: 5 Emeralds
         * Price2: None
         * Sale: 1 Steel Ingot
         * Max trades: 16
         * XP: 2
         * Price multiplier: 0.05F
         */
        trades.get(1).add(new BasicItemListing(new ItemStack(Items.EMERALD, 5),
                new ItemStack(ItemInit.STEEL_INGOT.get(), 1), 16, 2, 0.05F)
        );
        /**
         * Price: 10 Emeralds
         * Price2: None
         * Sale: 1 Emitter
         * Max trades: 10
         * XP: 10
         * Price multiplier: 0.05F
         */
        trades.get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 10),
                new ItemStack(ItemInit.EMITTER.get(), 1), 10, 10, 0.05F)
        );
        /**
         * Price: 15 Emeralds
         * Price2: None
         * Sale: 1 Emitter
         * Max trades: 10
         * XP: 10
         * Price multiplier: 0.05F
         */
        trades.get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 15),
                new ItemStack(ItemInit.WIRELESS_BOOSTER.get(), 1), 10, 10, 0.05F)
        );
        /**
         * Price: 20 Emeralds
         * Price2: None
         * Sale: 1 Conveyor
         * Max trades: 10
         * XP: 5
         * Price multiplier: 0.05F
         */
        trades.get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 20),
                new ItemStack(BlockInit.CONVEYOR.get()), 10, 5, 0.05F)
        );
        /**
         * Price: 25 Emeralds
         * Price2: None
         * Sale: 1 Mining Machine
         * Max trades: 7
         * XP: 5
         * Price multiplier: 0.05F
         */
        trades.get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 25),
                new ItemStack(BlockInit.MINING_MACHINE.get(), 1), 7, 5, 0.05F)
        );
        /**
         * Price: 5 Gravitonium
         * Price2: 27 Emeralds
         * Sale: 1 Gravity Device
         * Max trades: 7
         * XP: 5
         * Price multiplier: 0.05F
         */
        trades.get(4).add(new BasicItemListing(new ItemStack(ItemInit.GRAVITONIUM.get(), 5),
                new ItemStack(Items.EMERALD, 27), new ItemStack(ItemInit.GRAVITY_DEVICE.get(), 1),
                7, 5, 0.05F)
        );
        /**
         * Price: 30 Emeralds
         * Price2: None
         * Sale: 1 Mechanic Wing
         * Max trades: 5
         * XP: 5
         * Price multiplier: 0.05F
         */
        trades.get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 30),
                new ItemStack(ItemInit.MECHANIC_WINGS.get(), 1), 5, 5, 0.05F)
        );
        /**
         * Price: 50 Emeralds
         * Price2: 1 Teleport Saver
         * Sale: 1 Portable Teleporter
         * Max trades: 5
         * XP: 10
         * Price multiplier: 0.05F
         */
        trades.get(5).add(new BasicItemListing(new ItemStack(Items.EMERALD, 50),
                new ItemStack(ItemInit.TELEPORT_SAVER.get(), 1),
                new ItemStack(ItemInit.PORTABLE_TELEPORTER.get(), 1), 5, 10, 0.05F)
        );
    }
}
