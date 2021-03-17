package net.reikeb.electrona.init;

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

    // Normal items
    public static final RegistryObject<Item> PORTABLE_BATTERY = ITEMS.register("portable_battery", PortableBattery::new);
    public static final RegistryObject<Item> TIN_INGOT = ITEMS.register("tin_ingot", TinIngot::new);

    // Tools and armors
    public static final RegistryObject<Item> HAMMER = ITEMS.register("hammer", Hammer::new);
    public static final RegistryObject<Item> MECHANIC_WINGS = ITEMS.register("mechanic_wings", MechanicWings::new);

    // Generators - BlockItems
    public static final RegistryObject<Item> SOLAR_PANEL_T_1_ITEM = ITEMS.register("solar_panel_tiers1", () ->
            new BlockItem(BlockInit.SOLAR_PANEL_T_1.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_MACHINES)));
    public static final RegistryObject<Item> SOLAR_PANEL_T_2_ITEM = ITEMS.register("solar_panel_tiers2", () ->
            new BlockItem(BlockInit.SOLAR_PANEL_T_2.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_MACHINES)));
    public static final RegistryObject<Item> WATER_TURBINE_ITEM = ITEMS.register("water_turbine", () ->
            new BlockItem(BlockInit.WATER_TURBINE.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_MACHINES)));

    // Machines - BlockItems
    public static final RegistryObject<Item> BATTERY_ITEM = ITEMS.register("battery", () ->
            new BlockItem(BlockInit.BATTERY.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_MACHINES)));
    public static final RegistryObject<Item> COMPRESSOR_ITEM = ITEMS.register("compressor", () ->
            new BlockItem(BlockInit.COMPRESSOR.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_MACHINES)));

    // Others - BlockItems
    public static final RegistryObject<Item> TIN_ORE_ITEM = ITEMS.register("tin_ore", () ->
            new BlockItem(BlockInit.TIN_ORE.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_BLOCKS)));
    public static final RegistryObject<Item> TiN_BLOCK_ITEM = ITEMS.register("tin_block", () ->
            new BlockItem(BlockInit.TIN_BLOCK.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_BLOCKS)));
}
