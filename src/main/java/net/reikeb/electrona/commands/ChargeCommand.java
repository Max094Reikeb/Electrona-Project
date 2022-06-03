package net.reikeb.electrona.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.reikeb.electrona.misc.Tags;

import java.util.Collection;
import java.util.Collections;

public class ChargeCommand {

    private static final DynamicCommandExceptionType ERROR_NEGATIVE = new DynamicCommandExceptionType((error)
            -> new TranslatableComponent("command.electrona.negative_charge", error));

    private static final DynamicCommandExceptionType ERROR_NEGATIVE_RESULT = new DynamicCommandExceptionType((error)
            -> new TranslatableComponent("command.electrona.negative_subtraction", error));

    private static final DynamicCommandExceptionType ERROR_INCOMPATIBLE = new DynamicCommandExceptionType((error)
            -> new TranslatableComponent("command.electrona.cannot_be_charged", error));

    private static final DynamicCommandExceptionType ERROR_NO_ITEM = new DynamicCommandExceptionType((error)
            -> new TranslatableComponent("command.electrona.not_holding_items", error));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("charge").requires((commandSource)
                -> commandSource.hasPermission(2)).then(Commands.literal("set")
                .then(Commands.argument("chargeCount", IntegerArgumentType.integer()).executes((command)
                        -> setChargeItem(command.getSource(), Collections.singleton(command.getSource().getPlayerOrException()),
                        IntegerArgumentType.getInteger(command, "chargeCount"))))).then(Commands.literal("add")
                .then(Commands.argument("chargeCount", IntegerArgumentType.integer()).executes((command)
                        -> addChargeItem(command.getSource(), Collections.singleton(command.getSource().getPlayerOrException()),
                        IntegerArgumentType.getInteger(command, "chargeCount"))))));
    }

    private static int setChargeItem(CommandSourceStack source, Collection<ServerPlayer> user, int charge) throws CommandSyntaxException {
        if (charge < 0) {
            throw ERROR_NEGATIVE.create(charge);
        } else {
            for (ServerPlayer serverPlayerEntity : user) {
                ItemStack itemStack = serverPlayerEntity.getMainHandItem();
                if (!itemStack.isEmpty()) {
                    if (itemStack.is(Tags.POWERED_ITEMS)) {
                        itemStack.getOrCreateTag().putDouble("ElectronicPower", charge);
                        source.sendSuccess(new TranslatableComponent("command.electrona.item_charged",
                                itemStack.getItem().getName(itemStack).getString()), true);
                    } else if (user.size() >= 1) {
                        throw ERROR_INCOMPATIBLE.create(itemStack.getItem().getName(itemStack).getString());
                    }
                } else if (user.size() >= 1) {
                    throw ERROR_NO_ITEM.create(serverPlayerEntity.getName().getString());
                }
            }
        }

        return charge;
    }

    private static int addChargeItem(CommandSourceStack source, Collection<ServerPlayer> user, int charge) throws CommandSyntaxException {
        for (ServerPlayer serverPlayerEntity : user) {
            ItemStack itemStack = serverPlayerEntity.getMainHandItem();
            if (!itemStack.isEmpty()) {
                if (itemStack.is(Tags.POWERED_ITEMS)) {
                    double electronicPower = itemStack.getOrCreateTag().getDouble("ElectronicPower");
                    if (electronicPower + charge < 0) {
                        throw ERROR_NEGATIVE_RESULT.create(-charge);
                    } else {
                        itemStack.getOrCreateTag().putDouble("ElectronicPower", (electronicPower + charge));
                        if (electronicPower + charge > electronicPower) {
                            source.sendSuccess(new TranslatableComponent("command.electrona.item_charged",
                                    itemStack.getItem().getName(itemStack).getString()), true);
                        } else {
                            source.sendSuccess(new TranslatableComponent("command.electrona.item_uncharged",
                                    itemStack.getItem().getName(itemStack).getString()), true);
                        }
                    }
                } else if (user.size() >= 1) {
                    throw ERROR_INCOMPATIBLE.create(itemStack.getItem().getName(itemStack).getString());
                }
            } else if (user.size() >= 1) {
                throw ERROR_NO_ITEM.create(serverPlayerEntity.getName().getString());
            }
        }
        return charge;
    }
}