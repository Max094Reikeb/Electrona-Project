package net.reikeb.electrona.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.setup.ItemGroups;

public class TinPlate extends Item {

    public TinPlate() {
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
}
