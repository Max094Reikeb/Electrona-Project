package net.reikeb.electrona.items;

import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;

import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.setup.ItemGroups;

public class SteelHoe extends HoeItem {

    public SteelHoe() {
        super(new IItemTier() {
            @Override
            public int getUses() {
                return 604;
            }

            @Override
            public float getSpeed() {
                return 10f;
            }

            @Override
            public float getAttackDamageBonus() {
                return -1f;
            }

            @Override
            public int getLevel() {
                return 4;
            }

            @Override
            public int getEnchantmentValue() {
                return 22;
            }

            @Override
            public Ingredient getRepairIngredient() {
                return Ingredient.of(new ItemStack(ItemInit.STEEL_INGOT.get(), 1), new ItemStack(ItemInit.STEEL_HOE.get(), 1));
            }
        }, 0, 0f, new Item.Properties().tab(ItemGroups.ELECTRONA_TOOLS));
    }
}
