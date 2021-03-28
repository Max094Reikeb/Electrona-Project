package net.reikeb.electrona.init;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.*;

public class ContainerInit {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS,
            Electrona.MODID);

    public static final RegistryObject<ContainerType<BiomassGeneratorContainer>> BIOMASS_GENERATOR_CONTAINER = CONTAINERS.register("biomass_generator", () -> registerContainer(BiomassGeneratorContainer::new));
    public static final RegistryObject<ContainerType<NuclearGeneratorControllerContainer>> NUCLEAR_GENERATOR_CONTAINER = CONTAINERS.register("nuclear_generator_controller", () -> registerContainer(NuclearGeneratorControllerContainer::new));
    public static final RegistryObject<ContainerType<BatteryContainer>> BATTERY_CONTAINER = CONTAINERS.register("battery", () -> registerContainer(BatteryContainer::new));
    public static final RegistryObject<ContainerType<ConverterContainer>> CONVERTER_CONTAINER = CONTAINERS.register("el_converter", () -> registerContainer(ConverterContainer::new));
    public static final RegistryObject<ContainerType<CompressorContainer>> COMPRESSOR_CONTAINER = CONTAINERS.register("compressor", () -> registerContainer(CompressorContainer::new));
    public static final RegistryObject<ContainerType<XPGeneratorContainer>> XP_GENERATOR_CONTAINER = CONTAINERS.register("xp_generator", () -> registerContainer(XPGeneratorContainer::new));
    public static final RegistryObject<ContainerType<TeleporterContainer>> TELEPORTER_CONTAINER = CONTAINERS.register("teleporter", () -> registerContainer(TeleporterContainer::new));
    public static final RegistryObject<ContainerType<WaterPumpContainer>> WATER_PUMP_CONTAINER = CONTAINERS.register("water_pump", () -> registerContainer(WaterPumpContainer::new));
    public static final RegistryObject<ContainerType<PurificatorContainer>> PURIFICATOR_CONTAINER = CONTAINERS.register("purificator", () -> registerContainer(PurificatorContainer::new));
    public static final RegistryObject<ContainerType<SteelCrateContainer>> STEEL_CRATE_CONTAINER = CONTAINERS.register("steel_crate", () -> registerContainer(SteelCrateContainer::new));

    public static <T extends Container> ContainerType<T> registerContainer(IContainerFactory<T> fact) {
        ContainerType<T> type = new ContainerType<T>(fact);
        return type;
    }
}
