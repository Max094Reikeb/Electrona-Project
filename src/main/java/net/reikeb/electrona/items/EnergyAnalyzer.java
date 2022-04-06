package net.reikeb.electrona.items;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.blockentities.AbstractEnergyBlockEntity;
import net.reikeb.electrona.setup.ItemGroups;

import java.util.List;

public class EnergyAnalyzer extends Item {

    public EnergyAnalyzer() {
        super(new Properties()
                .stacksTo(1)
                .rarity(Rarity.COMMON)
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

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(new TranslatableComponent("item.electrona.energy_analyzer.desc"));
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        InteractionResult action = super.onItemUseFirst(stack, context);
        Level world = context.getLevel();
        Player entity = context.getPlayer();

        if (entity == null) return InteractionResult.FAIL;

        BlockEntity blockEntity = world.getBlockEntity(context.getClickedPos());
        double electronicPower = (blockEntity instanceof AbstractEnergyBlockEntity energyBlockEntity) ? energyBlockEntity.getElectronicPower() : 0;

        if (!world.isClientSide) {
            entity.displayClientMessage(new TranslatableComponent("message.electrona.energy_analyzed", electronicPower), true);
        }
        return action;
    }
}
