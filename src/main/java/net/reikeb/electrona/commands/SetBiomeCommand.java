package net.reikeb.electrona.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.reikeb.maxilib.utils.BiomeUtil;

public class SetBiomeCommand {

    public static final SuggestionProvider<CommandSourceStack> AVAILABLE_BIOMES = SuggestionProviders.register(
            new ResourceLocation("available_biomes"), (context, builder) ->
                    SharedSuggestionProvider.suggestResource(context.getSource().registryAccess()
                            .registryOrThrow(Registry.BIOME_REGISTRY).keySet(), builder)
    );

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("setbiome").requires((commandSource)
                        -> commandSource.hasPermission(2))
                .then(Commands.argument("location", Vec3Argument.vec3())
                        .then(Commands.argument("biome", ResourceLocationArgument.id()).suggests(AVAILABLE_BIOMES)
                                .executes((command) -> setBiomeAtPos(command.getSource(), Vec3Argument.getVec3(command, "location"),
                                        ResourceLocationArgument.getId(command, "biome")))))
                .then(Commands.argument("biome", ResourceLocationArgument.id()).suggests(AVAILABLE_BIOMES)
                        .executes((command) -> setBiomeAtPos(command.getSource(), command.getSource().getPosition(),
                                ResourceLocationArgument.getId(command, "biome")))));
    }

    private static int setBiomeAtPos(CommandSourceStack source, Vec3 coordinates, ResourceLocation biomeLocation) {
        BlockPos pos = new BlockPos((int) coordinates.x, (int) coordinates.y, (int) coordinates.z);
        BlockPos.betweenClosed(pos.getX() - 10, pos.getY() - 10, pos.getZ() - 10, pos.getX() + 10, pos.getY() + 10, pos.getZ() + 10).forEach(blockPos -> {
            BiomeUtil.setBiomeAtPos(source.getLevel(), blockPos, biomeLocation);
        });
        source.sendSuccess(new TranslatableComponent("command.electrona.biome_set", biomeLocation), true);
        return source.hashCode();
    }
}
