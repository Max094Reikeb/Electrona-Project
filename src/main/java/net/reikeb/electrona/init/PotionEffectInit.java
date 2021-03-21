package net.reikeb.electrona.init;

import net.minecraft.potion.Effect;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.potions.*;

public class PotionEffectInit {

    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS,
            Electrona.MODID);

    public static final RegistryObject<Effect> RADIOACTIVITY = EFFECTS.register("radioactivity", RadioactivityEffect::new);
}
