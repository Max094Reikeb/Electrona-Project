package net.reikeb.electrona.events.local;

import net.minecraft.world.entity.Entity;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import net.reikeb.electrona.entity.EnergeticLightningBolt;

/**
 * EntityStruckByEnergeticLightningEvent is fired when an Entity is about to be struck by an energetic lightening.<br>
 * This event is fired whenever an EnergeticLightningBolt is updated to strike an Entity in
 * {@link EnergeticLightningBolt#tick()}.<br>
 * <br>
 * {@link #lightning} contains the instance of EnergeticLightningBolt attempting to strike an entity.<br>
 * <br>
 * This event is {@link Cancelable}.<br>
 * If this event is canceled, the Entity is not struck by the energetic lightening.<br>
 * <br>
 * This event does not have a result. {@link Event.HasResult}<br>
 * <br>
 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.<br>
 **/
@Cancelable
public class EntityStruckByEnergeticLightningEvent extends EntityEvent {

    private final EnergeticLightningBolt lightning;

    public EntityStruckByEnergeticLightningEvent(Entity entity, EnergeticLightningBolt lightning) {
        super(entity);
        this.lightning = lightning;
    }

    public EnergeticLightningBolt getLightning() {
        return lightning;
    }
}
