package fr.firstmegagame4.electrona;

import fr.firstmegagame4.electrona.screenhandlers.CrateScreenHandler;
import fr.firstmegagame4.electrona.screenhandlers.LeadCrateScreenHandler;
import fr.firstmegagame4.electrona.screenhandlers.SteelCrateScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenHandlers {

    public static final ScreenHandlerType<CrateScreenHandler> STEEL_CRATE_SCREEN_HANDLER;
    public static final ScreenHandlerType<CrateScreenHandler> LEAD_CRATE_SCREEN_HANDLER;

    static {
        STEEL_CRATE_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(Utils.electronaIdentifier("steel_crate"), SteelCrateScreenHandler::new);
        LEAD_CRATE_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(Utils.electronaIdentifier("lead_crate"), LeadCrateScreenHandler::new);
    }

}
