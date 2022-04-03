package net.reikeb.electrona.init;

import net.minecraft.sounds.SoundEvent;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.reikeb.electrona.Electrona;

public class SoundsInit {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,
            Electrona.MODID);

    public static final RegistryObject<SoundEvent> BIOMASS_GENERATOR_ACTIVE = SOUNDS
            .register("block.biomass_generator.active", () -> setupSound("block.biomass_generator.active"));

    public static final RegistryObject<SoundEvent> COMPRESSOR_END_COMPRESSION = SOUNDS
            .register("block.compressor.compression_end", () -> setupSound("block.compressor.compression_end"));

    public static final RegistryObject<SoundEvent> NUCLEAR_EXPLOSION = SOUNDS
            .register("common.nuclear_explosion", () -> setupSound("common.nuclear_explosion"));

    public static final RegistryObject<SoundEvent> NUCLEAR_GENERATOR_CONTROLLER_ALERT = SOUNDS
            .register("block.nuclear_generator_controller.alert", () -> setupSound("block.nuclear_generator_controller.alert"));

    public static final RegistryObject<SoundEvent> PURIFICATOR_PURIFICATION = SOUNDS
            .register("block.purificator.purification", () -> setupSound("block.purificator.purification"));

    public static final RegistryObject<SoundEvent> WATER_PUMPING = SOUNDS
            .register("block.water_pump.pump", () -> setupSound("block.water_pump.pump"));

    private static SoundEvent setupSound(String soundName) {
        return new SoundEvent(Electrona.RL(soundName));
    }
}
