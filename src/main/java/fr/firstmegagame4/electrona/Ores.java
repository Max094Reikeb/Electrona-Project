package fr.firstmegagame4.electrona;

import fr.firstmegagame4.mega_lib.lib.generation.ores.CustomOre;
import fr.firstmegagame4.mega_lib.lib.initialization.OresInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class Ores implements OresInitializer {
    @Override
    public void register() {
        if (!FabricLoader.getInstance().isModLoaded("techreborn")) {
            new CustomOre().setVeinSize(10).setNumVeins(5).setMinHeight(0).setMaxHeight(50).overworldOre().register(Blocks.TIN_ORE.getIfCreated());
            new CustomOre().setVeinSize(10).setNumVeins(6).setMinHeight(0).setMaxHeight(24).overworldOre().register(Blocks.LEAD_ORE.getIfCreated());
        }

        new CustomOre().setVeinSize(8).setNumVeins(6).setMinHeight(0).setMaxHeight(30).overworldOre().register(Blocks.URANIUM_ORE);
        new CustomOre().setVeinSize(6).setNumVeins(4).setMinHeight(0).setMaxHeight(50).enderOre().register(Blocks.GRAVITONIUM_ORE);
    }
}
