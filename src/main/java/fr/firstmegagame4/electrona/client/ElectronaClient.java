package fr.firstmegagame4.electrona.client;

import fr.firstmegagame4.electrona.setup.Registers;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ElectronaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Registers.registerScreens();
    }
}
