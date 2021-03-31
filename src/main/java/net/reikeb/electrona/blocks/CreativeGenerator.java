package net.reikeb.electrona.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.*;
import net.minecraft.world.IBlockReader;

import net.minecraftforge.api.distmarker.*;

import net.reikeb.electrona.tileentities.TileCreativeGenerator;

import javax.annotation.Nullable;
import java.util.*;

public class CreativeGenerator extends Block {

    public CreativeGenerator() {
        super(Properties.of(Material.DECORATION)
                .sound(SoundType.METAL)
                .strength(-1, 6f)
                .lightLevel(s -> 0)
                .hasPostProcess((bs, br, bp) -> true)
                .emissiveRendering((bs, br, bp) -> true));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemstack, IBlockReader world, List<ITextComponent> list, ITooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(new TranslationTextComponent("block.electrona.creative_generator.desc1"));
        list.add(new TranslationTextComponent("block.electrona.creative_generator.desc2"));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(Blocks.AIR, 1));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileCreativeGenerator();
    }
}
