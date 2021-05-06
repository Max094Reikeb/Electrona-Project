package net.reikeb.electrona.items;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import net.reikeb.electrona.misc.vm.CosmicGemFunction;
import net.reikeb.electrona.setup.ItemGroups;
import net.reikeb.electrona.utils.GemPower;

import java.util.List;

public class CosmicGem extends Item {

    public CosmicGem() {
        super(new Properties()
                .stacksTo(1)
                .rarity(Rarity.EPIC)
                .tab(ItemGroups.ELECTRONA_ITEMS));
    }

    @Override
    public void appendHoverText(ItemStack itemstack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        String power = (itemstack).getOrCreateTag().getString("power");
        TranslationTextComponent text = new TranslationTextComponent("power.electrona." + power);
        if (!text.equals(new TranslationTextComponent("power.electrona."))) {
            list.add(text);
        }
        if (itemstack.getOrCreateTag().getBoolean("dimensionTravel")) {
            String dim = itemstack.getOrCreateTag().getString("dimension");
            TranslationTextComponent dimension = new TranslationTextComponent("power.electrona." + power + "_" + dim);
        }
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

    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        boolean flag = CosmicGemFunction.use(world, player, stack);
        if (flag) player.getCooldowns().addCooldown(this, GemPower.byCooldown(stack));
        return ActionResult.sidedSuccess(stack, world.isClientSide());
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        ActionResultType action = super.onItemUseFirst(stack, context);
        World world = context.getLevel();
        BlockState state = world.getBlockState(context.getClickedPos());
        PlayerEntity entity = context.getPlayer();

        if (entity == null) return ActionResultType.FAIL;

        boolean flag = CosmicGemFunction.useOn(world, state, entity, stack);
        if (flag) entity.getCooldowns().addCooldown(this, GemPower.byCooldown(stack));
        return action;
    }
}
