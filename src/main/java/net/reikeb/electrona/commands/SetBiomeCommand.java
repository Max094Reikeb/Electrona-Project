package net.reikeb.electrona.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceOrTagLocationArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import net.reikeb.maxilib.utils.BiomeUtil;

public class SetBiomeCommand {

    private static final DynamicCommandExceptionType ERROR_BIOME_NOT_FOUND = new DynamicCommandExceptionType((error)
            -> new TranslatableComponent("commands.locatebiome.notFound", error));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("setbiome").requires((commandSource)
                        -> commandSource.hasPermission(2))
                .then(Commands.argument("location", Vec3Argument.vec3())
                        .then(Commands.argument("biome", ResourceOrTagLocationArgument.resourceOrTag(Registry.BIOME_REGISTRY))
                                .executes((command) -> setBiomeAtPos(command.getSource(), Vec3Argument.getVec3(command, "location"),
                                        ResourceOrTagLocationArgument.getBiome(command, "biome")))))
                .then(Commands.argument("biome", ResourceOrTagLocationArgument.resourceOrTag(Registry.BIOME_REGISTRY))
                        .executes((command) -> setBiomeAtPos(command.getSource(), command.getSource().getPosition(),
                                ResourceOrTagLocationArgument.getBiome(command, "biome")))));
    }

    private static int setBiomeAtPos(CommandSourceStack source, Vec3 coordinates, ResourceOrTagLocationArgument.Result<Biome> biomeResult) throws CommandSyntaxException {
        biomeResult.unwrap().left().orElseThrow(() -> ERROR_BIOME_NOT_FOUND.create(source));
        if (biomeResult.unwrap().left().isEmpty()) return 0;
        ResourceKey<Biome> biomeKey = biomeResult.unwrap().left().get();
        BlockPos pos = new BlockPos((int) coordinates.x, (int) coordinates.y, (int) coordinates.z);
        BlockPos.betweenClosed(pos.getX() - 10, pos.getY() - 10, pos.getZ() - 10, pos.getX() + 10, pos.getY() + 10, pos.getZ() + 10).forEach(blockPos -> {
            BiomeUtil.setBiomeKeyAtPos(source.getLevel(), blockPos, biomeKey);
        });
        return source.hashCode();
    }
}
