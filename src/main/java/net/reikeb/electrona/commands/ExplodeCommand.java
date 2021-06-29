package net.reikeb.electrona.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.*;

import net.minecraft.command.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;

import net.reikeb.electrona.world.NuclearExplosion;

public class ExplodeCommand {

    private static final DynamicCommandExceptionType ERROR_NEGATIVE = new DynamicCommandExceptionType((error) -> {
        return new TranslationTextComponent("command.electrona.negative_power", error);
    });

    public static void register(CommandDispatcher<CommandSource> commandDispatcher) {
        commandDispatcher.register(Commands.literal("explode").requires((commandSource) -> {
            return commandSource.hasPermission(2);
        }).then(Commands.literal("tnt").then(Commands.argument("power", IntegerArgumentType.integer()).executes((command) -> {
            return explode(command.getSource(), command.getSource().getPlayerOrException(), IntegerArgumentType.getInteger(command, "power"));
        }))).then(Commands.literal("nuclear").then(Commands.argument("power", IntegerArgumentType.integer()).executes((command) -> {
            return nuclearExplode(command.getSource(), command.getSource().getPlayerOrException(), IntegerArgumentType.getInteger(command, "power"));
        }))));
    }

    private static int explode(CommandSource source, ServerPlayerEntity user, int power) throws CommandSyntaxException {
        if (power <= 0) {
            throw ERROR_NEGATIVE.create(power);
        } else {
            user.level.explode(user, user.getX(), user.getY(), user.getZ(), power, Explosion.Mode.DESTROY);
            source.sendSuccess(new TranslationTextComponent("command.electrona.explosion_success"), true);
        }

        return power;
    }

    private static int nuclearExplode(CommandSource source, ServerPlayerEntity user, int power) throws CommandSyntaxException {
        if (power <= 0) {
            throw ERROR_NEGATIVE.create(power);
        } else {
            new NuclearExplosion(user.level, (int) user.getX(), (int) user.getY(), (int) user.getZ(), power);
            source.sendSuccess(new TranslationTextComponent("command.electrona.nuclear_explosion_success"), true);
        }

        return power;
    }
}
