package net.reikeb.electrona.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;

import net.reikeb.electrona.utils.BiomeUtil;

public class SetBiomeCommand {

    private static final DynamicCommandExceptionType ERROR_INVALID_BIOME = new DynamicCommandExceptionType((error)
            -> new TranslatableComponent("commands.locatebiome.invalid", error));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("setbiome").requires((commandSource) -> commandSource.hasPermission(2))
                .then(Commands.argument("location", Vec3Argument.vec3())
                        .then(Commands.argument("biome", ResourceLocationArgument.id()).suggests(SuggestionProviders.AVAILABLE_BIOMES)).executes((command)
                                -> setBiomeAtPos(command.getSource(), Vec3Argument.getVec3(command, "location"), command.getArgument("biome", ResourceLocation.class))))
                .then(Commands.argument("biome", ResourceLocationArgument.id()).suggests(SuggestionProviders.AVAILABLE_BIOMES)).executes((command)
                        -> setBiomeAtPos(command.getSource(), command.getSource().getPosition(), command.getArgument("biome", ResourceLocation.class))));
    }

    private static int setBiomeAtPos(CommandSourceStack source, Vec3 coordinates, ResourceLocation resourceLocation) throws CommandSyntaxException {
        Biome biome = source.getServer().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getOptional(resourceLocation).orElseThrow(()
                -> ERROR_INVALID_BIOME.create(source));
        BlockPos blockPos = new BlockPos(coordinates.x, coordinates.y, coordinates.z);
        BiomeUtil.setBiomeAtPos(source.getLevel(), blockPos, resourceLocation);
        return source.hashCode();
    }
}
