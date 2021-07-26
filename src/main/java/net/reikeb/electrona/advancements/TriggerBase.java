package net.reikeb.electrona.advancements;

import com.google.common.collect.*;
import com.google.gson.JsonObject;

import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

public class TriggerBase implements CriterionTrigger<TriggerBase.Instance> {

    private final ResourceLocation RL;
    private final Map<PlayerAdvancements, TriggerBase.Listeners> listeners = Maps.newHashMap();

    /**
     * Instantiates a new custom trigger.
     *
     * @param parString the par string
     */
    public TriggerBase(String parString) {
        RL = new ResourceLocation(parString);
    }

    /**
     * Instantiates a new custom trigger.
     *
     * @param parRL the par RL
     */
    public TriggerBase(ResourceLocation parRL) {
        RL = parRL;
    }

    /*
     * (non-Javadoc)
     * @see net.minecraft.advancements.ICriterionTrigger#getId()
     */
    @Override
    public ResourceLocation getId() {
        return RL;
    }

    /*
     * (non-Javadoc)
     * @see net.minecraft.advancements.ICriterionTrigger#addListener(net.minecraft.advancements.PlayerAdvancements, net.minecraft.advancements.ICriterionTrigger.Listener)
     */
    @Override
    public void addPlayerListener(PlayerAdvancements playerAdvancementsIn, CriterionTrigger.Listener<TriggerBase.Instance> listener) {
        TriggerBase.Listeners myCustomTrigger$listeners = listeners.get(playerAdvancementsIn);

        if (myCustomTrigger$listeners == null) {
            myCustomTrigger$listeners = new TriggerBase.Listeners(playerAdvancementsIn);
            listeners.put(playerAdvancementsIn, myCustomTrigger$listeners);
        }
        myCustomTrigger$listeners.add(listener);
    }

    /*
     * (non-Javadoc)
     * @see net.minecraft.advancements.ICriterionTrigger#removeListener(net.minecraft.advancements.PlayerAdvancements, net.minecraft.advancements.ICriterionTrigger.Listener)
     */
    @Override
    public void removePlayerListener(PlayerAdvancements playerAdvancementsIn, CriterionTrigger.Listener<TriggerBase.Instance> listener) {
        TriggerBase.Listeners tameanimaltrigger$listeners = listeners.get(playerAdvancementsIn);

        if (tameanimaltrigger$listeners != null) {
            tameanimaltrigger$listeners.remove(listener);

            if (tameanimaltrigger$listeners.isEmpty()) {
                listeners.remove(playerAdvancementsIn);
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see net.minecraft.advancements.ICriterionTrigger#removeAllListeners(net.minecraft.advancements.PlayerAdvancements)
     */
    @Override
    public void removePlayerListeners(PlayerAdvancements playerAdvancementsIn) {
        listeners.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     *
     * @param object     the json
     * @param conditions the context
     * @return the tame bird trigger. instance
     */
    @Override
    public TriggerBase.Instance createInstance(JsonObject object, DeserializationContext conditions) {
        EntityPredicate.Composite entitypredicate$andpredicate = EntityPredicate.Composite.fromJson(object, "sub_condition", conditions);
        return new TriggerBase.Instance(getId(), entitypredicate$andpredicate);
    }


    /**
     * Trigger.
     *
     * @param parPlayer the player
     */
    public void trigger(ServerPlayer parPlayer) {
        TriggerBase.Listeners tameanimaltrigger$listeners = listeners.get(parPlayer.getAdvancements());

        if (tameanimaltrigger$listeners != null) {
            tameanimaltrigger$listeners.trigger(parPlayer);
        }
    }

    public static class Instance implements CriterionTriggerInstance {

        private final ResourceLocation criterion;
        private final EntityPredicate.Composite player;

        /**
         * Instantiates a new instance.
         *
         * @param parRL the par RL
         */
        public Instance(ResourceLocation parRL, EntityPredicate.Composite playerCondition) {
            this.criterion = parRL;
            this.player = playerCondition;
        }

        /**
         * Test.
         *
         * @return true, if successful
         */
        public boolean test() {
            return true;
        }

        @Override
        public ResourceLocation getCriterion() {
            return null;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            return null;
        }
    }

    static class Listeners {

        private final PlayerAdvancements playerAdvancements;
        private final Set<CriterionTrigger.Listener<TriggerBase.Instance>> listeners = Sets.newHashSet();

        /**
         * Instantiates a new listeners.
         *
         * @param playerAdvancementsIn the player advancements in
         */
        public Listeners(PlayerAdvancements playerAdvancementsIn) {
            playerAdvancements = playerAdvancementsIn;
        }

        /**
         * Checks if is empty.
         *
         * @return true, if is empty
         */
        public boolean isEmpty() {
            return listeners.isEmpty();
        }

        /**
         * Adds the listener.
         *
         * @param listener the listener
         */
        public void add(CriterionTrigger.Listener<TriggerBase.Instance> listener) {
            listeners.add(listener);
        }

        /**
         * Removes the listener.
         *
         * @param listener the listener
         */
        public void remove(CriterionTrigger.Listener<TriggerBase.Instance> listener) {
            listeners.remove(listener);
        }

        /**
         * Trigger.
         *
         * @param player the player
         */
        public void trigger(ServerPlayer player) {
            ArrayList<CriterionTrigger.Listener<TriggerBase.Instance>> list = null;

            for (CriterionTrigger.Listener<TriggerBase.Instance> listener : listeners) {
                if (listener.getTriggerInstance().test()) {
                    if (list == null) {
                        list = Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }

            if (list != null) {
                for (CriterionTrigger.Listener<TriggerBase.Instance> listener1 : list) {
                    listener1.run(playerAdvancements);
                }
            }
        }
    }
}
