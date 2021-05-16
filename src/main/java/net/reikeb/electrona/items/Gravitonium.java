package net.reikeb.electrona.items;

import net.minecraft.block.BlockState;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;

import net.reikeb.electrona.setup.ItemGroups;
import net.reikeb.electrona.utils.ElectronaUtils;

public class Gravitonium extends Item {

    public Gravitonium() {
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

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        ActionResultType action = super.onItemUseFirst(stack, context);
        ElectronaUtils.Gravity.applyGravity(context.getLevel(), context.getClickedPos());
        return action;
    }
}
