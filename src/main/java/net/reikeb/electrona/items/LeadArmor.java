package net.reikeb.electrona.items;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.setup.ItemGroups;

public class LeadArmor extends ArmorItem {

    public static final LeadMaterial leadMaterial = new LeadMaterial();

    public LeadArmor(EquipmentSlotType slot) {
        super(leadMaterial, slot, new Properties().tab(ItemGroups.ELECTRONA_TOOLS));
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return Electrona.MODID + ":textures/models/armor/lead_armor_layer_" + (slot == EquipmentSlotType.LEGS ? "2" : "1") + ".png";
    }

    public static class LeadMaterial implements IArmorMaterial {

        @Override
        public int getDurabilityForSlot(EquipmentSlotType slot) {
            return new int[]{13, 15, 16, 11}[slot.getIndex()] * 25;
        }

        @Override
        public int getDefenseForSlot(EquipmentSlotType slot) {
            return new int[]{2, 5, 6, 2}[slot.getIndex()];
        }

        @Override
        public int getEnchantmentValue() {
            return 13;
        }

        @Override
        public net.minecraft.util.SoundEvent getEquipSound() {
            return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(""));
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
    };
}
