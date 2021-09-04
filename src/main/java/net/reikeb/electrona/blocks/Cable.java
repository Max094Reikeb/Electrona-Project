package net.reikeb.electrona.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootContext;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.reikeb.electrona.misc.DamageSources;
import net.reikeb.electrona.tileentities.TileCable;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class Cable extends AbstractCable implements EntityBlock {

    public Cable() {
        super("cable", Material.CLOTH_DECORATION, 1f, 6f, SoundType.WOOL, 4);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemstack, BlockGetter world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(new TranslatableComponent("block.electrona.cable.desc"));
    }

    @Override
    public boolean canConnectTo(BlockState wireState, Level worldIn, BlockPos wirePos, BlockPos connectPos, Direction direction) {
        BlockState otherState = worldIn.getBlockState(connectPos);

        TagCollection<Block> tagCollection = BlockTags.getAllTags();
        Tag<Block> generatorTag, machineTag, cableTag;
        generatorTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", "electrona/generators"));
        machineTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", "electrona/machines_all"));
        cableTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", "electrona/cable"));

        return (generatorTag.contains(otherState.getBlock())) || (machineTag.contains(otherState.getBlock()))
                || (cableTag.contains(otherState.getBlock()));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
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
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        BlockState stateIn = worldIn.getBlockState(pos);
        BlockEntity tile = worldIn.getBlockEntity(pos);
        if (tile instanceof TileCable) {
            TileCable tileCable = (TileCable) tile;
            if ((hasOpenEnd(stateIn)) && (entityIn instanceof LivingEntity) && (tileCable.getTileData().getDouble("ElectronicPower") > 0)) {
                double damage = Math.random() * 10;
                if (damage > 0) entityIn.hurt(DamageSources.ELECTRIC_SHOCK.bypassArmor(), (float) damage);
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileCable(pos, state);
    }
}
