package net.reikeb.electrona.items;

import net.minecraft.block.BlockState;
import net.minecraft.item.*;

import net.reikeb.electrona.setup.ItemGroups;

public class GravityDevice extends Item {

    public GravityDevice() {
        super(new Properties()
                .stacksTo(64)
                .rarity(Rarity.RARE)
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
}
