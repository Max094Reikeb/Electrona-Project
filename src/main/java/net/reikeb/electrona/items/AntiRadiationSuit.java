package net.reikeb.electrona.items;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.registries.ForgeRegistries;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.setup.ItemGroups;

public class AntiRadiationSuit extends ArmorItem {

    public static final AntiRadiationMaterial antiRadiationMaterial = new AntiRadiationMaterial();

    public AntiRadiationSuit(EquipmentSlotType slot) {
        super(antiRadiationMaterial, slot, new Properties().tab(ItemGroups.ELECTRONA_TOOLS));
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return Electrona.MODID + ":textures/models/armor/anti_radiation_armor_layer_" + (slot == EquipmentSlotType.LEGS ? "2" : "1") + ".png";
    }

    public static class AntiRadiationMaterial implements IArmorMaterial {

        @Override
        public int getDurabilityForSlot(EquipmentSlotType slot) {
            return new int[]{13, 15, 16, 11}[slot.getIndex()] * 4;
        }

        @Override
        public int getDefenseForSlot(EquipmentSlotType slot) {
            return new int[]{1, 1, 1, 1}[slot.getIndex()];
        }

        @Override
        public int getEnchantmentValue() {
            return 9;
        }

        @Override
        public net.minecraft.util.SoundEvent getEquipSound() {
            return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(""));
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
