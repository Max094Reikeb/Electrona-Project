package net.reikeb.electrona.events.entity;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.entity.RadioactiveZombie;
import net.reikeb.electrona.init.EntityInit;

@Mod.EventBusSubscriber(modid = Electrona.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CreateAttributesEvent {

    @SubscribeEvent
    public static void createAttributes(EntityAttributeCreationEvent event) {
        RadioactiveZombie.createAttributes();
        event.put(EntityInit.RADIOACTIVE_ZOMBIE.get(), RadioactiveZombie.createAttributes().build());
    }
}
