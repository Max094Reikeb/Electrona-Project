package net.reikeb.electrona.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
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
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        int EL = 0;
        super.appendHoverText(itemstack, world, list, flag);
        list.add(new TranslatableComponent("item.electrona.portable_teleporter.desc1"));
        list.add(new TranslatableComponent("item.electrona.portable_teleporter.desc2",
                (itemstack).getOrCreateTag().getDouble("teleportX"),
                (itemstack).getOrCreateTag().getDouble("teleportY"),
                (itemstack).getOrCreateTag().getDouble("teleportZ")).withStyle(ChatFormatting.GRAY));
        EL = (itemstack).getOrCreateTag().getInt("ElectronicPower");
        list.add(new TextComponent(EL + " EL").withStyle(ChatFormatting.GRAY));
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
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        return TeleporterFunction.teleportPortable(worldIn, playerIn, handIn)
                ? InteractionResultHolder.success(stack) : InteractionResultHolder.fail(stack);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        InteractionResult action = super.onItemUseFirst(stack, context);
        Level world = context.getLevel();
        Player entity = context.getPlayer();
        InteractionHand hand = context.getHand();

        if (entity == null) return InteractionResult.FAIL;

        return TeleporterFunction.teleportPortable(world, entity, hand) ? action : action;
    }
}
