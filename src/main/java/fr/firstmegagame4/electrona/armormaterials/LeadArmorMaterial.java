package fr.firstmegagame4.electrona.armormaterials;

import fr.firstmegagame4.electrona.Items;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class LeadArmorMaterial implements ArmorMaterial {

    public static final ArmorMaterial INSTANCE = new LeadArmorMaterial();

    @Override
    public int getDurability(EquipmentSlot slot) {
        return new int[] {13, 15, 16, 11} [slot.getEntitySlotId()] * 25;
    }

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        return new int[] {2, 5, 6, 2} [slot.getEntitySlotId()];
    }

    @Override
    public int getEnchantability() {
        return 13;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_IRON;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(Items.LEAD_INGOT.getIfCreated());
    }

    @Override
    public String getName() {
        return "lead_armor";
    }

    @Override
    public float getToughness() {
        return 1.0F;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.0F;
    }

}
