package net.reikeb.electrona.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.blockentities.*;

public class BlockEntityInit {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES,
            Electrona.MODID);

    public static final RegistryObject<BlockEntityType<SolarPanelT1BlockEntity>> SOLAR_PANEL_T_1_BLOCK_ENTITY = BLOCK_ENTITIES.register("solar_panel_tiers1", () ->
            BlockEntityType.Builder.of(SolarPanelT1BlockEntity::new, BlockInit.SOLAR_PANEL_T_1.get()).build(null));

    public static final RegistryObject<BlockEntityType<SolarPanelT2BlockEntity>> SOLAR_PANEL_T_2_BLOCK_ENTITY = BLOCK_ENTITIES.register("solar_panel_tiers2", () ->
            BlockEntityType.Builder.of(SolarPanelT2BlockEntity::new, BlockInit.SOLAR_PANEL_T_2.get()).build(null));

    public static final RegistryObject<BlockEntityType<WaterTurbineBlockEntity>> WATER_TURBINE_BLOCK_ENTITY = BLOCK_ENTITIES.register("water_turbine", () ->
            BlockEntityType.Builder.of(WaterTurbineBlockEntity::new, BlockInit.WATER_TURBINE.get()).build(null));

    public static final RegistryObject<BlockEntityType<HeatGeneratorBlockEntity>> HEAT_GENERATOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("heat_generator", () ->
            BlockEntityType.Builder.of(HeatGeneratorBlockEntity::new, BlockInit.HEAT_GENERATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<BiomassGeneratorBlockEntity>> BIOMASS_GENERATOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("biomass_generator", () ->
            BlockEntityType.Builder.of(BiomassGeneratorBlockEntity::new, BlockInit.BIOMASS_GENERATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<NuclearGeneratorControllerBlockEntity>> NUCLEAR_GENERATOR_CONTROLLER_BLOCK_ENTITY = BLOCK_ENTITIES.register("nuclear_generator_controller", () ->
            BlockEntityType.Builder.of(NuclearGeneratorControllerBlockEntity::new, BlockInit.NUCLEAR_GENERATOR_CONTROLLER.get()).build(null));

    public static final RegistryObject<BlockEntityType<EnergeticLightningRodBlockEntity>> ENERGETIC_LIGHTNING_ROD_BLOCK_ENTITY = BLOCK_ENTITIES.register("energetic_lightning_rod", () ->
            BlockEntityType.Builder.of(EnergeticLightningRodBlockEntity::new, BlockInit.ENERGETIC_LIGHTNING_ROD.get()).build(null));

    public static final RegistryObject<BlockEntityType<CreativeGeneratorBlockEntity>> CREATIVE_GENERATOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("creative_generator", () ->
            BlockEntityType.Builder.of(CreativeGeneratorBlockEntity::new, BlockInit.CREATIVE_GENERATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<BatteryBlockEntity>> BATTERY_BLOCK_ENTITY = BLOCK_ENTITIES.register("battery", () ->
            BlockEntityType.Builder.of(BatteryBlockEntity::new, BlockInit.BATTERY.get()).build(null));

    public static final RegistryObject<BlockEntityType<ConverterBlockEntity>> CONVERTER_BLOCK_ENTITY = BLOCK_ENTITIES.register("el_converter", () ->
            BlockEntityType.Builder.of(ConverterBlockEntity::new, BlockInit.EL_CONVERTER.get()).build(null));

    public static final RegistryObject<BlockEntityType<CompressorBlockEntity>> COMPRESSOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("compressor", () ->
            BlockEntityType.Builder.of(CompressorBlockEntity::new, BlockInit.COMPRESSOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<XPGeneratorBlockEntity>> XP_GENERATOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("xp_generator", () ->
            BlockEntityType.Builder.of(XPGeneratorBlockEntity::new, BlockInit.XP_GENERATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<WaterPumpBlockEntity>> WATER_PUMP_BLOCK_ENTITY = BLOCK_ENTITIES.register("water_pump", () ->
            BlockEntityType.Builder.of(WaterPumpBlockEntity::new, BlockInit.WATER_PUMP.get()).build(null));

    public static final RegistryObject<BlockEntityType<TeleporterBlockEntity>> TELEPORTER_BLOCK_ENTITY = BLOCK_ENTITIES.register("teleporter", () ->
            BlockEntityType.Builder.of(TeleporterBlockEntity::new, BlockInit.TELEPORTER.get()).build(null));

    public static final RegistryObject<BlockEntityType<PurificatorBlockEntity>> PURIFICATOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("purificator", () ->
            BlockEntityType.Builder.of(PurificatorBlockEntity::new, BlockInit.PURIFICATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<MiningMachineBlockEntity>> MINING_MACHINE_BLOCK_ENTITY = BLOCK_ENTITIES.register("mining_machine", () ->
            BlockEntityType.Builder.of(MiningMachineBlockEntity::new, BlockInit.MINING_MACHINE.get()).build(null));

    public static final RegistryObject<BlockEntityType<MiningPipeBlockEntity>> MINING_PIPE_BLOCK_ENTITY = BLOCK_ENTITIES.register("mining_pipe", () ->
            BlockEntityType.Builder.of(MiningPipeBlockEntity::new, BlockInit.MINING_PIPE.get()).build(null));

    public static final RegistryObject<BlockEntityType<SprayerBlockEntity>> SPRAYER_BLOCK_ENTITY = BLOCK_ENTITIES.register("sprayer", () ->
            BlockEntityType.Builder.of(SprayerBlockEntity::new, BlockInit.SPRAYER.get()).build(null));

    public static final RegistryObject<BlockEntityType<ConveyorBlockEntity>> CONVEYOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("conveyor", () ->
            BlockEntityType.Builder.of(ConveyorBlockEntity::new, BlockInit.CONVEYOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<CableBlockEntity>> CABLE_BLOCK_ENTITY = BLOCK_ENTITIES.register("cable", () ->
            BlockEntityType.Builder.of(CableBlockEntity::new, BlockInit.CABLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlueCableBlockEntity>> BLUE_CABLE_BLOCK_ENTITY = BLOCK_ENTITIES.register("blue_cable", () ->
            BlockEntityType.Builder.of(BlueCableBlockEntity::new, BlockInit.BLUE_CABLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<WaterCableBlockEntity>> WATER_CABLE_BLOCK_ENTITY = BLOCK_ENTITIES.register("water_cable", () ->
            BlockEntityType.Builder.of(WaterCableBlockEntity::new, BlockInit.WATER_CABLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<SteelCrateBlockEntity>> STEEL_CRATE_BLOCK_ENTITY = BLOCK_ENTITIES.register("steel_crate", () ->
            BlockEntityType.Builder.of(SteelCrateBlockEntity::new, BlockInit.STEEL_CRATE.get()).build(null));

    public static final RegistryObject<BlockEntityType<LeadCrateBlockEntity>> LEAD_CRATE_BLOCK_ENTITY = BLOCK_ENTITIES.register("lead_crate", () ->
            BlockEntityType.Builder.of(LeadCrateBlockEntity::new, BlockInit.LEAD_CRATE.get()).build(null));

    public static final RegistryObject<BlockEntityType<CoolerBlockEntity>> COOLER_BLOCK_ENTITY = BLOCK_ENTITIES.register("cooler", () ->
            BlockEntityType.Builder.of(CoolerBlockEntity::new, BlockInit.COOLER.get()).build(null));

    public static final RegistryObject<BlockEntityType<DimensionLinkerBlockEntity>> DIMENSION_LINKER_BLOCK_ENTITY = BLOCK_ENTITIES.register("dimension_linker", () ->
            BlockEntityType.Builder.of(DimensionLinkerBlockEntity::new, BlockInit.DIMENSION_LINKER.get()).build(null));

    public static final RegistryObject<BlockEntityType<HoleBlockEntity>> HOLE_BLOCK_ENTITY = BLOCK_ENTITIES.register("hole", () ->
            BlockEntityType.Builder.of(HoleBlockEntity::new, BlockInit.HOLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<SingularityBlockEntity>> SINGULARITY_BLOCK_ENTITY = BLOCK_ENTITIES.register("singularity", () ->
            BlockEntityType.Builder.of(SingularityBlockEntity::new, BlockInit.SINGULARITY.get()).build(null));

    public static final RegistryObject<BlockEntityType<NuclearBombBlockEntity>> NUCLEAR_BOMB_BLOCK_ENTITY = BLOCK_ENTITIES.register("nuclear_bomb", () ->
            BlockEntityType.Builder.of(NuclearBombBlockEntity::new, BlockInit.NUCLEAR_BOMB.get()).build(null));

    public static final RegistryObject<BlockEntityType<GravitorBlockEntity>> GRAVITOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("gravitor", () ->
            BlockEntityType.Builder.of(GravitorBlockEntity::new, BlockInit.GRAVITOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<CustomSkullBlockEntity>> CUSTOM_SKULL = BLOCK_ENTITIES.register("custom_skull", () ->
            BlockEntityType.Builder.of(CustomSkullBlockEntity::new, BlockInit.RADIOACTIVE_ZOMBIE_HEAD.get(), BlockInit.RADIOACTIVE_ZOMBIE_HEAD_WALL.get()).build(null));
}
