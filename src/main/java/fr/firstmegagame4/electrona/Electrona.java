package fr.firstmegagame4.electrona;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Electrona implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("electrona");

    @Override
    public void onInitialize() {
        LOGGER.info("Electrona - For Fabric");

        Registers.registerItems();
        Registers.registerBlocks();
        Registers.registerBlockEntities();
        Registers.registerOres();
    }

}
