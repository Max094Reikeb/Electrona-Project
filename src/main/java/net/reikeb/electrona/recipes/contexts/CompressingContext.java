package net.reikeb.electrona.recipes.contexts;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import net.minecraftforge.items.IItemHandlerModifiable;

import net.reikeb.electrona.utils.ItemHandlerWrapper;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public class CompressingContext extends ItemHandlerWrapper {

    protected final Player player;
    protected final Random random;

    public CompressingContext(IItemHandlerModifiable inner, @Nullable Player player, @Nullable Supplier<Vec3> location, @Nullable Random random) {
        super(inner, location, 64);
        this.player = player;
        this.random = random;
    }

    @Nullable
    public Player getPlayer() {
        return player;
    }

    @Nullable
    public Random getRandom() {
        return random;
    }
}