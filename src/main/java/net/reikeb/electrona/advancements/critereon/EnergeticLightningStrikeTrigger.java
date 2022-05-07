package net.reikeb.electrona.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.reikeb.electrona.entity.EnergeticLightningBolt;
import net.reikeb.electrona.misc.Keys;

import java.util.List;
import java.util.stream.Collectors;

public class EnergeticLightningStrikeTrigger extends SimpleCriterionTrigger<EnergeticLightningStrikeTrigger.TriggerInstance> {

    public ResourceLocation getId() {
        return Keys.ENERGETIC_LIGHTNING_STRIKE_TRIGGER;
    }

    public EnergeticLightningStrikeTrigger.TriggerInstance createInstance(JsonObject jsonObject, EntityPredicate.Composite entityPredicateComposite, DeserializationContext deserializationContext) {
        EntityPredicate.Composite entitypredicate$composite = EntityPredicate.Composite.fromJson(jsonObject, "lightning", deserializationContext);
        EntityPredicate.Composite entitypredicate$composite1 = EntityPredicate.Composite.fromJson(jsonObject, "bystander", deserializationContext);
        return new EnergeticLightningStrikeTrigger.TriggerInstance(entityPredicateComposite, entitypredicate$composite, entitypredicate$composite1);
    }

    public void trigger(ServerPlayer serverPlayer, EnergeticLightningBolt energeticLightningBolt, List<Entity> entityList) {
        List<LootContext> list = entityList.stream().map((entity) -> {
            return EntityPredicate.createContext(serverPlayer, entity);
        }).collect(Collectors.toList());
        LootContext lootcontext = EntityPredicate.createContext(serverPlayer, energeticLightningBolt);
        this.trigger(serverPlayer, (triggerInstance) -> {
            return triggerInstance.matches(lootcontext, list);
        });
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final EntityPredicate.Composite energeticLightning;
        private final EntityPredicate.Composite bystander;

        public TriggerInstance(EntityPredicate.Composite entityComposite, EntityPredicate.Composite entityComposite1, EntityPredicate.Composite entityComposite2) {
            super(Keys.ENERGETIC_LIGHTNING_STRIKE_TRIGGER, entityComposite);
            this.energeticLightning = entityComposite1;
            this.bystander = entityComposite2;
        }

        public static EnergeticLightningStrikeTrigger.TriggerInstance energeticLightningStrike(EntityPredicate entityPredicate, EntityPredicate entityPredicate1) {
            return new EnergeticLightningStrikeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.wrap(entityPredicate), EntityPredicate.Composite.wrap(entityPredicate1));
        }

        public boolean matches(LootContext lootContext, List<LootContext> lootContextList) {
            if (!this.energeticLightning.matches(lootContext)) {
                return false;
            } else {
                return this.bystander == EntityPredicate.Composite.ANY || !lootContextList.stream().noneMatch(this.bystander::matches);
            }
        }

        public JsonObject serializeToJson(SerializationContext serializationContext) {
            JsonObject jsonobject = super.serializeToJson(serializationContext);
            jsonobject.add("lightning", this.energeticLightning.toJson(serializationContext));
            jsonobject.add("bystander", this.bystander.toJson(serializationContext));
            return jsonobject;
        }
    }
}
