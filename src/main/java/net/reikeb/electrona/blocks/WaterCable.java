package net.reikeb.electrona.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.tags.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.*;

import net.minecraftforge.api.distmarker.*;

import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.tileentities.TileWaterCable;

import javax.annotation.Nullable;
import java.util.*;

public class WaterCable extends AbstractCable {

    public WaterCable() {
        super("water_cable", Material.CLOTH_DECORATION, 1f, 6f, SoundType.WOOL, 4);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemstack, IBlockReader world, List<ITextComponent> list, ITooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(new TranslationTextComponent("block.electrona.water_cable.desc"));
    }

    @Override
    public boolean canConnectTo(BlockState wireState, World worldIn, BlockPos wirePos, BlockPos connectPos, Direction direction) {
        BlockState otherState = worldIn.getBlockState(connectPos);

        ITagCollection<Block> tagCollection = BlockTags.getAllTags();
        ITag<Block> machineTag, cableTag;
        machineTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", "electrona/has_water_tank"));
        cableTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", "electrona/water_cable"));

        return (otherState.getBlock() == BlockInit.WATER_PUMP.get()) || (machineTag.contains(otherState.getBlock()))
                || (cableTag.contains(otherState.getBlock()));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.IGNORE;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileWaterCable();
    }
}
