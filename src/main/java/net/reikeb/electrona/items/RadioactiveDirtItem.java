package net.reikeb.electrona.items;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.*;
import net.minecraft.world.World;

import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.misc.vm.RadioactivityFunction;
import net.reikeb.electrona.setup.ItemGroups;

public class RadioactiveDirtItem extends BlockItem {

    public RadioactiveDirtItem() {
        super(BlockInit.RADIOACTIVE_DIRT.get(), new Properties()
                .stacksTo(64).rarity(Rarity.COMMON).tab(ItemGroups.ELECTRONA_BLOCKS));
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public int getUseDuration(ItemStack itemstack) {
        return 0;
    }

    @Override
    public float getDestroySpeed(ItemStack par1ItemStack, BlockState par2Block) {
        return 1F;
    }

    @Override
    public void inventoryTick(ItemStack itemstack, World world, Entity entity, int slot, boolean selected) {
        RadioactivityFunction.radioactiveItemInInventory(world, entity, 200, 1);
    }
}
