package net.reikeb.electrona.misc;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class Tags {

    public static TagKey<Item> BIOMASS = ItemTags.create(Keys.BIOMASS);
    public static TagKey<Block> BLUE_CABLE = BlockTags.create(Keys.BLUE_CABLE_TAG);
    public static TagKey<Block> BLUE_MACHINES = BlockTags.create(Keys.BLUE_CABLE_MACHINE_TAG);
    public static TagKey<Block> CABLE = BlockTags.create(Keys.CABLE_TAG);
    public static TagKey<Block> DOORS = BlockTags.create(Keys.DOORS_TAG);
    public static TagKey<Block> ELECTRONA_ORES = BlockTags.create(Keys.ELECTRONA_ORES);
    public static TagKey<Block> FORGE_ORES = BlockTags.create(Keys.FORGE_ORES);
    public static TagKey<Block> GENERATORS = BlockTags.create(Keys.GENERATORS_TAG);
    public static TagKey<Block> GLASS = BlockTags.create(Keys.GLASS_TAG);
    public static TagKey<Block> LOGS = BlockTags.create(Keys.LOG_TAG);
    public static TagKey<Block> MACHINES = BlockTags.create(Keys.CABLE_MACHINE_TAG);
    public static TagKey<Block> MINEABLE_WITH_HAMMER = BlockTags.create(Keys.MINEABLE_HAMMER);
    public static TagKey<Block> MINECRAFT_ORES = BlockTags.create(Keys.MINECRAFT_ORES);
    public static TagKey<Block> PANES = BlockTags.create(Keys.PANES_TAG);
    public static TagKey<Block> PLANKS = BlockTags.create(Keys.PLANKS_TAG);
    public static TagKey<Item> POWERED_ITEMS = ItemTags.create(Keys.CHARGEABLE_ITEMS_TAG);
    public static TagKey<Block> SLABS = BlockTags.create(Keys.SLABS_TAG);
    public static TagKey<Block> STAIRS = BlockTags.create(Keys.STAIRS_TAG);
    public static TagKey<Block> STOPS_BLACK_HOLE = BlockTags.create(Keys.STOPS_BLACK_HOLES_TAG);
    public static TagKey<Block> WATER_CABLE = BlockTags.create(Keys.WATER_CABLE_TAG);
    public static TagKey<Block> WATER_TANK = BlockTags.create(Keys.HAS_WATER_TANK_TAG);

    public static boolean isBlockTag(Block block, TagKey<Block> blockTagKey) {
        return block.builtInRegistryHolder().is(blockTagKey);
    }
}
