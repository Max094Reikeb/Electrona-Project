package fr.firstmegagame4.electrona;

import net.fabricmc.loader.api.FabricLoader;

public class Registers {

    public static void registerItems() {
        if (!FabricLoader.getInstance().isModLoaded("techreborn")) {
            Utils.registerItem("raw_tin", Items.RAW_TIN);
            Utils.registerItem("tin_ingot", Items.TIN_INGOT);
            Utils.registerItem("raw_lead", Items.RAW_LEAD);
            Utils.registerItem("lead_ingot", Items.LEAD_INGOT);
            Utils.registerItem("steel_ingot", Items.STEEL_INGOT);
        }

        Utils.registerItem("gravitonium", Items.GRAVITONIUM);

        Utils.registerItem("steel_sword", Items.STEEL_SWORD);
        Utils.registerItem("steel_pickaxe", Items.STEEL_PICKAXE);
        Utils.registerItem("steel_axe", Items.STEEL_AXE);
        Utils.registerItem("steel_shovel", Items.STEEL_SHOVEL);
        Utils.registerItem("steel_hoe", Items.STEEL_HOE);
    }

    public static void registerBlocks() {
        if (!FabricLoader.getInstance().isModLoaded("techreborn")) {
            Utils.registerBlock("tin_block", Blocks.TIN_BLOCK, Blocks.TIN_BLOCK_ITEM);
            Utils.registerBlock("lead_block", Blocks.LEAD_BLOCK, Blocks.LEAD_BLOCK_ITEM);
            Utils.registerBlock("steel_block", Blocks.STEEL_BLOCK, Blocks.STEEL_BLOCK_ITEM);
            Utils.registerBlock("gravitonium_block", Blocks.GRAVITONIUM_BLOCK, Blocks.GRAVITONIUM_BLOCK_ITEM);
        }
    }

    public static void registerOres() {
        if (!FabricLoader.getInstance().isModLoaded("techreborn")) {
            Utils.registerOverworldOre("tin_ore", Blocks.TIN_ORE, Blocks.TIN_ORE_ITEM, false, 10, 5, 0, 50);
            Utils.registerOverworldOre("lead_ore", Blocks.LEAD_ORE, Blocks.LEAD_ORE_ITEM, false, 10, 6, 0, 25);
        }

        Utils.registerOverworldOre("uranium_ore", Blocks.URANIUM_ORE, Blocks.URANIUM_ORE_ITEM, false, 8, 6, 0, 30);
        Utils.registerEndOre("gravitonium_ore", Blocks.GRAVITONIUM_ORE, Blocks.GRAVITONIUM_ORE_ITEM, 6, 4, 0, 50);
    }

}
