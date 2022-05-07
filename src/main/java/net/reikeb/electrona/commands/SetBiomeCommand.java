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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import net.reikeb.maxilib.utils.BiomeUtil;

public class SetBiomeCommand {

    private static final DynamicCommandExceptionType ERROR_INVALID_BIOME = new DynamicCommandExceptionType((error)
            -> new TranslatableComponent("commands.locatebiome.invalid", error));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("setbiome").requires((commandSource) ->
                        commandSource.hasPermission(2)).then(Commands.argument("location", Vec3Argument.vec3())
                        .then(Commands.argument("biome", ResourceOrTagLocationArgument.resourceOrTag(Registry.BIOME_REGISTRY)).executes((command) ->
                                setBiomeAtPos(command.getSource(), Vec3Argument.getVec3(command, "location"),
                                        command.getArgument("biome", ResourceLocation.class)))))
                .then(Commands.argument("biome", ResourceOrTagLocationArgument.resourceOrTag(Registry.BIOME_REGISTRY)).executes((command) ->
                        setBiomeAtPos(command.getSource(), command.getSource().getPosition(), command.getArgument("biome", ResourceLocation.class)))));
    }

    private static int setBiomeAtPos(CommandSourceStack source, Vec3 coordinates, ResourceLocation resourceLocation) throws CommandSyntaxException {
        Biome biome = source.getServer().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getOptional(resourceLocation).orElseThrow(()
                -> ERROR_INVALID_BIOME.create(source));
        for (int j = 0; j < 50; ++j) {
            for (int k = 0; k < 35; ++k) {
                for (int l = 0; l < 45; ++l) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d0 = (float) j / 15.0F * 2.0F - 1.0F;
                        double d1 = (float) k / 15.0F * 2.0F - 1.0F;
                        double d2 = (float) l / 15.0F * 2.0F - 1.0F;
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 = d0 / d3;
                        d1 = d1 / d3;
                        d2 = d2 / d3;
                        float f = 15 * (0.7F + source.getLevel().random.nextFloat() * 0.6F);
                        double d4 = coordinates.x;
                        double d6 = coordinates.y;
                        double d8 = coordinates.z;

                        for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                            BlockPos blockpos = new BlockPos(d4, d6, d8);
                            BiomeUtil.setBiomeAtPos(source.getLevel(), blockpos, resourceLocation);
                            d4 += d0 * (double) 0.3F;
                            d6 += d1 * (double) 0.3F;
                            d8 += d2 * (double) 0.3F;
                        }
                    }
                }
            }
        }
        return source.hashCode();
    }
}
