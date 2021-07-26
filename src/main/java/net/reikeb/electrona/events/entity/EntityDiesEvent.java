package net.reikeb.electrona.events.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.entity.RadioactiveZombie;
import net.reikeb.electrona.init.EntityInit;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.misc.DamageSources;
import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.TotemPacket;

@Mod.EventBusSubscriber(modid = Electrona.MODID)
public class EntityDiesEvent {

    @SubscribeEvent
    public static void onEntityDies(LivingDeathEvent event) {
        if (event == null) return;

        if (event.getEntity() instanceof Player) {
            if (!event.getSource().isBypassInvul()) {
                Player player = (Player) event.getEntity();
                if (!(player.getMainHandItem().getItem().equals(ItemInit.ADVANCED_TOTEM_OF_UNDYING.get())
                        || player.getOffhandItem().getItem().equals(ItemInit.ADVANCED_TOTEM_OF_UNDYING.get()))) return;
                InteractionHand hand = (player.getMainHandItem().getItem().equals(ItemInit.ADVANCED_TOTEM_OF_UNDYING.get()) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);

                player.setItemInHand(hand, ItemStack.EMPTY);
                player.inventory.setChanged();
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 1));
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 1));
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 1200, 2));
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, 1));
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1200, 0));
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 2400, 0));
                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 200, 0));
                player.setHealth(player.getHealth() + 5);
                player.level.broadcastEntityEvent(player, (byte) 35);
                NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new TotemPacket());
                event.setCanceled(true);
            }
        } else if ((event.getEntity() instanceof Zombie || event.getEntity() instanceof Husk)
                && (event.getSource() == DamageSources.RADIOACTIVITY) && (event.getEntity().level instanceof ServerLevel)) {
            RadioactiveZombie radioactiveZombie = ((Zombie) event.getEntity()).convertTo(EntityInit.RADIOACTIVE_ZOMBIE_TYPE, true);
            net.minecraftforge.event.ForgeEventFactory.onLivingConvert((LivingEntity) event.getEntity(), radioactiveZombie);
        }
    }
}
