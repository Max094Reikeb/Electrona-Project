package net.reikeb.electrona.init;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.items.*;
import net.reikeb.electrona.setup.ItemGroups;

public class ItemInit {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            Electrona.MODID);

    // Items
    public static final RegistryObject<Item> TIN_INGOT = ITEMS.register("tin_ingot", TinIngot::new);
    public static final RegistryObject<Item> TIN_PLATE = ITEMS.register("tin_plate", TinPlate::new);
    public static final RegistryObject<Item> STEEL_INGOT = ITEMS.register("steel_ingot", SteelIngot::new);
    public static final RegistryObject<Item> STEEL_PLATE = ITEMS.register("steel_plate", SteelPlate::new);
    public static final RegistryObject<Item> LEAD_INGOT = ITEMS.register("lead_ingot", LeadIngot::new);
    public static final RegistryObject<Item> GOLD_POWDER = ITEMS.register("gold_powder", GoldPowder::new);
    public static final RegistryObject<Item> PORTABLE_BATTERY = ITEMS.register("portable_battery", PortableBattery::new);
    public static final RegistryObject<Item> ELECTRONIC_CIRCUIT = ITEMS.register("electronic_circuit", ElectronicCircuit::new);
    public static final RegistryObject<Item> MOTOR = ITEMS.register("motor", Motor::new);
    public static final RegistryObject<Item> PADDLE = ITEMS.register("paddle", Paddle::new);
    public static final RegistryObject<Item> PUMP = ITEMS.register("pump", Pump::new);
    public static final RegistryObject<Item> TELEPORT_SAVER = ITEMS.register("teleport_saver", TeleportSaver::new);
    public static final RegistryObject<Item> ADVANCED_TOTEM_OF_UNDYING = ITEMS.register("advanced_totem_of_undying", AdvancedTotemOfUndying::new);
    public static final RegistryObject<Item> EMPTY_CELL = ITEMS.register("empty_cell", EmptyCell::new);
    public static final RegistryObject<Item> WATER_CELL = ITEMS.register("water_cell", WaterCell::new);
    public static final RegistryObject<Item> LAVA_CELL = ITEMS.register("lava_cell", LavaCell::new);
    public static final RegistryObject<Item> BIOMASS_CELL = ITEMS.register("biomass_cell", BiomassCell::new);
    public static final RegistryObject<Item> SUGAR_BOTTLE = ITEMS.register("sugar_bottle", SugarBottle::new);
    public static final RegistryObject<Item> YELLOWCAKE = ITEMS.register("yellowcake", Yellowcake::new);
    public static final RegistryObject<Item> CONCENTRATED_URANIUM = ITEMS.register("concentrated_uranium", ConcentratedUranium::new);
    public static final RegistryObject<Item> PURIFIED_URANIUM = ITEMS.register("purified_uranium", PurifiedUranium::new);
    public static final RegistryObject<Item> URANIUM_BAR = ITEMS.register("uranium_bar", UraniumBar::new);
    public static final RegistryObject<Item> URANIUM_DUAL_BAR = ITEMS.register("uranium_dual_bar", UraniumDualBar::new);
    public static final RegistryObject<Item> URANIUM_QUAD_BAR = ITEMS.register("uranium_quad_bar", UraniumQuadBar::new);
    // Emitter
    // Wireless Booster

    // Tools and armors
    public static final RegistryObject<Item> HAMMER = ITEMS.register("hammer", Hammer::new);
    public static final RegistryObject<Item> STEEL_SWORD = ITEMS.register("steel_sword", SteelSword::new);
    public static final RegistryObject<Item> STEEL_PICKAXE = ITEMS.register("steel_pickaxe", SteelPickaxe::new);
    public static final RegistryObject<Item> STEEL_AXE = ITEMS.register("steel_axe", SteelAxe::new);
    public static final RegistryObject<Item> STEEL_SHOVEL = ITEMS.register("steel_shovel", SteelShovel::new);
    public static final RegistryObject<Item> STEEL_HOE = ITEMS.register("steel_hoe", SteelHoe::new);
    public static final RegistryObject<Item> ANTI_RADIATION_HELMET = ITEMS.register("anti_radiation_helmet", () -> new AntiRadiationSuit(EquipmentSlotType.HEAD));
    public static final RegistryObject<Item> ANTI_RADIATION_CHESTPLATE = ITEMS.register("anti_radiation_chestplate", () -> new AntiRadiationSuit(EquipmentSlotType.CHEST));
    public static final RegistryObject<Item> ANTI_RADIATION_LEGGINGS = ITEMS.register("anti_radiation_leggings", () -> new AntiRadiationSuit(EquipmentSlotType.LEGS));
    public static final RegistryObject<Item> ANTI_RADIATION_BOOTS = ITEMS.register("anti_radiation_boots", () -> new AntiRadiationSuit(EquipmentSlotType.FEET));
    public static final RegistryObject<Item> LEAD_HELMET = ITEMS.register("lead_helmet", () -> new LeadArmor(EquipmentSlotType.HEAD));
    public static final RegistryObject<Item> LEAD_CHESTPLATE = ITEMS.register("lead_chestplate", () -> new LeadArmor(EquipmentSlotType.CHEST));
    public static final RegistryObject<Item> LEAD_LEGGINGS = ITEMS.register("lead_leggings", () -> new LeadArmor(EquipmentSlotType.LEGS));
    public static final RegistryObject<Item> LEAD_BOOTS = ITEMS.register("lead_boots", () -> new LeadArmor(EquipmentSlotType.FEET));
    public static final RegistryObject<Item> MECHANIC_WINGS = ITEMS.register("mechanic_wings", MechanicWings::new);

    // Generators & Machines - BlockItems
    public static final RegistryObject<Item> BATTERY_ITEM = ITEMS.register("battery", () ->
            new BlockItem(BlockInit.BATTERY.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_MACHINES)));

    public static final RegistryObject<Item> SOLAR_PANEL_T_1_ITEM = ITEMS.register("solar_panel_tiers1", () ->
            new BlockItem(BlockInit.SOLAR_PANEL_T_1.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_MACHINES)));

    public static final RegistryObject<Item> SOLAR_PANEL_T_2_ITEM = ITEMS.register("solar_panel_tiers2", () ->
            new BlockItem(BlockInit.SOLAR_PANEL_T_2.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_MACHINES)));

    public static final RegistryObject<Item> WATER_TURBINE_ITEM = ITEMS.register("water_turbine", () ->
            new BlockItem(BlockInit.WATER_TURBINE.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_MACHINES)));

    public static final RegistryObject<Item> HEAT_GENERATOR_ITEM = ITEMS.register("heat_generator", () ->
            new BlockItem(BlockInit.HEAT_GENERATOR.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_MACHINES)));

    public static final RegistryObject<Item> BIOMASS_GENERATOR_ITEM = ITEMS.register("biomass_generator", () ->
            new BlockItem(BlockInit.BIOMASS_GENERATOR.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_MACHINES)));
    // Nuclear Generator Controller
    // Creative Generator
    public static final RegistryObject<Item> EL_CONVERTER_ITEM = ITEMS.register("el_converter", () ->
            new BlockItem(BlockInit.EL_CONVERTER.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_MACHINES)));

    public static final RegistryObject<Item> COMPRESSOR_ITEM = ITEMS.register("compressor", () ->
            new BlockItem(BlockInit.COMPRESSOR.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_MACHINES)));

    public static final RegistryObject<Item> TELEPORTER_ITEM = ITEMS.register("teleporter", () ->
            new BlockItem(BlockInit.TELEPORTER.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_MACHINES)));

    public static final RegistryObject<Item> XP_GENERATOR_ITEM = ITEMS.register("xp_generator", () ->
            new BlockItem(BlockInit.XP_GENERATOR.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_MACHINES)));
    // Purificator
    // Water Pump
    // Sprayer
    // Mining Machine
    // Conveyor

    // Others - BlockItems
    public static final RegistryObject<Item> TIN_ORE_ITEM = ITEMS.register("tin_ore", () ->
            new BlockItem(BlockInit.TIN_ORE.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_BLOCKS)));

    public static final RegistryObject<Item> LEAD_ORE_ITEM = ITEMS.register("lead_ore", () ->
            new BlockItem(BlockInit.LEAD_ORE.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_BLOCKS)));

    public static final RegistryObject<Item> URANIUM_ORE_ITEM = ITEMS.register("uranium_ore", () ->
            new BlockItem(BlockInit.URANIUM_ORE.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_BLOCKS)));

    public static final RegistryObject<Item> TiN_BLOCK_ITEM = ITEMS.register("tin_block", () ->
            new BlockItem(BlockInit.TIN_BLOCK.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_BLOCKS)));

    public static final RegistryObject<Item> STEEL_BLOCK_ITEM = ITEMS.register("steel_block", () ->
            new BlockItem(BlockInit.STEEL_BLOCK.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_BLOCKS)));

    public static final RegistryObject<Item> STEEL_CRATE_ITEM = ITEMS.register("steel_crate", () ->
            new BlockItem(BlockInit.STEEL_CRATE.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_BLOCKS)));

    public static final RegistryObject<Item> LEAD_BLOCK_ITEM = ITEMS.register("lead_block", () ->
            new BlockItem(BlockInit.LEAD_BLOCK.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_BLOCKS)));
    // Lead Crate
    public static final RegistryObject<Item> LEAD_DOOR_ITEM = ITEMS.register("lead_door", () ->
            new BlockItem(BlockInit.LEAD_DOOR.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_BLOCKS)));

    public static final RegistryObject<Item> MACHINE_CASING_ITEM = ITEMS.register("machine_casing", () ->
            new BlockItem(BlockInit.MACHINE_CASING.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_BLOCKS)));

    public static final RegistryObject<Item> CABLE_ITEM = ITEMS.register("cable", () ->
            new BlockItem(BlockInit.CABLE.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_BLOCKS)));

    public static final RegistryObject<Item> BLUE_CABLE_ITEM = ITEMS.register("blue_cable", () ->
            new BlockItem(BlockInit.BLUE_CABLE.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_BLOCKS)));

    public static final RegistryObject<Item> COMPRESSED_OBSIDIAN_ITEM = ITEMS.register("compressed_obsidian", () ->
            new BlockItem(BlockInit.COMPRESSED_OBSIDIAN.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_BLOCKS)));
    // Cooler
    // Dimension Linker
    // Singularity
}
