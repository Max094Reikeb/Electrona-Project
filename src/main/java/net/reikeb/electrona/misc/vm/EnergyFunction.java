package net.reikeb.electrona.misc.vm;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.reikeb.electrona.misc.Tags;
import net.reikeb.maxilib.intface.EnergyInterface;

public class EnergyFunction {

    /**
     * This method is used by Generators and Batteries to transfer
     * energy from them to Batteries or Machines
     *
     * @param level                The level of the blocks
     * @param pos                  The blockpos of the Generator/Machine
     * @param generatorBlockEntity The BlockEntity of the Generator/Machine
     * @param transferPerSecond    THe amount of energy transfered each second
     * @param isGenerator          Defines if the method is used by a Generator or a Machine
     */
    public static <T extends EnergyInterface> void generatorTransferEnergy(Level level, BlockPos pos, T generatorBlockEntity, int transferPerSecond, Boolean isGenerator) {
        double transferPerTick = transferPerSecond * 0.05;
        double generatorPower = generatorBlockEntity.getEnergy();

        TagKey<Block> machineTag = isGenerator ? Tags.MACHINES : Tags.BLUE_MACHINES;
        TagKey<Block> cableTag = isGenerator ? Tags.CABLE : Tags.BLUE_CABLE;

        for (Direction dir : Direction.values()) {
            if (generatorPower <= 0) return; // we have no more power

            BlockEntity blockEntity = level.getBlockEntity(pos.relative(dir));
            BlockState offsetBlockState = level.getBlockState(pos.relative(dir));
            if (!(blockEntity instanceof EnergyInterface energyBlockEntity)) continue;
            if (!(offsetBlockState.is(machineTag) || offsetBlockState.is(cableTag))) continue;

            double machinePower = energyBlockEntity.getEnergy();
            int machineMax = energyBlockEntity.getMaxEnergy();
            double headroom = machineMax - machinePower;
            double actualTransfer = Math.min(Math.min(transferPerTick, generatorPower), headroom);

            EnergyInterface.drainEnergy(generatorBlockEntity, actualTransfer);
            EnergyInterface.fillEnergy(energyBlockEntity, actualTransfer);
        }
    }

    /**
     * This methods setup an energy transfer between Machines that have slots
     * and items that can hold energy
     *
     * @param generatorBlockEntity The BlockEntity of the Generator/Machine
     * @param fromGenerator        Defines if the energy is given by the Generator/Machine or the item
     * @param slot                 The slot the item must be in
     * @param transferPerSecond    The amount of energy transfered each second
     */
    public static <T extends EnergyInterface> void transferEnergyWithItemSlot(T generatorBlockEntity, Boolean fromGenerator, int slot, double transferPerSecond) {
        double transferPerTick = transferPerSecond * 0.05;
        ItemStack stackInSlot = generatorBlockEntity.getItemInventory().getStackInSlot(slot);
        double electronicPower = generatorBlockEntity.getEnergy();

        if (fromGenerator && (electronicPower <= 0)) return; // we have no more power
        if (!stackInSlot.is(Tags.POWERED_ITEMS)) return; // the itemstack is not the one required

        double itemPower = stackInSlot.getOrCreateTag().getDouble("ElectronicPower");
        double actualTransfer = Math.min(transferPerTick, (fromGenerator ? electronicPower : itemPower));

        if (fromGenerator) {
            EnergyInterface.drainEnergy(generatorBlockEntity, actualTransfer);
        } else {
            EnergyInterface.fillEnergy(generatorBlockEntity, actualTransfer);
        }
        stackInSlot.getOrCreateTag().putDouble("ElectronicPower", (fromGenerator ? (itemPower + actualTransfer) : (itemPower - actualTransfer)));
    }
}
