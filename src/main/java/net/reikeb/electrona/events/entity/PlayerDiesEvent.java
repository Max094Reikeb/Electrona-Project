package net.reikeb.electrona.events.entity;

import net.minecraft.entity.player.*;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.*;
import net.minecraft.util.Hand;

import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.TotemPacket;

@Mod.EventBusSubscriber(modid = Electrona.MODID)
public class PlayerDiesEvent {

    @SubscribeEvent
    public static void onPlayerDies(LivingDeathEvent event) {
        if (event != null && event.getEntity() instanceof PlayerEntity) {
            if (!event.getSource().isBypassInvul()) {
                PlayerEntity player = (PlayerEntity) event.getEntity();
                if (!(player.getMainHandItem().getItem().equals(ItemInit.ADVANCED_TOTEM_OF_UNDYING.get())
                        || player.getOffhandItem().getItem().equals(ItemInit.ADVANCED_TOTEM_OF_UNDYING.get()))) return;
                Hand hand = (player.getMainHandItem().getItem().equals(ItemInit.ADVANCED_TOTEM_OF_UNDYING.get()) ? Hand.MAIN_HAND : Hand.OFF_HAND);

                player.setItemInHand(hand, ItemStack.EMPTY);
                player.inventory.setChanged();
                player.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 1200, 1));
                player.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 600, 1));
                player.addEffect(new EffectInstance(Effects.REGENERATION, 1200, 2));
                player.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 600, 1));
                player.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 1200, 0));
                player.addEffect(new EffectInstance(Effects.NIGHT_VISION, 2400, 0));
                player.addEffect(new EffectInstance(Effects.ABSORPTION, 200, 0));
                player.setHealth(player.getHealth() + 5);
                player.level.broadcastEntityEvent(player, (byte) 35);
                NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new TotemPacket());
                event.setCanceled(true);
            }
        }
    }
}
