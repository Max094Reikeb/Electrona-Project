package net.reikeb.electrona.items;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

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
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        String power = (itemstack).getOrCreateTag().getString("power");
        TranslatableComponent text = new TranslatableComponent("power.electrona." + power);
        if (!text.equals(new TranslatableComponent("power.electrona."))) {
            list.add(text);
        }
        if (itemstack.getOrCreateTag().getBoolean("dimensionTravel")) {
            String dim = itemstack.getOrCreateTag().getString("dimension");
            TranslatableComponent dimension = new TranslatableComponent("power.electrona." + power + "_" + dim);
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

    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        boolean flag = CosmicGemFunction.use(world, player, stack);
        if (flag) player.getCooldowns().addCooldown(this, GemPower.byCooldown(stack));
        return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        InteractionResult action = super.onItemUseFirst(stack, context);
        Level world = context.getLevel();
        BlockState state = world.getBlockState(context.getClickedPos());
        Player entity = context.getPlayer();

        if (entity == null) return InteractionResult.FAIL;

        boolean flag = CosmicGemFunction.useOn(world, state, entity, stack);
        if (flag) entity.getCooldowns().addCooldown(this, GemPower.byCooldown(stack));
        return action;
    }
}
