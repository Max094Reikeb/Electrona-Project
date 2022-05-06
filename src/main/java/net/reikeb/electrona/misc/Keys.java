package net.reikeb.electrona.misc;

import net.minecraft.resources.ResourceLocation;

import net.reikeb.electrona.Electrona;

public class Keys {

    public static final ResourceLocation DEFAULT_NULL = new ResourceLocation("");

    // Advancements
    public static final ResourceLocation A_WHOLE_NEW_WORLD_ADVANCEMENT = Electrona.RL("a_whole_new_world");
    public static final ResourceLocation I_AM_INEVITABLE_ADVANCEMENT = Electrona.RL("i_am_inevitable");
    public static final ResourceLocation LEADER_ADVANCEMENT = Electrona.RL("leader");
    public static final ResourceLocation UNLOCKED_POTENTIAL_ADVANCEMENT = Electrona.RL("unlocked_potential");
    public static final ResourceLocation SKY_HIGH_ADVANCEMENT = Electrona.RL("sky_high");

    // Biomes
    public static final ResourceLocation DESERT_BIOME = new ResourceLocation("desert");
    public static final ResourceLocation NETHER_WASTES_BIOME = new ResourceLocation("nether_wastes");
    public static final ResourceLocation WARM_OCEAN_BIOME = new ResourceLocation("warm_ocean");
    public static final ResourceLocation DEEP_WARM_OCEAN_BIOME = new ResourceLocation("deep_warm_ocean");
    public static final ResourceLocation MODIFIED_WOODED_BADLANDS_BIOME = new ResourceLocation("modified_wooded_badlands_plateau");
    public static final ResourceLocation MODIFIED_BADLANDS_BIOME = new ResourceLocation("modified_badlands_plateau");
    public static final ResourceLocation CRIMSON_FOREST_BIOME = new ResourceLocation("crimson_forest");
    public static final ResourceLocation WARPED_FOREST_BIOME = new ResourceLocation("warped_forest");
    public static final ResourceLocation SOUL_SAND_VALLEY_BIOME = new ResourceLocation("soul_sand_valley");
    public static final ResourceLocation BASALT_DELTAS_BIOME = new ResourceLocation("basalt_deltas");
    public static final ResourceLocation NUCLEAR_BIOME = Electrona.RL("nuclear");

    // Blocks tags
    public static final ResourceLocation STOPS_BLACK_HOLES_TAG = Electrona.FRL("electrona/stops_black_hole");
    public static final ResourceLocation LOG_TAG = Electrona.MRL("logs");
    public static final ResourceLocation PLANKS_TAG = Electrona.MRL("planks");
    public static final ResourceLocation STAIRS_TAG = Electrona.MRL("wooden_stairs");
    public static final ResourceLocation SLABS_TAG = Electrona.MRL("wooden_slabs");
    public static final ResourceLocation DOORS_TAG = Electrona.MRL("wooden_doors");
    public static final ResourceLocation GLASS_TAG = Electrona.MRL("impermeable");
    public static final ResourceLocation PANES_TAG = Electrona.FRL("panes");

    public static final ResourceLocation MINECRAFT_ORES = Electrona.MRL("ores");
    public static final ResourceLocation FORGE_ORES = Electrona.FRL("ores");
    public static final ResourceLocation ELECTRONA_ORES = Electrona.FRL("electrona/ores");

    public static final ResourceLocation MINEABLE_HAMMER = Electrona.RL("mineable/hammer");

    // Cable & machines tags
    public static final ResourceLocation CABLE_TAG = Electrona.FRL("electrona/cable");
    public static final ResourceLocation CABLE_MACHINE_TAG = Electrona.FRL("electrona/machines_all");
    public static final ResourceLocation BLUE_CABLE_TAG = Electrona.FRL("electrona/blue_cable");
    public static final ResourceLocation BLUE_CABLE_MACHINE_TAG = Electrona.FRL("electrona/machines");
    public static final ResourceLocation WATER_CABLE_TAG = Electrona.FRL("electrona/water_cable");
    public static final ResourceLocation HAS_WATER_TANK_TAG = Electrona.FRL("electrona/has_water_tank");
    public static final ResourceLocation GENERATORS_TAG = Electrona.FRL("electrona/generators");

    // Configured structures
    public static final ResourceLocation CONFIGURED_RUINS = Electrona.RL("configured_ruins");
    public static final ResourceLocation RUINS_START_POOL = Electrona.RL("ruins/start_pool");

    // Dimensions
    public static final ResourceLocation OVERWORLD = Electrona.MRL("overworld");

