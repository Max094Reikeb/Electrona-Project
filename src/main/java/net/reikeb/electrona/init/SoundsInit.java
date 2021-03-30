package net.reikeb.electrona.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.reikeb.electrona.Electrona;

public class SoundsInit {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,
            Electrona.MODID);

    public static final RegistryObject<SoundEvent> NUCLEAR_GENERATOR_CONTROLLER_ALERT = SOUNDS
            .register("block.nuclear_generator_controller.alert", () -> setupSound("block.nuclear_generator_controller.alert"));

    private static SoundEvent setupSound(String soundName) {
        return new SoundEvent(new ResourceLocation(Electrona.MODID, soundName));
    }
}
