package net.reikeb.electrona.events;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.ItemInit;

@Mod.EventBusSubscriber(modid = Electrona.MODID)
public class PlayerDiesEvent {

    @SubscribeEvent
    public static void onPlayerDies(LivingDeathEvent event) {
        if (event != null && event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            World world = player.level;
            boolean flag = player.getMainHandItem().getItem() == ItemInit.ADVANCED_TOTEM_OF_UNDYING.get();
            boolean flag1 = player.getOffhandItem().getItem() == ItemInit.ADVANCED_TOTEM_OF_UNDYING.get();
            if (flag) {
                if (world.isClientSide()) Minecraft.getInstance().gameRenderer.displayItemActivation(player.getMainHandItem());
                player.setItemInHand((Hand.MAIN_HAND), new ItemStack(Blocks.AIR, 1));
                player.inventory.setChanged();
            } else if (flag1) {
                if (world.isClientSide()) Minecraft.getInstance().gameRenderer.displayItemActivation(player.getOffhandItem());
                player.setItemInHand((Hand.OFF_HAND), new ItemStack(Blocks.AIR, 1));
                player.inventory.setChanged();
            }
            if (flag || flag1) {
                player.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 1200, 1));
                player.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 600, 1));
                player.addEffect(new EffectInstance(Effects.REGENERATION, 1200, 2));
                player.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 600, 1));
                player.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 1200, 0));
                player.addEffect(new EffectInstance(Effects.NIGHT_VISION, 2400, 0));
                player.addEffect(new EffectInstance(Effects.ABSORPTION, 200, 0));
                player.setHealth(player.getHealth() + 5);
                event.setCanceled(true);
            }
        }
    }
}