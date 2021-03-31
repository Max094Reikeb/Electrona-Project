package net.reikeb.electrona.items;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import net.minecraftforge.api.distmarker.*;

import net.reikeb.electrona.setup.ItemGroups;

import java.util.List;

public class TeleportSaver extends Item {

    public TeleportSaver() {
        super(new Properties()
                .stacksTo(1)
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
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(ItemStack itemstack) {
        return (itemstack).getOrCreateTag().getBoolean("linked");
    }

    @Override
    public void appendHoverText(ItemStack itemstack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        if ((itemstack).getOrCreateTag().getBoolean("linked")) {
            list.add(new TranslationTextComponent("item.electrona.teleport_saver.desc"));
            list.add(new StringTextComponent("\u00A77" + (itemstack).getOrCreateTag().getDouble("teleportX") + " "
                    + (itemstack).getOrCreateTag().getDouble("teleportY") + " " + (itemstack).getOrCreateTag().getDouble("teleportZ")));
        }
    }
}
