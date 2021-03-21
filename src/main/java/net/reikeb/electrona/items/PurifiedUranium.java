package net.reikeb.electrona.items;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.world.World;

import net.reikeb.electrona.setup.ItemGroups;
import net.reikeb.electrona.utils.ElectronaUtils;

public class PurifiedUranium extends Item {

    public PurifiedUranium() {
        super(new Properties()
                .stacksTo(64)
                .rarity(Rarity.COMMON)
                .tab(ItemGroups.ELECTRONA_ITEMS));
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
        ElectronaUtils.radioactiveItemInInventory(itemstack, world, entity, slot, selected);
    }
}
