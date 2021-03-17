package net.reikeb.electrona.utils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollection;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.items.ItemStackHandler;

public class ElectronaUtils {

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
    public static void generatorTransferEnergy(World world, BlockPos pos, Direction[] directions, CompoundNBT generatorNBT, int transferPerSecond, double generatorPower, Boolean isGenerator) {
        double transferPerTick = transferPerSecond * 0.05;

        ITagCollection<Block> tagCollection = BlockTags.getAllTags();
        ITag<Block> machineTag, cableTag;
        machineTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", (isGenerator ? "electrona/machines_all" : "electrona/machines")));
        cableTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", (isGenerator ? "electrona/cable" : "elctrona/blue_cable")));

        for (Direction dir : directions) {
            if (generatorPower <= 0) return; // we have no more power

            TileEntity tileEntity = world.getBlockEntity(pos.relative(dir));
            if (tileEntity == null) continue;
            Block offsetBlock = world.getBlockState(pos.relative(dir)).getBlock();
            if (!(machineTag.contains(offsetBlock) || cableTag.contains(offsetBlock))) continue;

            double machinePower = tileEntity.getTileData().getDouble("ElectronicPower");
            int machineMax = tileEntity.getTileData().getInt("MaxStorage");
            double headroom = machineMax - machinePower;
            double actualTransfer = Math.min(Math.min(transferPerTick, generatorPower), headroom);

            generatorNBT.putDouble("ElectronicPower", (generatorPower -= actualTransfer));
            tileEntity.getTileData().putDouble("ElectronicPower", (machinePower + actualTransfer));
        }
    }

    /**
     * This methods setup an energy transfer between Machines that have slots
     * and items that can hold energy
     *
     * @param generatorNBT      The NBT Tag of the Generator/Machines
     * @param requiredItem      The item that is check to be in the slot
     * @param stackHandler      The slothandler given by the TE of the Generator/Machines
     * @param fromGenerator     Defines if the energy is given by the Generator/Machines or the Item
     * @param generatorPower    THe power of the Generator/Machines
     * @param slot              The Slot the Item must be in
     * @param transferPerSecond The amount of energy transfered each second
     */
    public static void transferEnergyWithItemSlot(CompoundNBT generatorNBT, Item requiredItem, ItemStackHandler stackHandler, Boolean fromGenerator, double generatorPower, int slot, double transferPerSecond) {
        double transferPerTick = transferPerSecond * 0.05;
        ItemStack stackInSlot = stackHandler.getStackInSlot(slot);

        if (fromGenerator && (generatorPower <= 0)) return; // we have no more power
        if (requiredItem != stackInSlot.getItem()) return; // the itemstack is not the one required

        double itemPower = stackInSlot.getOrCreateTag().getDouble("ElectronicPower");
        double actualTransfer = Math.min(transferPerTick, (fromGenerator ? generatorPower : itemPower));

        generatorNBT.putDouble("ElectronicPower", (fromGenerator ? (generatorPower - actualTransfer) : (generatorPower + actualTransfer)));
        stackInSlot.getOrCreateTag().putDouble("ElectronicPower", (fromGenerator ? (itemPower + actualTransfer) : (itemPower - actualTransfer)));
    }
}
