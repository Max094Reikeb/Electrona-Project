package net.reikeb.electrona.advancements;

import com.google.common.collect.*;
import com.google.gson.JsonObject;

import net.minecraft.advancements.*;
import net.minecraft.advancements.criterion.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public class TriggerBase implements ICriterionTrigger<TriggerBase.Instance> {

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
    public void addPlayerListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<TriggerBase.Instance> listener) {
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
    public void removePlayerListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<TriggerBase.Instance> listener) {
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
    public TriggerBase.Instance createInstance(JsonObject object, ConditionArrayParser conditions) {
        EntityPredicate.AndPredicate entitypredicate$andpredicate = EntityPredicate.AndPredicate.fromJson(object, "sub_condition", conditions);
        return new TriggerBase.Instance(getId(), entitypredicate$andpredicate);
    }


    /**
     * Trigger.
     *
     * @param parPlayer the player
     */
    public void trigger(ServerPlayerEntity parPlayer) {
        TriggerBase.Listeners tameanimaltrigger$listeners = listeners.get(parPlayer.getAdvancements());

        if (tameanimaltrigger$listeners != null) {
            tameanimaltrigger$listeners.trigger(parPlayer);
        }
    }

    public static class Instance implements ICriterionInstance {

        private final ResourceLocation criterion;
        private final EntityPredicate.AndPredicate player;

        /**
         * Instantiates a new instance.
         *
         * @param parRL the par RL
         */
        public Instance(ResourceLocation parRL, EntityPredicate.AndPredicate playerCondition) {
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
        public JsonObject serializeToJson(ConditionArraySerializer conditions) {
            return null;
        }
    }

    static class Listeners {

        private final PlayerAdvancements playerAdvancements;
        private final Set<ICriterionTrigger.Listener<TriggerBase.Instance>> listeners = Sets.newHashSet();

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
        public void add(ICriterionTrigger.Listener<TriggerBase.Instance> listener) {
            listeners.add(listener);
        }

        /**
         * Removes the listener.
         *
         * @param listener the listener
         */
        public void remove(ICriterionTrigger.Listener<TriggerBase.Instance> listener) {
            listeners.remove(listener);
        }

        /**
         * Trigger.
         *
         * @param player the player
         */
        public void trigger(ServerPlayerEntity player) {
            ArrayList<ICriterionTrigger.Listener<TriggerBase.Instance>> list = null;

            for (ICriterionTrigger.Listener<TriggerBase.Instance> listener : listeners) {
                if (listener.getTriggerInstance().test()) {
                    if (list == null) {
                        list = Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }

            if (list != null) {
                for (ICriterionTrigger.Listener<TriggerBase.Instance> listener1 : list) {
                    listener1.run(playerAdvancements);
                }
            }
        }
    }
}
