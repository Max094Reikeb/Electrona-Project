package net.reikeb.electrona.items;

import net.minecraft.block.BlockState;
import net.minecraft.item.*;

import net.minecraftforge.api.distmarker.*;

import net.reikeb.electrona.setup.ItemGroups;

public class AdvancedTotemOfUndying extends Item {

    public AdvancedTotemOfUndying() {
        super(new Properties()
                .stacksTo(1)
                .rarity(Rarity.UNCOMMON)
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
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(ItemStack itemstack) {
        return true;
    }
}
