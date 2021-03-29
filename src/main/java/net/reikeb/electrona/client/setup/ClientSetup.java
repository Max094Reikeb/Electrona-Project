package net.reikeb.electrona.client.setup;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;

import net.minecraft.client.renderer.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.guis.*;
import net.reikeb.electrona.init.*;
import net.reikeb.electrona.particles.DarkMatter;

import static net.reikeb.electrona.init.ContainerInit.*;

@Mod.EventBusSubscriber(modid = Electrona.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        ScreenManager.register(BIOMASS_GENERATOR_CONTAINER.get(), BiomassGeneratorWindow::new);
        ScreenManager.register(NUCLEAR_GENERATOR_CONTAINER.get(), NuclearGeneratorControllerWindow::new);
        ScreenManager.register(BATTERY_CONTAINER.get(), BatteryWindow::new);
        ScreenManager.register(CONVERTER_CONTAINER.get(), ConverterWindow::new);
        ScreenManager.register(COMPRESSOR_CONTAINER.get(), CompressorWindow::new);
        ScreenManager.register(XP_GENERATOR_CONTAINER.get(), XPGeneratorWindow::new);
        ScreenManager.register(TELEPORTER_CONTAINER.get(), TeleporterWindow::new);
        ScreenManager.register(WATER_PUMP_CONTAINER.get(), WaterPumpWindow::new);
        ScreenManager.register(PURIFICATOR_CONTAINER.get(), PurificatorWindow::new);
        ScreenManager.register(DIMENSION_LINKER_CONTAINER.get(), DimensionLinkerWindow::new);
        ScreenManager.register(STEEL_CRATE_CONTAINER.get(), SteelCrateWindow::new);

        // Make this deferred because RenderTypeLookup is not thread safe
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(BlockInit.SINGULARITY.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(BlockInit.HOLE.get(), RenderType.translucent());
        });
    }

    @SubscribeEvent
    public static void registerParticles(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(ParticleInit.DARK_MATTER.get(), DarkMatter.DarkMatterParticleFactory::new);
    }
}
