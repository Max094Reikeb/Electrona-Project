package net.reikeb.electrona.init;

import net.minecraft.block.Block;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.blocks.*;

public class BlockInit {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            Electrona.MODID);

    // Generators
    public static final RegistryObject<SolarPanelT1> SOLAR_PANEL_T_1 = BLOCKS.register("solar_panel_tiers1", SolarPanelT1::new);
    public static final RegistryObject<SolarPanelT2> SOLAR_PANEL_T_2 = BLOCKS.register("solar_panel_tiers2", SolarPanelT2::new);
    public static final RegistryObject<WaterTurbine> WATER_TURBINE = BLOCKS.register("water_turbine", WaterTurbine::new);
    public static final RegistryObject<HeatGenerator> HEAT_GENERATOR = BLOCKS.register("heat_generator", HeatGenerator::new);
    public static final RegistryObject<BiomassGenerator> BIOMASS_GENERATOR = BLOCKS.register("biomass_generator", BiomassGenerator::new);

    // Machines
    public static final RegistryObject<Battery> BATTERY = BLOCKS.register("battery", Battery::new);
    public static final RegistryObject<Compressor> COMPRESSOR = BLOCKS.register("compressor", Compressor::new);
    public static final RegistryObject<XPGenerator> XP_GENERATOR = BLOCKS.register("xp_generator", XPGenerator::new);

    // Other blocks
    public static final RegistryObject<TinOre> TIN_ORE = BLOCKS.register("tin_ore", TinOre::new);
    public static final RegistryObject<UraniumOre> URANIUM_ORE = BLOCKS.register("uranium_ore", UraniumOre::new);
    public static final RegistryObject<TinBlock> TIN_BLOCK = BLOCKS.register("tin_block", TinBlock::new);
    public static final RegistryObject<SteelBlock> STEEL_BLOCK = BLOCKS.register("steel_block", SteelBlock::new);
    public static final RegistryObject<SteelCrate> STEEL_CRATE = BLOCKS.register("steel_crate", SteelCrate::new);
    public static final RegistryObject<MachineCasing> MACHINE_CASING = BLOCKS.register("machine_casing", MachineCasing::new);
    public static final RegistryObject<Cable> CABLE = BLOCKS.register("cable", Cable::new);
    public static final RegistryObject<BlueCable> BLUE_CABLE = BLOCKS.register("blue_cable", BlueCable::new);
    public static final RegistryObject<CompressedObsidian> COMPRESSED_OBSIDIAN = BLOCKS.register("compressed_obsidian", CompressedObsidian::new);
}
