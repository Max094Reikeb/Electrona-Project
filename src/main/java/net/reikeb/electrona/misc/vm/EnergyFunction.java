package net.reikeb.electrona.misc.vm;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.reikeb.electrona.blockentities.AbstractEnergyBlockEntity;
import net.reikeb.electrona.misc.Tags;

public class EnergyFunction {

    /**
     * This method is used by Generators and Batteries to transfer
     * energy from them to Batteries or Machines
     *
     * @param world                The world of the blocks
     * @param pos                  The blockpos of the Generator/Machine
     * @param directions           The directions of the Generator/Machine
     * @param generatorBlockEntity The BlockEntity of the Generator/Machine
     * @param transferPerSecond    THe amount of energy transfered each second
     * @param isGenerator          Defines if the method is used by a Generator or a Machine
     */
    public static <T extends AbstractEnergyBlockEntity> void generatorTransferEnergy(Level world, BlockPos pos, Direction[] directions, T generatorBlockEntity, int transferPerSecond, Boolean isGenerator) {
        double transferPerTick = transferPerSecond * 0.05;
        double generatorPower = getEnergy(generatorBlockEntity);

        Tag<Block> machineTag = isGenerator ? Tags.MACHINES : Tags.BLUE_MACHINES;
        Tag<Block> cableTag = isGenerator ? Tags.CABLE : Tags.BLUE_CABLE;

        for (Direction dir : directions) {
            if (generatorPower <= 0) return; // we have no more power

            BlockEntity blockEntity = world.getBlockEntity(pos.relative(dir));
            Block offsetBlock = world.getBlockState(pos.relative(dir)).getBlock();
            if (!(blockEntity instanceof AbstractEnergyBlockEntity energyBlockEntity)) continue;
            if (!(machineTag.contains(offsetBlock) || cableTag.contains(offsetBlock))) continue;

            double machinePower = getEnergy(energyBlockEntity);
            int machineMax = getMaxEnergy(energyBlockEntity);
            double headroom = machineMax - machinePower;
            double actualTransfer = Math.min(Math.min(transferPerTick, generatorPower), headroom);

            drainEnergy(generatorBlockEntity, actualTransfer);
            fillEnergy(energyBlockEntity, actualTransfer);
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
    public static void transferEnergyWithItemSlot(AbstractEnergyBlockEntity generatorBlockEntity, Boolean fromGenerator, int slot, double transferPerSecond) {
        double transferPerTick = transferPerSecond * 0.05;
        ItemStack stackInSlot = generatorBlockEntity.getItemInventory().getStackInSlot(slot);
        double electronicPower = getEnergy(generatorBlockEntity);

        if (fromGenerator && (electronicPower <= 0)) return; // we have no more power
        if (!Tags.POWERED_ITEMS.contains(stackInSlot.getItem())) return; // the itemstack is not the one required

        double itemPower = stackInSlot.getOrCreateTag().getDouble("ElectronicPower");
        double actualTransfer = Math.min(transferPerTick, (fromGenerator ? electronicPower : itemPower));

        if (fromGenerator) {
            drainEnergy(generatorBlockEntity, actualTransfer);
        } else {
            fillEnergy(generatorBlockEntity, actualTransfer);
        }
        stackInSlot.getOrCreateTag().putDouble("ElectronicPower", (fromGenerator ? (itemPower + actualTransfer) : (itemPower - actualTransfer)));
    }

    /**
     * Small method to drain energy from a BlockEntity
     *
     * @param be     The BlockEntity we drain energy from
     * @param amount The amount of energy drained
     */
    public static void drainEnergy(AbstractEnergyBlockEntity be, double amount) {
        be.setElectronicPower(be.getElectronicPower() - amount);
    }

    /**
     * Small method to fill energy into a BlockEntity
     *
     * @param be     The BlockEntity we give energy to
     * @param amount The amount of energy given
     */
    public static void fillEnergy(AbstractEnergyBlockEntity be, double amount) {
        be.setElectronicPower(be.getElectronicPower() + amount);
    }

    /**
     * Small method to get the energy of a block in a BlockEntity
     *
     * @param be The BlockEntity to check
     * @return The amount of energy
     */
    public static double getEnergy(AbstractEnergyBlockEntity be) {
        return be.getElectronicPower();
    }

    /**
     * Small method to set the energy of a BlockEntity
     *
     * @param be     The BlockEntity we set energy to
     * @param amount The amount of energy we set
     */
    public static void setEnergy(AbstractEnergyBlockEntity be, double amount) {
        be.setElectronicPower(amount);
    }

    /**
     * Small method to get the capacity of a BlockEntity
     *
     * @param be The BlockEntity to check
     * @return The capacity
     */
    public static int getMaxEnergy(AbstractEnergyBlockEntity be) {
        return be.getMaxStorage();
    }
}
