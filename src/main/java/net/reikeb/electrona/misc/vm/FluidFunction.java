package net.reikeb.electrona.misc.vm;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluids;
import net.minecraft.tags.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.*;

import java.util.concurrent.atomic.AtomicInteger;

public class FluidFunction {

    /**
     * This method is used by fluid generators to transfer fluids to Machines.
     *
     * @param world             The world of the blocks
     * @param pos               The blockpos of the generator
     * @param directions        The directions of the generator
     * @param generatorTE       The TileEntity of the generator
     * @param generatorLevel    The current fluid amount of the generator
     * @param transferPerSecond The amount of fluid transfered per second
     * @param isGenerator       Defines if the method is used by a generator or a storage
     */
    public static void generatorTransferFluid(World world, BlockPos pos, Direction[] directions, TileEntity generatorTE, int generatorLevel, int transferPerSecond, Boolean isGenerator) {
        double transferPerTick = transferPerSecond * 0.05;

        ITagCollection<Block> tagCollection = BlockTags.getAllTags();
        ITag<Block> machineTag, cableTag;
        machineTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", (isGenerator ? "electrona/has_water_tank" : "electrona/has_water_tank")));
        cableTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", (isGenerator ? "electrona/fluid_cable" : "electrona/fluid_cable")));

        for (Direction dir : directions) {
            if (generatorLevel <= 0) return; // we have no more fluid

            TileEntity tileEntity = world.getBlockEntity(pos.relative(dir));
            if (tileEntity == null) continue;
            Block offsetBlock = world.getBlockState(pos.relative(dir)).getBlock();
            if (!(machineTag.contains(offsetBlock) || cableTag.contains(offsetBlock))) continue;

            AtomicInteger machineLevel = new AtomicInteger();
            tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).ifPresent(cap -> machineLevel.set(cap.getFluidInTank(1).getAmount()));
            AtomicInteger machineCapacity = new AtomicInteger();
            tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).ifPresent(cap -> machineCapacity.set(cap.getTankCapacity(1)));

            double headroom = machineCapacity.get() - machineLevel.get();
            double actualTransfer = Math.min(Math.min(transferPerTick, generatorLevel), headroom);

            drainWater(generatorTE, (int) actualTransfer);
            fillWater(tileEntity, (int) actualTransfer);
        }
    }

    /**
     * Small method to drain water from a TileEntity
     *
     * @param te     The TileEntity we drain water from
     * @param amount The amount of water drained
     */
    public static void drainWater(TileEntity te, int amount) {
        te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                .ifPresent(cap -> cap.drain(amount, IFluidHandler.FluidAction.EXECUTE));
    }

    /**
     * Small method to fill water into a TileEntity
     *
     * @param te     The TileEntity we give water to
     * @param amount The amount of water given
     */
    public static void fillWater(TileEntity te, int amount) {
        te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                .ifPresent(cap -> cap.fill(new FluidStack(Fluids.WATER, amount), IFluidHandler.FluidAction.EXECUTE));
    }
}
