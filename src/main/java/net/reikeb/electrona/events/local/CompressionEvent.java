package net.reikeb.electrona.events.local;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.*;

/**
 * CompressionEvent triggers when a compression is done with the Compressor.<br>
 * <br>
 * This event is {@link Cancelable}.<br>
 * This event does not use {@link HasResult}.<br>
 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.<br>
 */
@Cancelable
public class CompressionEvent extends Event {

    private final World world;
    private final BlockPos pos;
    private final ItemStack input1;
    private final ItemStack input2;
    private final ItemStack output;
    private final int compressingTime;
    private final int energyRequired;

    public CompressionEvent(World world, BlockPos pos, ItemStack input1, ItemStack input2, ItemStack output,
                            int compressingTime, int energyRequired) {
        this.world = world;
        this.pos = pos;
        this.input1 = input1;
        this.input2 = input2;
        this.output = output;
        this.compressingTime = compressingTime;
        this.energyRequired = energyRequired;
    }

    public World getWorld() {
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
