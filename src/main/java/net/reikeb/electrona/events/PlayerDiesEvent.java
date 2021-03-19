package net.reikeb.electrona.events;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.network.PacketDistributor;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.TotemPacket;

@Mod.EventBusSubscriber(modid = Electrona.MODID)
public class PlayerDiesEvent
{
    @SubscribeEvent
    public static void onPlayerDies(LivingDeathEvent event)
    {
        if (event != null && event.getEntity() instanceof PlayerEntity)
        {
            if (!event.getSource().isBypassInvul())
            {
                PlayerEntity player = (PlayerEntity) event.getEntity();
                Hand hand = null;
                if (player.getOffhandItem().getItem().equals(ItemInit.ADVANCED_TOTEM_OF_UNDYING.get()))
                {
                    hand = Hand.OFF_HAND;
                }
                else if (player.getMainHandItem().getItem().equals(ItemInit.ADVANCED_TOTEM_OF_UNDYING.get()))
                {
                    hand = Hand.MAIN_HAND;
                }
    
                if (hand != null)
                {
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
}