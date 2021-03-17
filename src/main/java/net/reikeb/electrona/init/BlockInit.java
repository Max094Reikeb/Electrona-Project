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

    // Machines
    public static final RegistryObject<Battery> BATTERY = BLOCKS.register("battery", Battery::new);
    public static final RegistryObject<Compressor> COMPRESSOR = BLOCKS.register("compressor", Compressor::new);

    // Other blocks
    public static final RegistryObject<TinOre> TIN_ORE = BLOCKS.register("tin_ore", TinOre::new);
    public static final RegistryObject<TinBlock> TIN_BLOCK = BLOCKS.register("tin_block", TinBlock::new);

}
