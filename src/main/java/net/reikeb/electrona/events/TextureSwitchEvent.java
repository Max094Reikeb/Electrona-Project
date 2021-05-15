package net.reikeb.electrona.events;

import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.reikeb.electrona.Electrona;

@Mod.EventBusSubscriber(modid = Electrona.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TextureSwitchEvent {

    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event) {
        if (event.getMap().location().equals(AtlasTexture.LOCATION_BLOCKS)) {
            event.addSprite(new ResourceLocation(Electrona.MODID, "entity/gravitor/base"));
            event.addSprite(new ResourceLocation(Electrona.MODID, "entity/gravitor/cage"));
            event.addSprite(new ResourceLocation(Electrona.MODID, "entity/gravitor/wind"));
            event.addSprite(new ResourceLocation(Electrona.MODID, "entity/gravitor/wind_vertical"));
            event.addSprite(new ResourceLocation(Electrona.MODID, "entity/gravitor/open_eye"));
            event.addSprite(new ResourceLocation(Electrona.MODID, "entity/gravitor/closed_eye"));
        }
    }
}
