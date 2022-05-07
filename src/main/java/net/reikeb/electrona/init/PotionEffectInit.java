package net.reikeb.electrona.init;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.potions.RadioactivityEffect;

public class PotionEffectInit {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS,
            Electrona.MODID);

    public static final RegistryObject<MobEffect> RADIOACTIVITY = EFFECTS.register("radioactivity", RadioactivityEffect::new);
}
