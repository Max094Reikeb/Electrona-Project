package net.reikeb.electrona.init;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.*;

public class ContainerInit {

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Electrona.MODID);

    public static <T extends AbstractContainerMenu> MenuType<T> registerContainer(MenuType.MenuSupplier<T> menuSupplier) {
        return new MenuType<>(menuSupplier);
    }

    public static final RegistryObject<MenuType<BatteryContainer>> BATTERY_CONTAINER = CONTAINERS.register("battery",
            () -> IForgeMenuType.create((id, inv, data) -> new BatteryContainer(id, data.readBlockPos(), inv, inv.player)));

    public static final RegistryObject<MenuType<BiomassGeneratorContainer>> BIOMASS_GENERATOR_CONTAINER = CONTAINERS.register("biomass_generator",
            () -> IForgeMenuType.create((id, inv, data) -> new BiomassGeneratorContainer(id, data.readBlockPos(), inv, inv.player)));

    public static final RegistryObject<MenuType<CompressorContainer>> COMPRESSOR_CONTAINER = CONTAINERS.register("compressor",
            () -> IForgeMenuType.create((id, inv, data) -> new CompressorContainer(id, data.readBlockPos(), inv, inv.player)));

    public static final RegistryObject<MenuType<ConverterContainer>> CONVERTER_CONTAINER = CONTAINERS.register("el_converter",
            () -> IForgeMenuType.create((id, inv, data) -> new ConverterContainer(id, data.readBlockPos(), inv, inv.player)));

    public static final RegistryObject<MenuType<DimensionLinkerContainer>> DIMENSION_LINKER_CONTAINER = CONTAINERS.register("dimension_linker", () -> registerContainer(DimensionLinkerContainer::new));

    public static final RegistryObject<MenuType<LeadCrateContainer>> LEAD_CRATE_CONTAINER = CONTAINERS.register("lead_crate",
            () -> IForgeMenuType.create((id, inv, data) -> new LeadCrateContainer(id, data.readBlockPos(), inv, inv.player)));

    public static final RegistryObject<MenuType<MiningMachineContainer>> MINING_MACHINE_CONTAINER = CONTAINERS.register("mining_machine",
            () -> IForgeMenuType.create((id, inv, data) -> new MiningMachineContainer(id, data.readBlockPos(), inv, inv.player)));

    public static final RegistryObject<MenuType<NuclearBombContainer>> NUCLEAR_BOMB_CONTAINER = CONTAINERS.register("nuclear_bomb", () -> registerContainer(NuclearBombContainer::new));

    public static final RegistryObject<MenuType<NuclearGeneratorControllerContainer>> NUCLEAR_GENERATOR_CONTAINER = CONTAINERS.register("nuclear_generator_controller", () -> registerContainer(NuclearGeneratorControllerContainer::new));

    public static final RegistryObject<MenuType<PurificatorContainer>> PURIFICATOR_CONTAINER = CONTAINERS.register("purificator", () -> registerContainer(PurificatorContainer::new));

    public static final RegistryObject<MenuType<SprayerContainer>> SPRAYER_CONTAINER = CONTAINERS.register("sprayer", () -> registerContainer(SprayerContainer::new));

    public static final RegistryObject<MenuType<SteelCrateContainer>> STEEL_CRATE_CONTAINER = CONTAINERS.register("steel_crate",
            () -> IForgeMenuType.create((id, inv, data) -> new SteelCrateContainer(id, data.readBlockPos(), inv, inv.player)));

    public static final RegistryObject<MenuType<TeleporterContainer>> TELEPORTER_CONTAINER = CONTAINERS.register("teleporter", () -> registerContainer(TeleporterContainer::new));

    public static final RegistryObject<MenuType<XPGeneratorContainer>> XP_GENERATOR_CONTAINER = CONTAINERS.register("xp_generator", () -> registerContainer(XPGeneratorContainer::new));

    public static final RegistryObject<MenuType<WaterPumpContainer>> WATER_PUMP_CONTAINER = CONTAINERS.register("water_pump", () -> registerContainer(WaterPumpContainer::new));
}
