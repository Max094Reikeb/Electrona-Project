package net.reikeb.electrona.misc;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class Tags {

    public static Tag<Item> BIOMASS = ItemTags.getAllTags().getTagOrEmpty(Keys.BIOMASS);
    public static Tag<Block> BLUE_CABLE = BlockTags.getAllTags().getTagOrEmpty(Keys.BLUE_CABLE_TAG);
    public static Tag<Block> BLUE_MACHINES = BlockTags.getAllTags().getTagOrEmpty(Keys.BLUE_CABLE_MACHINE_TAG);
    public static Tag<Block> CABLE = BlockTags.getAllTags().getTagOrEmpty(Keys.CABLE_TAG);
    public static Tag<Block> DOORS = BlockTags.getAllTags().getTagOrEmpty(Keys.DOORS_TAG);
    public static Tag<Block> ELECTRONA_ORES = BlockTags.getAllTags().getTagOrEmpty(Keys.ELECTRONA_ORES);
    public static Tag<Block> FORGE_ORES = BlockTags.getAllTags().getTagOrEmpty(Keys.FORGE_ORES);
    public static Tag<Block> GENERATORS = BlockTags.getAllTags().getTagOrEmpty(Keys.GENERATORS_TAG);
    public static Tag<Block> GLASS = BlockTags.getAllTags().getTagOrEmpty(Keys.GLASS_TAG);
    public static Tag<Block> LOGS = BlockTags.getAllTags().getTagOrEmpty(Keys.LOG_TAG);
    public static Tag<Block> LOG_THAT_BURN = BlockTags.getAllTags().getTagOrEmpty(Keys.LOG_THAT_BURN_TAG);
    public static Tag<Block> MACHINES = BlockTags.getAllTags().getTagOrEmpty(Keys.CABLE_MACHINE_TAG);
    public static Tag<Block> MINEABLE_WITH_HAMMER = BlockTags.getAllTags().getTagOrEmpty(Keys.MINEABLE_HAMMER);
    public static Tag<Block> MINECRAFT_ORES = BlockTags.getAllTags().getTagOrEmpty(Keys.MINECRAFT_ORES);
    public static Tag<Block> NUCLEAR_DEBRIS = BlockTags.getAllTags().getTagOrEmpty(Keys.NUCLEAR_DEBRIS);
    public static Tag<Block> PANES = BlockTags.getAllTags().getTagOrEmpty(Keys.PANES_TAG);
    public static Tag<Block> PLANKS = BlockTags.getAllTags().getTagOrEmpty(Keys.PLANKS_TAG);
    public static Tag<Item> POWERED_ITEMS = ItemTags.getAllTags().getTagOrEmpty(Keys.CHARGEABLE_ITEMS_TAG);
    public static Tag<Block> SLABS = BlockTags.getAllTags().getTagOrEmpty(Keys.SLABS_TAG);
    public static Tag<Block> STAIRS = BlockTags.getAllTags().getTagOrEmpty(Keys.STAIRS_TAG);
    public static Tag<Block> STOPS_BLACK_HOLE = BlockTags.getAllTags().getTagOrEmpty(Keys.STOPS_BLACK_HOLES_TAG);
    public static Tag<Block> WATER_CABLE = BlockTags.getAllTags().getTagOrEmpty(Keys.WATER_CABLE_TAG);
    public static Tag<Block> WATER_TANK = BlockTags.getAllTags().getTagOrEmpty(Keys.HAS_WATER_TANK_TAG);
}
