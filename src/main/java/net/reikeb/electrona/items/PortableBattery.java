package net.reikeb.electrona.items;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.reikeb.electrona.setup.ItemGroups;

import java.util.List;

public class PortableBattery extends Item {

    public PortableBattery() {
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
        int EL = 0;
        super.appendHoverText(itemstack, world, list, flag);
        EL = (itemstack).getOrCreateTag().getInt("ElectronicPower");
        list.add(new TextComponent((("\u00A77") + "" + ((EL)) + "" + (" EL"))));
    }
}
