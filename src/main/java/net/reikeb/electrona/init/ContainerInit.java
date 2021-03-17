package net.reikeb.electrona.init;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.*;

public class ContainerInit {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS,
            Electrona.MODID);

    public static final RegistryObject<ContainerType<BatteryContainer>> BATTERY_CONTAINER = CONTAINERS.register("battery", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.level;
        return new BatteryContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<ContainerType<CompressorContainer>> COMPRESSOR_CONTAINER = CONTAINERS.register("compressor",
            () -> IForgeContainerType.create((windowId, inv, data) -> {
                return new CompressorContainer(windowId, inv);
            }));
}
