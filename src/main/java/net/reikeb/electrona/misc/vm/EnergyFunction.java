package net.reikeb.electrona.misc.vm;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollection;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.reikeb.electrona.misc.Keys;
import net.reikeb.electrona.utils.ItemHandler;

public class EnergyFunction {

    /**
     * This method is used by Generators and Batteries to transfer
     * energy from them to Batteries or Machines
     *
     * @param world             The world of the blocks
     * @param pos               The blockpos of the Generator/Machine
     * @param directions        The directions of the Generator/Machine
     * @param generatorNBT      The NBT Tag of the Generator/Machine
     * @param transferPerSecond THe amount of energy transfered each second
     * @param generatorPower    The amount of energy the Generator/Machine has
     * @param isGenerator       Defines if the method is used by a Generator or a Machine
     */
    public static void generatorTransferEnergy(Level world, BlockPos pos, Direction[] directions, CompoundTag generatorNBT, int transferPerSecond, double generatorPower, Boolean isGenerator) {
        double transferPerTick = transferPerSecond * 0.05;

        TagCollection<Block> tagCollection = BlockTags.getAllTags();
        Tag<Block> machineTag, cableTag;
        machineTag = tagCollection.getTagOrEmpty((isGenerator ? Keys.CABLE_MACHINE_TAG : Keys.BLUE_CABLE_MACHINE_TAG));
        cableTag = tagCollection.getTagOrEmpty((isGenerator ? Keys.CABLE_TAG : Keys.BLUE_CABLE_TAG));

        for (Direction dir : directions) {
            if (generatorPower <= 0) return; // we have no more power

            BlockEntity blockEntity = world.getBlockEntity(pos.relative(dir));
            Block offsetBlock = world.getBlockState(pos.relative(dir)).getBlock();
            if (blockEntity == null) continue;
            if (!(machineTag.contains(offsetBlock) || cableTag.contains(offsetBlock))) continue;

            double machinePower = blockEntity.getTileData().getDouble("ElectronicPower");
            int machineMax = blockEntity.getTileData().getInt("MaxStorage");
            double headroom = machineMax - machinePower;
            double actualTransfer = Math.min(Math.min(transferPerTick, generatorPower), headroom);

            generatorNBT.putDouble("ElectronicPower", (generatorPower -= actualTransfer));
            blockEntity.getTileData().putDouble("ElectronicPower", (machinePower + actualTransfer));
        }
    }

    /**
     * This methods setup an energy transfer between Machines that have slots
     * and items that can hold energy
     *
     * @param generatorNBT      The NBT Tag of the Generator/Machine
     * @param requiredItem      The item that is checked to be in the slot
     * @param inventory         The inventory of the Generator/Machine's TE
     * @param fromGenerator     Defines if the energy is given by the Generator/Machine or the item
     * @param generatorPower    The power of the Generator/Machine
     * @param slot              The slot the item must be in
     * @param transferPerSecond The amount of energy transfered each second
     */
    public static void transferEnergyWithItemSlot(CompoundTag generatorNBT, Item requiredItem, ItemHandler inventory, Boolean fromGenerator, double generatorPower, int slot, double transferPerSecond) {
        double transferPerTick = transferPerSecond * 0.05;
        ItemStack stackInSlot = inventory.getStackInSlot(slot);

        if (fromGenerator && (generatorPower <= 0)) return; // we have no more power
        if (requiredItem != stackInSlot.getItem()) return; // the itemstack is not the one required

        double itemPower = stackInSlot.getOrCreateTag().getDouble("ElectronicPower");
        double actualTransfer = Math.min(transferPerTick, (fromGenerator ? generatorPower : itemPower));

        generatorNBT.putDouble("ElectronicPower", (fromGenerator ? (generatorPower - actualTransfer) : (generatorPower + actualTransfer)));
        stackInSlot.getOrCreateTag().putDouble("ElectronicPower", (fromGenerator ? (itemPower + actualTransfer) : (itemPower - actualTransfer)));
    }
}
