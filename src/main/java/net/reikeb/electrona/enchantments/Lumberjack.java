package net.reikeb.electrona.enchantments;

import net.minecraft.enchantment.*;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

import net.reikeb.electrona.init.*;

public class Lumberjack extends Enchantment {

    public Lumberjack(EquipmentSlotType... slots) {
        super(Enchantment.Rarity.UNCOMMON, EnchantmentType.DIGGER, slots);
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    protected boolean checkCompatibility(Enchantment ench) {
        if (ench == Enchantments.BINDING_CURSE)
            return true;
        if (ench == Enchantments.SHARPNESS)
            return true;
        if (ench == Enchantments.BLOCK_EFFICIENCY)
            return true;
        if (ench == Enchantments.UNBREAKING)
            return true;
        if (ench == Enchantments.MENDING)
            return true;
        if (ench == Enchantments.VANISHING_CURSE)
            return true;
        return ench == EnchantmentInit.THUNDERING.get();
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.getItem() == ItemInit.STEEL_AXE.get();
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public boolean isCurse() {
        return false;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }
}
