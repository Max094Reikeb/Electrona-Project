package net.reikeb.electrona.items;

import net.minecraft.block.DispenserBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import net.reikeb.electrona.setup.ItemGroups;

import javax.annotation.Nullable;
import java.util.List;

public class MechanicWings extends Item {

    public MechanicWings() {
        super(new Item.Properties()
                .durability(500)
                .rarity(Rarity.COMMON)
                .tab(ItemGroups.ELECTRONA_TOOLS)
        );
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public void appendHoverText(ItemStack itemstack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        int EL = 0;
        super.appendHoverText(itemstack, world, list, flag);
        EL = (itemstack).getOrCreateTag().getInt("ElectronicPower");
        list.add(new StringTextComponent((("\u00A77") + "" + ((EL)) + "" + (" EL"))));
    }

    @Nullable
    @Override
    public EquipmentSlotType getEquipmentSlot(ItemStack stack) {
        return EquipmentSlotType.CHEST; // Or you could just extend ItemArmor
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        EquipmentSlotType equipmentslottype = MobEntity.getEquipmentSlotForItem(itemstack);
        ItemStack itemstack1 = player.getItemBySlot(equipmentslottype);
        if (itemstack1.isEmpty()) {
            player.setItemSlot(equipmentslottype, itemstack.copy());
            itemstack.setCount(0);
            return ActionResult.sidedSuccess(itemstack, world.isClientSide());
        } else {
            return ActionResult.fail(itemstack);
        }
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity) {
        return armorType == EquipmentSlotType.CHEST;
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return ((stack.getDamageValue() < stack.getMaxDamage() - 1));
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        if (!entity.level.isClientSide && (flightTicks + 1) % 40 == 0) {
            stack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(net.minecraft.inventory.EquipmentSlotType.CHEST));
        }
        return true;
    }
}
