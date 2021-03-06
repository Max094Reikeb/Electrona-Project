package net.reikeb.electrona.commands;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.reikeb.electrona.Electrona;

@Mod.EventBusSubscriber(modid = Electrona.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommandRegistration {

    @SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> commandDispatcher = event.getDispatcher();
        ChargeCommand.register(commandDispatcher);
        ExplodeCommand.register(commandDispatcher);
    }
}
