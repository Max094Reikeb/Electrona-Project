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

    private static final DynamicCommandExceptionType ERROR_NEGATIVE = new DynamicCommandExceptionType((error)
            -> new TranslatableComponent("command.electrona.negative_power", error));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("explode").requires((commandSource)
                -> commandSource.hasPermission(2)).then(Commands.literal("tnt")
                .then(Commands.argument("power", IntegerArgumentType.integer()).executes((command)
                        -> explode(command.getSource(), command.getSource().getPlayerOrException(),
                        IntegerArgumentType.getInteger(command, "power"))))).then(Commands.literal("nuclear")
                .then(Commands.argument("power", IntegerArgumentType.integer()).executes((command)
                        -> nuclearExplode(command.getSource(), command.getSource().getPlayerOrException(),
                        IntegerArgumentType.getInteger(command, "power"))))));
    }

    private static int explode(CommandSourceStack source, ServerPlayer user, int power) throws CommandSyntaxException {
        if (power > 0) {
            user.level.explode(user, user.getX(), user.getY(), user.getZ(), power, Explosion.BlockInteraction.DESTROY);
            source.sendSuccess(new TranslatableComponent("command.electrona.explosion_success"), true);
        } else {
            throw ERROR_NEGATIVE.create(power);
        }
        return power;
    }

    private static int nuclearExplode(CommandSourceStack source, ServerPlayer user, int power) throws CommandSyntaxException {
        if (power > 0) {
            new NuclearExplosion(user.level, user.blockPosition(), power);
            source.sendSuccess(new TranslatableComponent("command.electrona.nuclear_explosion_success"), true);
        } else {
            throw ERROR_NEGATIVE.create(power);
        }
        return power;
    }
}
