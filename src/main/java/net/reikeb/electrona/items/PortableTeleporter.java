package net.reikeb.electrona.items;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import net.reikeb.electrona.misc.vm.TeleporterFunction;
import net.reikeb.electrona.setup.ItemGroups;

import java.util.List;

public class PortableTeleporter extends Item {

    public PortableTeleporter() {
        super(new Properties()
                .stacksTo(1)
                .tab(ItemGroups.ELECTRONA_ITEMS));
    }

    @Override
    public void appendHoverText(ItemStack itemstack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        int EL = 0;
        super.appendHoverText(itemstack, world, list, flag);
        list.add(new TranslationTextComponent("item.electrona.portable_teleporter.desc1"));
        list.add(new TranslationTextComponent("item.electrona.portable_teleporter.desc2",
                (itemstack).getOrCreateTag().getDouble("teleportX"),
                (itemstack).getOrCreateTag().getDouble("teleportY"),
                (itemstack).getOrCreateTag().getDouble("teleportZ")).withStyle(TextFormatting.GRAY));
        EL = (itemstack).getOrCreateTag().getInt("ElectronicPower");
        list.add(new StringTextComponent(EL + " EL").withStyle(TextFormatting.GRAY));
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public int getUseDuration(ItemStack itemstack) {
        return 0;
    }

    @Override
    public float getDestroySpeed(ItemStack par1ItemStack, BlockState par2Block) {
        return 1F;
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        return TeleporterFunction.teleportPortable(worldIn, playerIn, handIn)
                ? ActionResult.success(stack) : ActionResult.fail(stack);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        ActionResultType action = super.onItemUseFirst(stack, context);
        World world = context.getLevel();
        PlayerEntity entity = context.getPlayer();
        Hand hand = context.getHand();

        if (entity == null) return ActionResultType.FAIL;

        return TeleporterFunction.teleportPortable(world, entity, hand) ? action : action;
    }
}
