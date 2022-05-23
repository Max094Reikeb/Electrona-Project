package net.reikeb.electrona.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.reikeb.electrona.blockentities.BlueCableBlockEntity;
import net.reikeb.electrona.init.BlockEntityInit;
import net.reikeb.electrona.misc.DamageSources;
import net.reikeb.electrona.misc.Tags;
import net.reikeb.maxilib.abs.AbstractCable;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class BlueCable extends AbstractCable implements EntityBlock {

    public BlueCable() {
        super("cable", Material.CLOTH_DECORATION, 1f, 6f, SoundType.WOOL, 4);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemstack, BlockGetter blockGetter, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, blockGetter, list, flag);
        list.add(new TranslatableComponent("block.electrona.blue_cable.desc"));
    }

    @Override
    public boolean canConnectTo(BlockState wireState, Level level, BlockPos wirePos, BlockPos connectPos, Direction direction) {
        BlockState otherState = level.getBlockState(connectPos);
        return (otherState.is(Tags.GENERATORS)) || (otherState.is(Tags.MACHINES)) || (otherState.is(Tags.BLUE_CABLE));
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
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        BlockState stateIn = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BlueCableBlockEntity blueCableBlockEntity) {
            if ((hasOpenEnd(stateIn)) && (entity instanceof LivingEntity) && (blueCableBlockEntity.getEnergy() > 0)) {
                double damage = Math.random() * 10;
                if (damage > 0) entity.hurt(DamageSources.ELECTRIC_SHOCK.bypassArmor(), (float) damage);
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlueCableBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return blockEntityType == BlockEntityInit.BLUE_CABLE_BLOCK_ENTITY.get() ? (BlockEntityTicker<T>) BlueCableBlockEntity.TICKER : null;
    }
}
