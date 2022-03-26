package fr.firstmegagame4.electrona.toolmaterials;

import fr.firstmegagame4.electrona.Items;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class SteelToolMaterial implements ToolMaterial {

    public static final SteelToolMaterial INSTANCE = new SteelToolMaterial();

    @Override
    public int getDurability() {
        return 604;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 10F;
    }

    @Override
    public float getAttackDamage() {
        return 3.5F;
    }

    @Override
    public int getMiningLevel() {
        return 4;
    }

    @Override
    public int getEnchantability() {
        return 22;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(Items.STEEL_INGOT);
    }

}
