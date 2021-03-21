package net.reikeb.electrona.init;

import net.minecraft.inventory.container.ContainerType;

import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.*;

public class ContainerInit {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS,
            Electrona.MODID);

    public static final RegistryObject<ContainerType<BatteryContainer>> BATTERY_CONTAINER = CONTAINERS.register("battery",
            () -> IForgeContainerType.create((windowId, inv, data) -> {
                return new BatteryContainer(windowId, inv);
            }));

    public static final RegistryObject<ContainerType<CompressorContainer>> COMPRESSOR_CONTAINER = CONTAINERS.register("compressor",
            () -> IForgeContainerType.create((windowId, inv, data) -> {
                return new CompressorContainer(windowId, inv);
            }));

    public static final RegistryObject<ContainerType<SteelCrateContainer>> STEEL_CRATE_CONTAINER = CONTAINERS.register("steel_crate",
            () -> IForgeContainerType.create(((windowId, inv, data) -> {
                return new SteelCrateContainer(windowId, inv);
            })));
}
