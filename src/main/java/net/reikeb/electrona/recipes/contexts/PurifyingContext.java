package net.reikeb.electrona.recipes.contexts;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

import net.minecraftforge.items.IItemHandlerModifiable;

import net.reikeb.electrona.utils.ItemHandlerWrapper;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public class PurifyingContext extends ItemHandlerWrapper {

    protected final PlayerEntity player;
    protected final Random random;

    public PurifyingContext(IItemHandlerModifiable inner, @Nullable PlayerEntity player, @Nullable Supplier<Vector3d> location, @Nullable Random random) {
        super(inner, location, 64);
        this.player = player;
        this.random = random;
    }

    @Nullable
    public PlayerEntity getPlayer() {
        return player;
    }

    @Nullable
    public Random getRandom() {
        return random;
    }
}
