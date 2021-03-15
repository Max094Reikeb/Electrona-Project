package net.reikeb.electrona.client.setup;

import net.minecraft.client.gui.ScreenManager;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.guis.BatteryWindow;
import net.reikeb.electrona.setup.RegistryHandler;

@Mod.EventBusSubscriber(modid = Electrona.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        ScreenManager.register(RegistryHandler.BATTERY_CONTAINER.get(), BatteryWindow::new);
    }
}
