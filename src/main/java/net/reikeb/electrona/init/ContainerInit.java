package net.reikeb.electrona.init;

import net.minecraft.world.inventory.MenuType;

import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.*;

public class ContainerInit {

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Electrona.MODID);

    public static final RegistryObject<MenuType<BatteryContainer>> BATTERY_CONTAINER = CONTAINERS.register("battery",
            () -> IForgeMenuType.create((id, inv, data) -> new BatteryContainer(id, data.readBlockPos(), inv, inv.player)));

    public static final RegistryObject<MenuType<BiomassGeneratorContainer>> BIOMASS_GENERATOR_CONTAINER = CONTAINERS.register("biomass_generator",
            () -> IForgeMenuType.create((id, inv, data) -> new BiomassGeneratorContainer(id, data.readBlockPos(), inv, inv.player)));

    public static final RegistryObject<MenuType<CompressorContainer>> COMPRESSOR_CONTAINER = CONTAINERS.register("compressor",
            () -> IForgeMenuType.create((id, inv, data) -> new CompressorContainer(id, data.readBlockPos(), inv, inv.player)));

    public static final RegistryObject<MenuType<ConverterContainer>> CONVERTER_CONTAINER = CONTAINERS.register("el_converter",
            () -> IForgeMenuType.create((id, inv, data) -> new ConverterContainer(id, data.readBlockPos(), inv, inv.player)));

    public static final RegistryObject<MenuType<DimensionLinkerContainer>> DIMENSION_LINKER_CONTAINER = CONTAINERS.register("dimension_linker",
            () -> IForgeMenuType.create((id, inv, data) -> new DimensionLinkerContainer(id, data.readBlockPos(), inv, inv.player)));

    public static final RegistryObject<MenuType<LeadCrateContainer>> LEAD_CRATE_CONTAINER = CONTAINERS.register("lead_crate",
            () -> IForgeMenuType.create((id, inv, data) -> new LeadCrateContainer(id, data.readBlockPos(), inv, inv.player)));

    public static final RegistryObject<MenuType<MiningMachineContainer>> MINING_MACHINE_CONTAINER = CONTAINERS.register("mining_machine",
            () -> IForgeMenuType.create((id, inv, data) -> new MiningMachineContainer(id, data.readBlockPos(), inv, inv.player)));

    public static final RegistryObject<MenuType<NuclearBombContainer>> NUCLEAR_BOMB_CONTAINER = CONTAINERS.register("nuclear_bomb",
            () -> IForgeMenuType.create((id, inv, data) -> new NuclearBombContainer(id, data.readBlockPos(), inv, inv.player)));

    public static final RegistryObject<MenuType<NuclearGeneratorControllerContainer>> NUCLEAR_GENERATOR_CONTAINER = CONTAINERS.register("nuclear_generator_controller",
            () -> IForgeMenuType.create((id, inv, data) -> new NuclearGeneratorControllerContainer(id, data.readBlockPos(), inv, inv.player)));

    public static final RegistryObject<MenuType<PurificatorContainer>> PURIFICATOR_CONTAINER = CONTAINERS.register("purificator",
            () -> IForgeMenuType.create((id, inv, data) -> new PurificatorContainer(id, data.readBlockPos(), inv, inv.player)));

    public static final RegistryObject<MenuType<SprayerContainer>> SPRAYER_CONTAINER = CONTAINERS.register("sprayer",
            () -> IForgeMenuType.create((id, inv, data) -> new SprayerContainer(id, data.readBlockPos(), inv, inv.player)));

    public static final RegistryObject<MenuType<SteelCrateContainer>> STEEL_CRATE_CONTAINER = CONTAINERS.register("steel_crate",
            () -> IForgeMenuType.create((id, inv, data) -> new SteelCrateContainer(id, data.readBlockPos(), inv, inv.player)));

    public static final RegistryObject<MenuType<TeleporterContainer>> TELEPORTER_CONTAINER = CONTAINERS.register("teleporter",
            () -> IForgeMenuType.create((id, inv, data) -> new TeleporterContainer(id, data.readBlockPos(), inv, inv.player)));

    public static final RegistryObject<MenuType<WaterPumpContainer>> WATER_PUMP_CONTAINER = CONTAINERS.register("water_pump",
            () -> IForgeMenuType.create((id, inv, data) -> new WaterPumpContainer(id, data.readBlockPos(), inv, inv.player)));

    public static final RegistryObject<MenuType<XPGeneratorContainer>> XP_GENERATOR_CONTAINER = CONTAINERS.register("xp_generator",
            () -> IForgeMenuType.create((id, inv, data) -> new XPGeneratorContainer(id, data.readBlockPos(), inv, inv.player)));
}
