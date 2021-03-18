package net.reikeb.electrona.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.world.World;

import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.setup.ItemGroups;
import net.reikeb.electrona.utils.ElectronaUtils;

public class SteelSword extends SwordItem {

    public SteelSword() {
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
                return Ingredient.of(new ItemStack(ItemInit.STEEL_INGOT.get(), 1), new ItemStack(ItemInit.STEEL_SWORD.get(), 1));
            }
        }, 3, -2.4f, new Item.Properties().tab(ItemGroups.ELECTRONA_TOOLS));
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, World world, PlayerEntity entity) {
        ElectronaUtils.steelToolCraftedAdvancement((ServerPlayerEntity) entity);
    }
}
