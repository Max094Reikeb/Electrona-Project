package net.reikeb.electrona.items;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.setup.ItemGroups;

public class LeadArmor extends ArmorItem {

    public static final LeadMaterial leadMaterial = new LeadMaterial();

    public LeadArmor(EquipmentSlot slot) {
        super(leadMaterial, slot, new Properties().tab(ItemGroups.ELECTRONA_TOOLS));
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return Electrona.MODID + ":textures/models/armor/lead_armor_layer_" + (slot == EquipmentSlot.LEGS ? "2" : "1") + ".png";
    }

    public static class LeadMaterial implements ArmorMaterial {

        @Override
        public int getDurabilityForSlot(EquipmentSlot slot) {
            return new int[]{13, 15, 16, 11}[slot.getIndex()] * 25;
        }

        @Override
        public int getDefenseForSlot(EquipmentSlot slot) {
            return new int[]{2, 5, 6, 2}[slot.getIndex()];
        }

        @Override
        public int getEnchantmentValue() {
            return 13;
        }

        @Override
        public net.minecraft.sounds.SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_IRON;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(ItemInit.LEAD_INGOT.get());
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public String getName() {
            return "lead";
        }

        @Override
        public float getToughness() {
            return 1f;
        }

        @Override
        public float getKnockbackResistance() {
            return 0f;
        }
    }

    ;
}