    // Engineer House
    public static final ResourceLocation ENGINEER_HOUSE_PLAINS = Electrona.RL("villages/engineer_house_plains");
    public static final ResourceLocation ENGINEER_HOUSE_SAVANNA = Electrona.RL("villages/engineer_house_savanna");
    public static final ResourceLocation ENGINEER_HOUSE_DESERT = Electrona.RL("villages/engineer_house_desert");
    public static final ResourceLocation ENGINEER_HOUSE_TAIGA = Electrona.RL("villages/engineer_house_taiga");
    public static final ResourceLocation ENGINEER_HOUSE_SNOWY = Electrona.RL("villages/engineer_house_snowy");

    // Entities
    public static final ResourceLocation GRAVITOR_BASE = Electrona.RL("entity/gravitor/base");
    public static final ResourceLocation GRAVITOR_CAGE = Electrona.RL("entity/gravitor/cage");
    public static final ResourceLocation GRAVITOR_WIND = Electrona.RL("entity/gravitor/wind");
    public static final ResourceLocation GRAVITOR_WIND_VERTICAL = Electrona.RL("entity/gravitor/wind_vertical");
    public static final ResourceLocation GRAVITOR_OPEN_EYE = Electrona.RL("entity/gravitor/open_eye");
    public static final ResourceLocation GRAVITOR_CLOSED_EYE = Electrona.RL("entity/gravitor/closed_eye");
    public static final ResourceLocation MECHANIC_WINGS = Electrona.RL("textures/models/mechanic_wings.png");
    public static final ResourceLocation PLAYER = Electrona.MRL("player");
    public static final ResourceLocation RADIOACTIVE_ZOMBIE = Electrona.RL("textures/entity/radioactive_zombie.png");
    public static final ResourceLocation SINGULARITY_BEAM_LOCATION = new ResourceLocation("textures/entity/beacon_beam.png");

    // GUIs
    public static final ResourceLocation BATTERY_GUI = Electrona.RL("textures/guis/battery_gui.png");
    public static final ResourceLocation BIOMASS_GENERATOR_GUI = Electrona.RL("textures/guis/biomass_generator_gui.png");
    public static final ResourceLocation COMPRESSOR_GUI = Electrona.RL("textures/guis/compressor_gui.png");
    public static final ResourceLocation CONVERTER_GUI = Electrona.RL("textures/guis/converter_gui.png");
    public static final ResourceLocation CRATE_GUI = Electrona.RL("textures/guis/crate_gui.png");
    public static final ResourceLocation DIMENSION_LINKER_GUI = Electrona.RL("textures/guis/dimension_linker_gui.png");
    public static final ResourceLocation MINING_MACHINE_GUI = Electrona.RL("textures/guis/mining_machine_gui.png");
    public static final ResourceLocation NUCLEAR_BOMB_GUI = Electrona.RL("textures/guis/nuclear_bomb_gui.png");
    public static final ResourceLocation NUCLEAR_GENERATOR_CONTROLLER_GUI = Electrona.RL("textures/guis/nuclear_generator_controller_gui.png");
    public static final ResourceLocation PURIFICATOR_GUI = Electrona.RL("textures/guis/purificator_gui.png");
    public static final ResourceLocation SPRAYER_GUI = Electrona.RL("textures/guis/sprayer_gui.png");
    public static final ResourceLocation TELEPORTER_GUI = Electrona.RL("textures/guis/teleporter_gui.png");
    public static final ResourceLocation WATER_PUMP_GUI = Electrona.RL("textures/guis/water_pump_gui.png");
    public static final ResourceLocation XP_GENERATOR_GUI = Electrona.RL("textures/guis/xp_generator_gui.png");

    // ItemProperties
    public static final ResourceLocation ANGLE_PROPERTY = new ResourceLocation("angle");

    // Items tag
    public static final ResourceLocation CHARGEABLE_ITEMS_TAG = Electrona.FRL("electrona/chargeable_items");

    // JEI IDs
    public static final ResourceLocation DEFAULT_ID = Electrona.RL("default");
    public static final ResourceLocation COMPRESSOR_ID = Electrona.RL("compressor_category");
    public static final ResourceLocation PURIFICATOR_ID = Electrona.RL("purificator_category");

    // Others
    public static final ResourceLocation BIOMASS = new ResourceLocation(("forge:biomass").toLowerCase(java.util.Locale.ENGLISH));

    // Potions
    public static final ResourceLocation RADIOACTIVITY_EFFECT = Electrona.RL("textures/mob_effect/radioactivity.png");

    // Recipes
    public static final ResourceLocation COMPRESSING = Electrona.RL("compressing");
    public static final ResourceLocation PURIFYING = Electrona.RL("purifying");
    public static final ResourceLocation SHAPED_HAMMER = Electrona.RL("hammer_shaped");
    public static final ResourceLocation SHAPELESS_HAMMER = Electrona.RL("hammer_shapeless");

    // Triggers
    public static final ResourceLocation ENERGETIC_LIGHTNING_STRIKE_TRIGGER = new ResourceLocation("energetic_lightning_strike");
}
