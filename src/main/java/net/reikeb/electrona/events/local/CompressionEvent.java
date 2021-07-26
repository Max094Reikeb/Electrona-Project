package net.reikeb.electrona.events.local;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * CompressionEvent triggers when a compression is done with the Compressor.<br>
 * <br>
 * This event is {@link Cancelable}.<br>
 * This event does not use {@link HasResult}.<br>
 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.<br>
 */
@Cancelable
public class CompressionEvent extends Event {

    private final Level world;
    private final BlockPos pos;
    private final ItemStack input1;
    private final ItemStack input2;
    private final ItemStack output;
    private final int compressingTime;
    private final int energyRequired;

    public CompressionEvent(Level world, BlockPos pos, ItemStack input1, ItemStack input2, ItemStack output,
                            int compressingTime, int energyRequired) {
        this.world = world;
        this.pos = pos;
        this.input1 = input1;
        this.input2 = input2;
        this.output = output;
        this.compressingTime = compressingTime;
        this.energyRequired = energyRequired;
    }

    public Level getWorld() {
        return this.world;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public ItemStack getInput1() {
        return this.input1;
    }

    public ItemStack getInput2() {
        return this.input2;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    public int getCompressingTime() {
        return this.compressingTime;
    }

    public int getEnergyRequired() {
        return this.energyRequired;
    }
}
