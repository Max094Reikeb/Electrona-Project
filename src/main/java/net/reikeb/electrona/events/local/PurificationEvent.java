package net.reikeb.electrona.events.local;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * PurificationEvent triggers when a purification is done with the Purificator.<br>
 * <br>
 * This event is {@link Cancelable}.<br>
 * This event does not use {@link HasResult}.<br>
 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.<br>
 */
@Cancelable
public class PurificationEvent extends Event {

    private final Level world;
    private final BlockPos pos;
    private final ItemStack input;
    private final ItemStack output;
    private final int purifyingTime;
    private final int waterRequired;

    public PurificationEvent(Level world, BlockPos pos, ItemStack input, ItemStack output, int purifyingTime,
                             int waterRequired) {
        this.world = world;
        this.pos = pos;
        this.input = input;
        this.output = output;
        this.purifyingTime = purifyingTime;
        this.waterRequired = waterRequired;
    }

    public Level getWorld() {
        return this.world;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public ItemStack getInput() {
        return this.input;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    public int getPurifyingTime() {
        return purifyingTime;
    }

    public int getWaterRequired() {
        return waterRequired;
    }
}
