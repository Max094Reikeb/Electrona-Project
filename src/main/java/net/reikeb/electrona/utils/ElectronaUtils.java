package net.reikeb.electrona.utils;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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
        cableTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", (isGenerator ? "electrona/cable" : "electrona/blue_cable")));

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
     * @param generatorNBT      The NBT Tag of the Generator/Machine
     * @param requiredItem      The item that is checked to be in the slot
     * @param inventory         The inventory of the Generator/Machine's TE
     * @param fromGenerator     Defines if the energy is given by the Generator/Machine or the item
     * @param generatorPower    The power of the Generator/Machine
     * @param slot              The slot the item must be in
     * @param transferPerSecond The amount of energy transfered each second
     */
    public static void transferEnergyWithItemSlot(CompoundNBT generatorNBT, Item requiredItem, ItemHandler inventory, Boolean fromGenerator, double generatorPower, int slot, double transferPerSecond) {
        double transferPerTick = transferPerSecond * 0.05;
        ItemStack stackInSlot = inventory.getStackInSlot(slot);

        if (fromGenerator && (generatorPower <= 0)) return; // we have no more power
        if (requiredItem != stackInSlot.getItem()) return; // the itemstack is not the one required

        double itemPower = stackInSlot.getOrCreateTag().getDouble("ElectronicPower");
        double actualTransfer = Math.min(transferPerTick, (fromGenerator ? generatorPower : itemPower));

        generatorNBT.putDouble("ElectronicPower", (fromGenerator ? (generatorPower - actualTransfer) : (generatorPower + actualTransfer)));
        stackInSlot.getOrCreateTag().putDouble("ElectronicPower", (fromGenerator ? (itemPower + actualTransfer) : (itemPower - actualTransfer)));
    }

    /**
     * This method is used by Cables to transfer energy to Machines or other cables
     *
     * @param world             The world the blocks are in
     * @param pos               The blockpos of the Cable
     * @param directions        The directions of the Cable
     * @param cableNBT          The NBT Tag of the Cable
     * @param transferPerSecond The amount of energy transfered each second
     * @param isBlue            Defines if the Cable is a normal Cable or a Blue Cable
     */
    public static void cableTransferEnergy(World world, BlockPos pos, Direction[] directions, CompoundNBT cableNBT, double cablePower, boolean cableLogic, int transferPerSecond, Boolean isBlue) {
        double transferPerTick = transferPerSecond * 0.05;

        ITagCollection<Block> tagCollection = BlockTags.getAllTags();
        ITag<Block> machineTag, cableTag;
        machineTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", (isBlue ? "electrona/machines" : "electrona/machines_all")));
        cableTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", (isBlue ? "electrona/blue_cable" : "electrona/cable")));

        for (Direction dir : directions) {
            if (cablePower > 0) {
                TileEntity tileEntity = world.getBlockEntity(pos.relative(dir));
                if (tileEntity == null) continue;
                Block offsetBlock = world.getBlockState(pos.relative(dir)).getBlock();
                if (!(machineTag.contains(offsetBlock) || cableTag.contains(offsetBlock))) continue;

                double machinePower = tileEntity.getTileData().getDouble("ElectronicPower");
                int machineMax = tileEntity.getTileData().getInt("MaxStorage");
                boolean machineLogic = tileEntity.getTileData().getBoolean("logic");

                if (machineTag.contains(offsetBlock)) {
                    if (machinePower < (machineMax - cablePower)) {
                        tileEntity.getTileData().putDouble("ElectronicPower", (machinePower + transferPerTick));
                        cableNBT.putDouble("ElectronicPower", (cablePower - transferPerTick));
                    } else {
                        tileEntity.getTileData().putDouble("ElectronicPower", machineMax);
                    }
                } else if (cableTag.contains(offsetBlock)) {
                    if ((!machineLogic) && ((transferPerTick + machinePower) < machineMax)) {
                        tileEntity.getTileData().putDouble("ElectronicPower", (machinePower + transferPerTick));
                        cableNBT.putDouble("ElectronicPower", (cablePower - transferPerTick));
                        if (cableNBT.getDouble("ElectronicPower") > 0) {
                            cableNBT.putBoolean("logic", false);
                        } else {
                            cableNBT.putBoolean("logic", true);
                        }
                        tileEntity.getTileData().putBoolean("logic", false);
                    }
                }
            } else {
                cableNBT.putBoolean("logic", false);
                return; // we have no more power
            }
        }
    }

    /**
     * Method to get if the entity given is wearing the whole Anti-Radiation Suit
     *
     * @param entity   The entity we're checking
     * @return boolean If the entity is wearing the full suit
     */
    public static boolean isEntityWearingAntiRadiationSuit(LivingEntity entity) {
        return true;
    }

    /**
     * Method to get if the entity given is wearing the whole Lead Armor
     *
     * @param entity   The entity we're checking
     * @return boolean If the entity is wearing the full armor
     */
    public static boolean isEntityWearingLeadArmor(LivingEntity entity) {
        return true;
    }

    /**
     * Applies Radioactivity effect to the entity if it has radioactive item in his inventory
     *
     * @param itemstack The radioactive item
     * @param world     The world the entity is in
     * @param entity    The entity who has the item
     * @param slot      The slot the item is in
     * @param selected  If the item is in the entity's hand
     */
    public static void radioactiveItemInInventory(ItemStack itemstack, World world, Entity entity, int slot, boolean selected) {
    }
}
