package net.reikeb.electrona.init;

import net.minecraft.tileentity.TileEntityType;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.tileentities.*;

public class TileEntityInit {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES,
            Electrona.MODID);

    public static final RegistryObject<TileEntityType<TileSolarPanelT1>> TILE_SOLAR_PANEL_T_1 = TILE_ENTITIES.register("solar_panel_tiers1", () ->
            TileEntityType.Builder.of(TileSolarPanelT1::new, BlockInit.SOLAR_PANEL_T_1.get()).build(null));

    public static final RegistryObject<TileEntityType<TileSolarPanelT2>> TILE_SOLAR_PANEL_T_2 = TILE_ENTITIES.register("solar_panel_tiers2", () ->
            TileEntityType.Builder.of(TileSolarPanelT2::new, BlockInit.SOLAR_PANEL_T_2.get()).build(null));

    public static final RegistryObject<TileEntityType<TileWaterTurbine>> TILE_WATER_TURBINE = TILE_ENTITIES.register("water_turbine", () ->
            TileEntityType.Builder.of(TileWaterTurbine::new, BlockInit.WATER_TURBINE.get()).build(null));

    public static final RegistryObject<TileEntityType<TileHeatGenerator>> TILE_HEAT_GENERATOR = TILE_ENTITIES.register("heat_generator", () ->
            TileEntityType.Builder.of(TileHeatGenerator::new, BlockInit.HEAT_GENERATOR.get()).build(null));

    public static final RegistryObject<TileEntityType<TileBiomassGenerator>> TILE_BIOMASS_GENERATOR = TILE_ENTITIES.register("biomass_generator", () ->
            TileEntityType.Builder.of(TileBiomassGenerator::new, BlockInit.BIOMASS_GENERATOR.get()).build(null));

    public static final RegistryObject<TileEntityType<TileNuclearGeneratorController>> TILE_NUCLEAR_GENERATOR_CONTROLLER = TILE_ENTITIES.register("nuclear_generator_controller", () ->
            TileEntityType.Builder.of(TileNuclearGeneratorController::new, BlockInit.NUCLEAR_GENERATOR_CONTROLLER.get()).build(null));

    public static final RegistryObject<TileEntityType<TileCreativeGenerator>> TILE_CREATIVE_GENERATOR = TILE_ENTITIES.register("creative_generator", () ->
            TileEntityType.Builder.of(TileCreativeGenerator::new, BlockInit.CREATIVE_GENERATOR.get()).build(null));

    public static final RegistryObject<TileEntityType<TileBattery>> TILE_BATTERY = TILE_ENTITIES.register("battery", () ->
            TileEntityType.Builder.of(TileBattery::new, BlockInit.BATTERY.get()).build(null));

    public static final RegistryObject<TileEntityType<TileConverter>> TILE_CONTERTER = TILE_ENTITIES.register("el_converter", () ->
            TileEntityType.Builder.of(TileConverter::new, BlockInit.EL_CONVERTER.get()).build(null));

    public static final RegistryObject<TileEntityType<TileCompressor>> TILE_COMPRESSOR = TILE_ENTITIES.register("compressor", () ->
            TileEntityType.Builder.of(TileCompressor::new, BlockInit.COMPRESSOR.get()).build(null));

    public static final RegistryObject<TileEntityType<TileXPGenerator>> TILE_XP_GENERATOR = TILE_ENTITIES.register("xp_generator", () ->
            TileEntityType.Builder.of(TileXPGenerator::new, BlockInit.XP_GENERATOR.get()).build(null));

    public static final RegistryObject<TileEntityType<TileWaterPump>> TILE_WATER_PUMP = TILE_ENTITIES.register("water_pump", () ->
            TileEntityType.Builder.of(TileWaterPump::new, BlockInit.WATER_PUMP.get()).build(null));

    public static final RegistryObject<TileEntityType<TileTeleporter>> TILE_TELEPORTER = TILE_ENTITIES.register("teleporter", () ->
            TileEntityType.Builder.of(TileTeleporter::new, BlockInit.TELEPORTER.get()).build(null));

    public static final RegistryObject<TileEntityType<TilePurificator>> TILE_PURIFICATOR = TILE_ENTITIES.register("purificator", () ->
            TileEntityType.Builder.of(TilePurificator::new, BlockInit.PURIFICATOR.get()).build(null));

    public static final RegistryObject<TileEntityType<TileMiningMachine>> TILE_MINING_MACHINE = TILE_ENTITIES.register("mining_machine", () ->
            TileEntityType.Builder.of(TileMiningMachine::new, BlockInit.MINING_MACHINE.get()).build(null));

    public static final RegistryObject<TileEntityType<TileMiningPipe>> TILE_MINING_PIPE = TILE_ENTITIES.register("mining_pipe", () ->
            TileEntityType.Builder.of(TileMiningPipe::new, BlockInit.MINING_PIPE.get()).build(null));

    public static final RegistryObject<TileEntityType<TileSprayer>> TILE_SPRAYER = TILE_ENTITIES.register("sprayer", () ->
            TileEntityType.Builder.of(TileSprayer::new, BlockInit.SPRAYER.get()).build(null));

    public static final RegistryObject<TileEntityType<TileConveyor>> TILE_CONVEYOR = TILE_ENTITIES.register("conveyor", () ->
            TileEntityType.Builder.of(TileConveyor::new, BlockInit.CONVEYOR.get()).build(null));

    public static final RegistryObject<TileEntityType<TileCable>> TILE_CABLE = TILE_ENTITIES.register("cable", () ->
            TileEntityType.Builder.of(TileCable::new, BlockInit.CABLE.get()).build(null));

    public static final RegistryObject<TileEntityType<TileBlueCable>> TILE_BLUE_CABLE = TILE_ENTITIES.register("blue_cable", () ->
            TileEntityType.Builder.of(TileBlueCable::new, BlockInit.BLUE_CABLE.get()).build(null));

    public static final RegistryObject<TileEntityType<TileSteelCrate>> TILE_STEEL_CRATE = TILE_ENTITIES.register("steel_crate", () ->
            TileEntityType.Builder.of(TileSteelCrate::new, BlockInit.STEEL_CRATE.get()).build(null));

    public static final RegistryObject<TileEntityType<TileLeadCrate>> TILE_LEAD_CRATE = TILE_ENTITIES.register("lead_crate", () ->
            TileEntityType.Builder.of(TileLeadCrate::new, BlockInit.LEAD_CRATE.get()).build(null));

    public static final RegistryObject<TileEntityType<TileCooler>> TILE_COOLER = TILE_ENTITIES.register("cooler", () ->
            TileEntityType.Builder.of(TileCooler::new, BlockInit.COOLER.get()).build(null));

    public static final RegistryObject<TileEntityType<TileDimensionLinker>> TILE_DIMENSION_LINKER = TILE_ENTITIES.register("dimension_linker", () ->
            TileEntityType.Builder.of(TileDimensionLinker::new, BlockInit.DIMENSION_LINKER.get()).build(null));

    public static final RegistryObject<TileEntityType<TileHole>> TILE_HOLE = TILE_ENTITIES.register("hole", () ->
            TileEntityType.Builder.of(TileHole::new, BlockInit.HOLE.get()).build(null));

    public static final RegistryObject<TileEntityType<TileSingularity>> TILE_SINGULARITY = TILE_ENTITIES.register("singularity", () ->
            TileEntityType.Builder.of(TileSingularity::new, BlockInit.SINGULARITY.get()).build(null));
}
