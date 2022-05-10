package fr.firstmegagame4.electrona;

import fr.firstmegagame4.electrona.screenhandlers.CrateScreenHandler;
import fr.firstmegagame4.electrona.screenhandlers.LeadCrateScreenHandler;
import fr.firstmegagame4.electrona.screenhandlers.SteelCrateScreenHandler;
import fr.firstmegagame4.electrona.screens.CrateScreen;
import fr.firstmegagame4.mega_lib.lib.initialization.ScreensInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenHandlers implements ScreensInitializer {

    public static final ScreenHandlerType<CrateScreenHandler> STEEL_CRATE_SCREEN_HANDLER;
    public static final ScreenHandlerType<CrateScreenHandler> LEAD_CRATE_SCREEN_HANDLER;

    static {
        STEEL_CRATE_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(Utils.ELIdentifier("steel_crate"), SteelCrateScreenHandler::new);
        LEAD_CRATE_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(Utils.ELIdentifier("lead_crate"), LeadCrateScreenHandler::new);
    }

    public void register() {
        ScreenRegistry.register(ScreenHandlers.STEEL_CRATE_SCREEN_HANDLER, CrateScreen::new);
        ScreenRegistry.register(ScreenHandlers.LEAD_CRATE_SCREEN_HANDLER, CrateScreen::new);
    }

}
