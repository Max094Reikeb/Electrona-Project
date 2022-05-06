package net.reikeb.electrona.misc;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class Tags {

    public static Tag<Item> BIOMASS = ItemTags.getAllTags().getTagOrEmpty(Keys.BIOMASS);
    public static net.minecraftforge.common.Tags.IOptionalNamedTag<Block> BLUE_CABLE = BlockTags.createOptional(Keys.BLUE_CABLE_TAG);
    public static net.minecraftforge.common.Tags.IOptionalNamedTag<Block> BLUE_MACHINES = BlockTags.createOptional(Keys.BLUE_CABLE_MACHINE_TAG);
    public static net.minecraftforge.common.Tags.IOptionalNamedTag<Block> CABLE = BlockTags.createOptional(Keys.CABLE_TAG);
    public static net.minecraftforge.common.Tags.IOptionalNamedTag<Block> DOORS = BlockTags.createOptional(Keys.DOORS_TAG);
    public static net.minecraftforge.common.Tags.IOptionalNamedTag<Block> ELECTRONA_ORES = BlockTags.createOptional(Keys.ELECTRONA_ORES);
    public static net.minecraftforge.common.Tags.IOptionalNamedTag<Block> FORGE_ORES = BlockTags.createOptional(Keys.FORGE_ORES);
    public static net.minecraftforge.common.Tags.IOptionalNamedTag<Block> GENERATORS = BlockTags.createOptional(Keys.GENERATORS_TAG);
    public static net.minecraftforge.common.Tags.IOptionalNamedTag<Block> GLASS = BlockTags.createOptional(Keys.GLASS_TAG);
    public static net.minecraftforge.common.Tags.IOptionalNamedTag<Block> LOGS = BlockTags.createOptional(Keys.LOG_TAG);
    public static net.minecraftforge.common.Tags.IOptionalNamedTag<Block> MACHINES = BlockTags.createOptional(Keys.CABLE_MACHINE_TAG);
    public static net.minecraftforge.common.Tags.IOptionalNamedTag<Block> MINEABLE_WITH_HAMMER = BlockTags.createOptional(Keys.MINEABLE_HAMMER);
    public static net.minecraftforge.common.Tags.IOptionalNamedTag<Block> MINECRAFT_ORES = BlockTags.createOptional(Keys.MINECRAFT_ORES);
    public static net.minecraftforge.common.Tags.IOptionalNamedTag<Block> PANES = BlockTags.createOptional(Keys.PANES_TAG);
    public static net.minecraftforge.common.Tags.IOptionalNamedTag<Block> PLANKS = BlockTags.createOptional(Keys.PLANKS_TAG);
    public static Tag<Item> POWERED_ITEMS = ItemTags.getAllTags().getTagOrEmpty(Keys.CHARGEABLE_ITEMS_TAG);
    public static net.minecraftforge.common.Tags.IOptionalNamedTag<Block> SLABS = BlockTags.createOptional(Keys.SLABS_TAG);
    public static net.minecraftforge.common.Tags.IOptionalNamedTag<Block> STAIRS = BlockTags.createOptional(Keys.STAIRS_TAG);
    public static net.minecraftforge.common.Tags.IOptionalNamedTag<Block> STOPS_BLACK_HOLE = BlockTags.createOptional(Keys.STOPS_BLACK_HOLES_TAG);
    public static net.minecraftforge.common.Tags.IOptionalNamedTag<Block> WATER_CABLE = BlockTags.createOptional(Keys.WATER_CABLE_TAG);
    public static net.minecraftforge.common.Tags.IOptionalNamedTag<Block> WATER_TANK = BlockTags.createOptional(Keys.HAS_WATER_TANK_TAG);
}
