package net.reikeb.electrona.init;

import net.minecraft.world.effect.MobEffect;

import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.*;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.potions.*;

public class PotionEffectInit {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS,
            Electrona.MODID);

    public static final RegistryObject<MobEffect> RADIOACTIVITY = EFFECTS.register("radioactivity", RadioactivityEffect::new);
}
