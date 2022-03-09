package net.reikeb.electrona.init;

import net.minecraft.world.level.block.entity.BlockEntityType;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.tileentities.*;

public class TileEntityInit {

    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES,
            Electrona.MODID);

    public static final RegistryObject<BlockEntityType<TileSolarPanelT1>> TILE_SOLAR_PANEL_T_1 = TILE_ENTITIES.register("solar_panel_tiers1", () ->
            BlockEntityType.Builder.of(TileSolarPanelT1::new, BlockInit.SOLAR_PANEL_T_1.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileSolarPanelT2>> TILE_SOLAR_PANEL_T_2 = TILE_ENTITIES.register("solar_panel_tiers2", () ->
            BlockEntityType.Builder.of(TileSolarPanelT2::new, BlockInit.SOLAR_PANEL_T_2.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileWaterTurbine>> TILE_WATER_TURBINE = TILE_ENTITIES.register("water_turbine", () ->
            BlockEntityType.Builder.of(TileWaterTurbine::new, BlockInit.WATER_TURBINE.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileHeatGenerator>> TILE_HEAT_GENERATOR = TILE_ENTITIES.register("heat_generator", () ->
            BlockEntityType.Builder.of(TileHeatGenerator::new, BlockInit.HEAT_GENERATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileBiomassGenerator>> TILE_BIOMASS_GENERATOR = TILE_ENTITIES.register("biomass_generator", () ->
            BlockEntityType.Builder.of(TileBiomassGenerator::new, BlockInit.BIOMASS_GENERATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileNuclearGeneratorController>> TILE_NUCLEAR_GENERATOR_CONTROLLER = TILE_ENTITIES.register("nuclear_generator_controller", () ->
            BlockEntityType.Builder.of(TileNuclearGeneratorController::new, BlockInit.NUCLEAR_GENERATOR_CONTROLLER.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileEnergeticLightningRod>> TILE_ENERGETIC_LIGHTNING_ROD = TILE_ENTITIES.register("energetic_lightning_rod", () ->
            BlockEntityType.Builder.of(TileEnergeticLightningRod::new, BlockInit.ENERGETIC_LIGHTNING_ROD.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileCreativeGenerator>> TILE_CREATIVE_GENERATOR = TILE_ENTITIES.register("creative_generator", () ->
            BlockEntityType.Builder.of(TileCreativeGenerator::new, BlockInit.CREATIVE_GENERATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileBattery>> TILE_BATTERY = TILE_ENTITIES.register("battery", () ->
            BlockEntityType.Builder.of(TileBattery::new, BlockInit.BATTERY.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileConverter>> TILE_CONVERTER = TILE_ENTITIES.register("el_converter", () ->
            BlockEntityType.Builder.of(TileConverter::new, BlockInit.EL_CONVERTER.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileCompressor>> TILE_COMPRESSOR = TILE_ENTITIES.register("compressor", () ->
            BlockEntityType.Builder.of(TileCompressor::new, BlockInit.COMPRESSOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileXPGenerator>> TILE_XP_GENERATOR = TILE_ENTITIES.register("xp_generator", () ->
            BlockEntityType.Builder.of(TileXPGenerator::new, BlockInit.XP_GENERATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileWaterPump>> TILE_WATER_PUMP = TILE_ENTITIES.register("water_pump", () ->
            BlockEntityType.Builder.of(TileWaterPump::new, BlockInit.WATER_PUMP.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileTeleporter>> TILE_TELEPORTER = TILE_ENTITIES.register("teleporter", () ->
            BlockEntityType.Builder.of(TileTeleporter::new, BlockInit.TELEPORTER.get()).build(null));

    public static final RegistryObject<BlockEntityType<TilePurificator>> TILE_PURIFICATOR = TILE_ENTITIES.register("purificator", () ->
            BlockEntityType.Builder.of(TilePurificator::new, BlockInit.PURIFICATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileMiningMachine>> TILE_MINING_MACHINE = TILE_ENTITIES.register("mining_machine", () ->
            BlockEntityType.Builder.of(TileMiningMachine::new, BlockInit.MINING_MACHINE.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileMiningPipe>> TILE_MINING_PIPE = TILE_ENTITIES.register("mining_pipe", () ->
            BlockEntityType.Builder.of(TileMiningPipe::new, BlockInit.MINING_PIPE.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileSprayer>> TILE_SPRAYER = TILE_ENTITIES.register("sprayer", () ->
            BlockEntityType.Builder.of(TileSprayer::new, BlockInit.SPRAYER.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileConveyor>> TILE_CONVEYOR = TILE_ENTITIES.register("conveyor", () ->
            BlockEntityType.Builder.of(TileConveyor::new, BlockInit.CONVEYOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileCable>> TILE_CABLE = TILE_ENTITIES.register("cable", () ->
            BlockEntityType.Builder.of(TileCable::new, BlockInit.CABLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileBlueCable>> TILE_BLUE_CABLE = TILE_ENTITIES.register("blue_cable", () ->
            BlockEntityType.Builder.of(TileBlueCable::new, BlockInit.BLUE_CABLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileWaterCable>> TILE_WATER_CABLE = TILE_ENTITIES.register("water_cable", () ->
            BlockEntityType.Builder.of(TileWaterCable::new, BlockInit.WATER_CABLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileSteelCrate>> TILE_STEEL_CRATE = TILE_ENTITIES.register("steel_crate", () ->
            BlockEntityType.Builder.of(TileSteelCrate::new, BlockInit.STEEL_CRATE.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileLeadCrate>> TILE_LEAD_CRATE = TILE_ENTITIES.register("lead_crate", () ->
            BlockEntityType.Builder.of(TileLeadCrate::new, BlockInit.LEAD_CRATE.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileCooler>> TILE_COOLER = TILE_ENTITIES.register("cooler", () ->
            BlockEntityType.Builder.of(TileCooler::new, BlockInit.COOLER.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileDimensionLinker>> TILE_DIMENSION_LINKER = TILE_ENTITIES.register("dimension_linker", () ->
            BlockEntityType.Builder.of(TileDimensionLinker::new, BlockInit.DIMENSION_LINKER.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileHole>> TILE_HOLE = TILE_ENTITIES.register("hole", () ->
            BlockEntityType.Builder.of(TileHole::new, BlockInit.HOLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileSingularity>> TILE_SINGULARITY = TILE_ENTITIES.register("singularity", () ->
            BlockEntityType.Builder.of(TileSingularity::new, BlockInit.SINGULARITY.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileNuclearBomb>> TILE_NUCLEAR_BOMB = TILE_ENTITIES.register("nuclear_bomb", () ->
            BlockEntityType.Builder.of(TileNuclearBomb::new, BlockInit.NUCLEAR_BOMB.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileGravitor>> TILE_GRAVITOR = TILE_ENTITIES.register("gravitor", () ->
            BlockEntityType.Builder.of(TileGravitor::new, BlockInit.GRAVITOR.get()).build(null));
}
