package net.reikeb.electrona.items;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
    public void appendHoverText(ItemStack itemstack, Level level, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, level, list, flag);
        if ((itemstack).getOrCreateTag().getBoolean("linked")) {
            list.add(new TranslatableComponent("item.electrona.teleport_saver.desc"));
            list.add(new TextComponent("\u00A77" + (itemstack).getOrCreateTag().getDouble("teleportX") + " "
                    + (itemstack).getOrCreateTag().getDouble("teleportY") + " " + (itemstack).getOrCreateTag().getDouble("teleportZ")));
        }
    }
}
