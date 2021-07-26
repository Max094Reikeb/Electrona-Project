package net.reikeb.electrona.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Explosion;

import net.reikeb.electrona.world.NuclearExplosion;

public class ExplodeCommand {

    private static final DynamicCommandExceptionType ERROR_NEGATIVE = new DynamicCommandExceptionType((error) -> {
        return new TranslatableComponent("command.electrona.negative_power", error);
    });

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("explode").requires((commandSource) -> {
            return commandSource.hasPermission(2);
        }).then(Commands.literal("tnt").then(Commands.argument("power", IntegerArgumentType.integer()).executes((command) -> {
            return explode(command.getSource(), command.getSource().getPlayerOrException(), IntegerArgumentType.getInteger(command, "power"));
        }))).then(Commands.literal("nuclear").then(Commands.argument("power", IntegerArgumentType.integer()).executes((command) -> {
            return nuclearExplode(command.getSource(), command.getSource().getPlayerOrException(), IntegerArgumentType.getInteger(command, "power"));
        }))));
    }

    private static int explode(CommandSourceStack source, ServerPlayer user, int power) throws CommandSyntaxException {
        if (power <= 0) {
            throw ERROR_NEGATIVE.create(power);
        } else {
            user.level.explode(user, user.getX(), user.getY(), user.getZ(), power, Explosion.BlockInteraction.DESTROY);
            source.sendSuccess(new TranslatableComponent("command.electrona.explosion_success"), true);
        }

        return power;
    }

    private static int nuclearExplode(CommandSourceStack source, ServerPlayer user, int power) throws CommandSyntaxException {
        if (power <= 0) {
            throw ERROR_NEGATIVE.create(power);
        } else {
            new NuclearExplosion(user.level, (int) user.getX(), (int) user.getY(), (int) user.getZ(), power);
            source.sendSuccess(new TranslatableComponent("command.electrona.nuclear_explosion_success"), true);
        }

        return power;
    }
}
