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
import net.reikeb.electrona.setup.ItemGroups;

public class AntiRadiationSuit extends ArmorItem {

    public static final AntiRadiationMaterial antiRadiationMaterial = new AntiRadiationMaterial();

    public AntiRadiationSuit(EquipmentSlot slot) {
        super(antiRadiationMaterial, slot, new Properties().tab(ItemGroups.ELECTRONA_TOOLS));
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return Electrona.MODID + ":textures/models/armor/anti_radiation_armor_layer_" + (slot == EquipmentSlot.LEGS ? "2" : "1") + ".png";
    }

    public static class AntiRadiationMaterial implements ArmorMaterial {

        @Override
        public int getDurabilityForSlot(EquipmentSlot slot) {
            return new int[]{13, 15, 16, 11}[slot.getIndex()] * 4;
        }

        @Override
        public int getDefenseForSlot(EquipmentSlot slot) {
            return new int[]{1, 1, 1, 1}[slot.getIndex()];
        }

        @Override
        public int getEnchantmentValue() {
            return 9;
        }

        @Override
        public net.minecraft.sounds.SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_LEATHER;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.EMPTY;
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public String getName() {
            return "anti_radiation";
        }

        @Override
        public float getToughness() {
            return 0f;
        }

        @Override
        public float getKnockbackResistance() {
            return 0f;
        }
    }
}
