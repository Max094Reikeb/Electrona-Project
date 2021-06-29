package net.reikeb.electrona.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.*;

import net.minecraft.command.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;

import net.reikeb.electrona.init.ItemInit;

import java.util.*;

public class ChargeCommand {

    private static final DynamicCommandExceptionType ERROR_NEGATIVE = new DynamicCommandExceptionType((error) -> {
        return new TranslationTextComponent("command.electrona.negative_charge", error);
    });

    private static final DynamicCommandExceptionType ERROR_NEGATIVE_RESULT = new DynamicCommandExceptionType((error) -> {
        return new TranslationTextComponent("command.electrona.negative_subtraction", error);
    });

    private static final DynamicCommandExceptionType ERROR_INCOMPATIBLE = new DynamicCommandExceptionType((error) -> {
        return new TranslationTextComponent("command.electrona.cannot_be_charged", error);
    });

    private static final DynamicCommandExceptionType ERROR_NO_ITEM = new DynamicCommandExceptionType((error) -> {
        return new TranslationTextComponent("command.electrona.not_holding_items", error);
    });

    public static void register(CommandDispatcher<CommandSource> commandDispatcher) {
        commandDispatcher.register(Commands.literal("charge").requires((commandSource) -> {
            return commandSource.hasPermission(2);
        }).then(Commands.literal("set").then(Commands.argument("chargeCount", IntegerArgumentType.integer()).executes((command) -> {
            return setChargeItem(command.getSource(), Collections.singleton(command.getSource().getPlayerOrException()), IntegerArgumentType.getInteger(command, "chargeCount"));
        }))).then(Commands.literal("add").then(Commands.argument("chargeCount", IntegerArgumentType.integer()).executes((command) -> {
            return addChargeItem(command.getSource(), Collections.singleton(command.getSource().getPlayerOrException()), IntegerArgumentType.getInteger(command, "chargeCount"));
        }))));
    }

    private static int setChargeItem(CommandSource source, Collection<ServerPlayerEntity> user, int charge) throws CommandSyntaxException {
        if (charge < 0) {
            throw ERROR_NEGATIVE.create(charge);
        } else {
            for (ServerPlayerEntity serverPlayerEntity : user) {
                ItemStack itemStack = serverPlayerEntity.getMainHandItem();
                if (!itemStack.isEmpty()) {
                    if ((itemStack.getItem() == ItemInit.BATTERY_ITEM.get())
                            || (itemStack.getItem() == ItemInit.MECHANIC_WINGS.get())
                            || (itemStack.getItem() == ItemInit.PORTABLE_TELEPORTER.get())) {
                        itemStack.getOrCreateTag().putDouble("ElectronicPower", charge);
                        source.sendSuccess(new TranslationTextComponent("command.electrona.item_charged",
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

    private static int addChargeItem(CommandSource source, Collection<ServerPlayerEntity> user, int charge) throws CommandSyntaxException {
        for (ServerPlayerEntity serverPlayerEntity : user) {
            ItemStack itemStack = serverPlayerEntity.getMainHandItem();
            if (!itemStack.isEmpty()) {
                if ((itemStack.getItem() == ItemInit.BATTERY_ITEM.get())
                        || (itemStack.getItem() == ItemInit.MECHANIC_WINGS.get())
                        || (itemStack.getItem() == ItemInit.PORTABLE_TELEPORTER.get())) {
                    double electronicPower = itemStack.getOrCreateTag().getDouble("ElectronicPower");
                    if (electronicPower + charge < 0) {
                        throw ERROR_NEGATIVE_RESULT.create(charge);
                    } else {
                        itemStack.getOrCreateTag().putDouble("ElectronicPower", (electronicPower + charge));
                        if (electronicPower + charge > electronicPower) {
                            source.sendSuccess(new TranslationTextComponent("command.electrona.item_charged",
                                    itemStack.getItem().getName(itemStack).getString()), true);
                        } else {
                            source.sendSuccess(new TranslationTextComponent("command.electrona.item_uncharged",
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
