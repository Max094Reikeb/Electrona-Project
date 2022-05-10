package net.reikeb.electrona.items;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.reikeb.electrona.gempower.GemObject;
import net.reikeb.electrona.gempower.PowerUtils;
import net.reikeb.electrona.gempower.UsePower;
import net.reikeb.electrona.init.GemInit;
import net.reikeb.electrona.setup.ItemGroups;

import java.util.List;

public class CosmicGem extends Item {

    public CosmicGem() {
        super(new Properties()
                .stacksTo(1)
                .rarity(Rarity.EPIC)
                .tab(ItemGroups.ELECTRONA_ITEMS));
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

    public ItemStack getDefaultInstance() {
        return PowerUtils.setGem(super.getDefaultInstance(), GemInit.EMPTY.get());
    }

    public void appendHoverText(ItemStack itemstack, Level level, List<Component> list, TooltipFlag flag) {
        PowerUtils.addGemTooltip(itemstack, list);
    }

    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> stacks) {
        if (this.allowdedIn(group)) {
            for (GemObject gemObject : GemInit.GEM_REGISTRY.get()) {
                stacks.add(PowerUtils.setGem(new ItemStack(this), gemObject));
            }
        }
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        new UsePower(level, player, stack);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        InteractionResult action = super.onItemUseFirst(stack, context);
        Level level = context.getLevel();
        BlockState state = level.getBlockState(context.getClickedPos());
        Player player = context.getPlayer();

        if (player == null) return InteractionResult.FAIL;

        new UsePower.UseOn(level, player, state, stack);
        return action;
    }
}
