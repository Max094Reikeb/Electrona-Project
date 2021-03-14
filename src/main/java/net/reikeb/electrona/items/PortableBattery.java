package net.reikeb.electrona.items;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
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
    public void appendHoverText(ItemStack itemstack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        int EL = 0;
        super.appendHoverText(itemstack, world, list, flag);
        EL = (itemstack).getOrCreateTag().getInt("ElectronicPower");
        list.add(new StringTextComponent((("\u00A77") + "" + ((EL)) + "" + (" EL"))));
    }
}
