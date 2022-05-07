package net.reikeb.electrona.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.reikeb.electrona.init.ItemInit;

public class Veinminer extends Enchantment {

    public Veinminer(EquipmentSlot... slots) {
        super(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.DIGGER, slots);
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
        if (ench == Enchantments.BLOCK_EFFICIENCY)
            return true;
        if (ench == Enchantments.UNBREAKING)
            return true;
        if (ench == Enchantments.MENDING)
            return true;
        return ench == Enchantments.VANISHING_CURSE;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.getItem() == ItemInit.STEEL_PICKAXE.get();
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
