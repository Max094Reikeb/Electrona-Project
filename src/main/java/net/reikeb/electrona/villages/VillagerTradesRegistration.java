package net.reikeb.electrona.villages;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.Items;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.*;

import java.util.List;

@Mod.EventBusSubscriber(modid = Electrona.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VillagerTradesRegistration {

    @SubscribeEvent
    public static void registerTrades(VillagerTradesEvent event) {
        Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();
        if (event.getType() != Villagers.ENGINEER.get()) return;
        /**
         * Price: 8 Tin Ingots
         * Price2: None
         * Sale: 2 Emeralds
         * Max trades: 15
         * XP: 2
         * Price multiplier: 0.05F
         */
        trades.get(1).add(new RandomTradeBuilder(15, 2, 0.05F)
                .setPrice(ItemInit.TIN_INGOT.get(), 8, 8)
                .setForSale(Items.EMERALD, 2, 2)
                .build()
        );
        /**
         * Price: 3 Uranium Ore
         * Price2: None
         * Sale: 5 Emeralds
         * Max trades: 10
         * XP: 4
         * Price multiplier: 0.05F
         */
        trades.get(2).add(new RandomTradeBuilder(10, 4, 0.05F)
                .setPrice(BlockInit.URANIUM_ORE.get().asItem(), 3, 3)
                .setForSale(Items.EMERALD, 5, 5)
                .build()
        );
        /**
         * Price: 5 Emeralds
         * Price2: None
         * Sale: 1 Steel Ingot
         * Max trades: 16
         * XP: 2
         * Price multiplier: 0.05F
         */
        trades.get(1).add(new RandomTradeBuilder(16, 2, 0.05F)
                .setPrice(Items.EMERALD, 5, 5)
                .setForSale(ItemInit.STEEL_INGOT.get(), 1, 1)
                .build()
        );
        /**
         * Price: 10 Emeralds
         * Price2: None
         * Sale: 1 Emitter
         * Max trades: 10
         * XP: 10
         * Price multiplier: 0.05F
         */
        trades.get(2).add(new RandomTradeBuilder(10, 10, 0.05F)
                .setPrice(Items.EMERALD, 10, 10)
                .setForSale(ItemInit.EMITTER.get(), 1, 1)
                .build()
        );
        /**
         * Price: 15 Emeralds
         * Price2: None
         * Sale: 1 Emitter
         * Max trades: 10
         * XP: 10
         * Price multiplier: 0.05F
         */
        trades.get(2).add(new RandomTradeBuilder(10, 10, 0.05F)
                .setPrice(Items.EMERALD, 15, 15)
                .setForSale(ItemInit.WIRELESS_BOOSTER.get(), 1, 1)
                .build()
        );
        /**
         * Price: 20 Emeralds
         * Price2: None
         * Sale: 1 Conveyor
         * Max trades: 10
         * XP: 5
         * Price multiplier: 0.05F
         */
        trades.get(3).add(new RandomTradeBuilder(10, 5, 0.05F)
                .setPrice(Items.EMERALD, 20, 20)
                .setForSale(BlockInit.CONVEYOR.get().asItem(), 1, 1)
                .build()
        );
        /**
         * Price: 25 Emeralds
         * Price2: None
         * Sale: 1 Mining Machine
         * Max trades: 7
         * XP: 5
         * Price multiplier: 0.05F
         */
        trades.get(4).add(new RandomTradeBuilder(7, 5, 0.05F)
                .setPrice(Items.EMERALD, 25, 25)
                .setForSale(BlockInit.MINING_MACHINE.get().asItem(), 1, 1)
                .build()
        );
        /**
         * Price: 5 Gravitonium
         * Price2: 27 Emeralds
         * Sale: 1 Gravity Device
         * Max trades: 7
         * XP: 5
         * Price multiplier: 0.05F
         */
        trades.get(4).add(new RandomTradeBuilder(7, 5, 0.05F)
                .setPrice(ItemInit.GRAVITONIUM.get(), 5, 5)
                .setPrice2(Items.EMERALD, 27, 27)
                .setForSale(ItemInit.GRAVITY_DEVICE.get(), 1, 1)
                .build()
        );
        /**
         * Price: 30 Emeralds
         * Price2: None
         * Sale: 1 Mechanic Wing
         * Max trades: 5
         * XP: 5
         * Price multiplier: 0.05F
         */
        trades.get(5).add(new RandomTradeBuilder(5, 5, 0.05F)
                .setPrice(Items.EMERALD, 30, 30)
                .setForSale(ItemInit.MECHANIC_WINGS.get(), 1, 1)
                .build()
        );
        /**
         * Price: 50 Emeralds
         * Price2: 1 Teleport Saver
         * Sale: 1 Portable Teleporter
         * Max trades: 5
         * XP: 10
         * Price multiplier: 0.05F
         */
        trades.get(5).add(new RandomTradeBuilder(5, 10, 0.05F)
                .setPrice(Items.EMERALD, 50, 50)
                .setPrice2(ItemInit.TELEPORT_SAVER.get(), 1, 1)
                .setForSale(ItemInit.PORTABLE_TELEPORTER.get(), 1, 1)
                .build());
    }
}
