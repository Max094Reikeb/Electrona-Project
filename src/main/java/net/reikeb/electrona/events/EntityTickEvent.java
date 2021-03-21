package net.reikeb.electrona.events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.PotionEffectInit;

import java.util.Collection;

@Mod.EventBusSubscriber(modid = Electrona.MODID)
public class EntityTickEvent {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            LivingEntity entity = event.player;
            if (entity != null) {
                boolean flag = false;
                Collection<EffectInstance> effects = entity.getActiveEffects();
                for (EffectInstance effect : effects) {
                    if (effect.getEffect() == PotionEffectInit.RADIOACTIVITY.get()) {
                        flag = true;
                    }
                }
                if (!flag) {
                    entity.getPersistentData().putDouble("radioactive", 0);
                }
            }
        }
    }
}
