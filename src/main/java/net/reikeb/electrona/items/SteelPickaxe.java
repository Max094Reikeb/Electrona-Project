package net.reikeb.electrona.items;

import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;

import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.setup.ItemGroups;

public class SteelPickaxe extends PickaxeItem {

    public SteelPickaxe() {
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
                return 3.5f;
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
                return Ingredient.of(new ItemStack(ItemInit.STEEL_INGOT.get(), 1), new ItemStack(ItemInit.STEEL_PICKAXE.get(), 1));
            }
        }, 1, -2.8f, new Item.Properties().tab(ItemGroups.ELECTRONA_TOOLS));
    }
}
