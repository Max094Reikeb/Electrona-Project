package net.reikeb.electrona.events.entity;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.misc.vm.RadioactivityFunction;

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
                if (entity instanceof ServerPlayer && RadioactivityFunction.isEntityWearingLeadArmor(entity)) {
                    Advancement advancement = ((ServerPlayer) entity).server.getAdvancements().getAdvancement(new ResourceLocation("electrona:leader"));
                    if (advancement == null) System.out.println("Advancement Leader! seems to be null");
                    if (advancement == null) return;
                    AdvancementProgress advancementProgress = ((ServerPlayer) entity).getAdvancements().getOrStartProgress(advancement);
                    if (!advancementProgress.isDone()) {
                        for (String criteria : advancementProgress.getRemainingCriteria()) {
                            ((ServerPlayer) entity).getAdvancements().award(advancement, criteria);
                        }
                    }
                }
            }
        }
    }
}
