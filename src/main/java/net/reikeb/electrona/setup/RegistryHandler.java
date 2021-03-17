package net.reikeb.electrona.setup;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.init.ContainerInit;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.init.TileEntityInit;

public class RegistryHandler {

    public static void init() {
        BlockInit.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ContainerInit.CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ItemInit.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TileEntityInit.TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
