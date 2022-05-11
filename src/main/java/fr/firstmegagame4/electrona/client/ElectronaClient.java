package fr.firstmegagame4.electrona.client;

import fr.firstmegagame4.electrona.ScreenHandlers;
import fr.firstmegagame4.mega_lib.lib.initialization.MegaLibClientModInitializer;
import fr.firstmegagame4.mega_lib.lib.initialization.ScreensInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ElectronaClient implements MegaLibClientModInitializer {
    @Override
    public ScreensInitializer getScreensInitializer() {
        return new ScreenHandlers();
    }

    @Override
    public void onInitializeClient() {
        MegaLibClientModInitializer.super.onInitializeClient();
    }
}
