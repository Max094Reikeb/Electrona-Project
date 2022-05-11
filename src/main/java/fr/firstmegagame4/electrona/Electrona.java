package fr.firstmegagame4.electrona;

import fr.firstmegagame4.mega_lib.lib.initialization.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Electrona implements MegaLibModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("electrona");

    @Override
    public String getIdentifier() {
        return "electrona";
    }

    @Override
    public String getModName() {
        return "Electrona";
    }

    @Override
    public BlocksInitializer getBlocksInitializer() {
        return new Blocks();
    }

    @Override
    public BlockEntitiesInitializer getBlockEntitiesInitializer() {
        return new BlockEntities();
    }

    @Override
    public OresInitializer getOresInitializer() {
        return new Ores();
    }

    @Override
    public ItemsInitializer getItemsInitializer() {
        return new Items();
    }

    @Override
    public void onInitialize() {
        MegaLibModInitializer.super.onInitialize();

        this.getLogger().info("Electrona - For Fabric");
    }

}
