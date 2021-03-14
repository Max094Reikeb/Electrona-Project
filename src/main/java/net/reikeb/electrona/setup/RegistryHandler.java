package net.reikeb.electrona.setup;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.BlockItem;
import net.minecraft.tileentity.TileEntityType;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.blocks.SolarPanelT1;
import net.reikeb.electrona.blocks.SolarPanelT2;
import net.reikeb.electrona.blocks.TinBlock;
import net.reikeb.electrona.blocks.TinOre;
import net.reikeb.electrona.items.PortableBattery;
import net.reikeb.electrona.items.TinIngot;
import net.reikeb.electrona.tileentities.TileSolarPanelT1;
import net.reikeb.electrona.tileentities.TileSolarPanelT2;

public class RegistryHandler {
    // create DeferredRegister object
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Electrona.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Electrona.MODID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Electrona.MODID);

    public static void init() {
        // attach DeferredRegister to the event bus
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    // Register generators
    public static final RegistryObject<SolarPanelT1> SOLAR_PANEL_T_1 = BLOCKS.register("solar_panel_tiers1", SolarPanelT1::new);
    public static final RegistryObject<Item> SOLAR_PANEL_T_1_ITEM = ITEMS.register("solar_panel_tiers1", () ->
            new BlockItem(SOLAR_PANEL_T_1.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_MACHINES)));
    public static final RegistryObject<TileEntityType<TileSolarPanelT1>> TILE_SOLAR_PANEL_T_1 = TILES.register("solar_panel_tiers1", () ->
            TileEntityType.Builder.of(TileSolarPanelT1::new, SOLAR_PANEL_T_1.get()).build(null));

    public static final RegistryObject<SolarPanelT2> SOLAR_PANEL_T_2 = BLOCKS.register("solar_panel_tiers2", SolarPanelT2::new);
    public static final RegistryObject<Item> SOLAR_PANEL_T_2_ITEM = ITEMS.register("solar_panel_tiers2", () ->
            new BlockItem(SOLAR_PANEL_T_2.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_MACHINES)));
    public static final RegistryObject<TileEntityType<TileSolarPanelT2>> TILE_SOLAR_PANEL_T_2 = TILES.register("solar_panel_tiers2", () ->
            TileEntityType.Builder.of(TileSolarPanelT2::new, SOLAR_PANEL_T_2.get()).build(null));

    // Register machines


    // Register blocks
    public static final RegistryObject<TinBlock> TIN_BLOCK = BLOCKS.register("tin_block", TinBlock::new);
    public static final RegistryObject<Item> TiN_BLOCK_ITEM = ITEMS.register("tin_block", () ->
            new BlockItem(TIN_BLOCK.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_BLOCKS)));

    public static final RegistryObject<TinOre> TIN_ORE = BLOCKS.register("tin_ore", TinOre::new);
    public static final RegistryObject<Item> TIN_ORE_ITEM = ITEMS.register("tin_ore", () ->
            new BlockItem(TIN_ORE.get(), new Item.Properties().tab(ItemGroups.ELECTRONA_BLOCKS)));

    // Register items
    public static final RegistryObject<Item> PORTABLE_BATTERY = ITEMS.register("portable_battery", PortableBattery::new);
    public static final RegistryObject<Item> TIN_INGOT = ITEMS.register("tin_ingot", TinIngot::new);

    // Register tools
}
