package net.reikeb.electrona.events.entity;

import net.minecraft.advancements.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.PotionEffectInit;
import net.reikeb.electrona.misc.vm.RadioactivityFunction;

import java.util.Collection;

@Mod.EventBusSubscriber(modid = Electrona.MODID)
public class EntityTickEvent {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            LivingEntity entity = event.player;
            if (entity != null) {
                // Reset radioactivity effect
                int radio = entity.getPersistentData().getInt("radioactive");
                int nextRad = entity.getPersistentData().getInt("nextRad");
                if (radio == nextRad) {
                    entity.getPersistentData().putInt("radioactive", 0);
                    entity.getPersistentData().putInt("nextRad", 0);
                } else {
                    entity.getPersistentData().putInt("nextRad", radio);
                }
                // Leader advancement
                if (entity instanceof ServerPlayerEntity && RadioactivityFunction.isEntityWearingLeadArmor(entity)) {
                    Advancement advancement = ((ServerPlayerEntity) entity).server.getAdvancements().getAdvancement(new ResourceLocation("electrona:leader"));
                    if (advancement == null) System.out.println("Advancement Leader! seems to be null");
                    if (advancement == null) return;
                    AdvancementProgress advancementProgress = ((ServerPlayerEntity) entity).getAdvancements().getOrStartProgress(advancement);
                    if (!advancementProgress.isDone()) {
                        for (String criteria : advancementProgress.getRemainingCriteria()) {
                            ((ServerPlayerEntity) entity).getAdvancements().award(advancement, criteria);
                        }
                    }
                }
            }
        }
    }
}
