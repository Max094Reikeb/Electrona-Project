package net.reikeb.electrona.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.reikeb.electrona.blockentities.EnergeticLightningRodBlockEntity;
import net.reikeb.electrona.init.BlockEntityInit;
import net.reikeb.electrona.misc.vm.CustomShapes;
import net.reikeb.electrona.utils.ElectronaUtils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class EnergeticLightningRod extends AbstractWaterLoggableBlock implements EntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public EnergeticLightningRod() {
        super(Properties.of(Material.HEAVY_METAL)
                .sound(SoundType.CHAIN)
                .strength(3f, 6f)
                .noOcclusion());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.UP)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        if (facing == Direction.DOWN) {
            return CustomShapes.EnergeticLightningRodDown;
        } else if (facing == Direction.NORTH) {
            return CustomShapes.EnergeticLightningRod;
        } else if (facing == Direction.SOUTH) {
            return ElectronaUtils.rotateShape(Direction.NORTH, Direction.SOUTH, CustomShapes.EnergeticLightningRod);
        } else if (facing == Direction.EAST) {
            return ElectronaUtils.rotateShape(Direction.NORTH, Direction.EAST, CustomShapes.EnergeticLightningRod);
        } else if (facing == Direction.WEST) {
            return ElectronaUtils.rotateShape(Direction.NORTH, Direction.WEST, CustomShapes.EnergeticLightningRod);
        }
        return CustomShapes.EnergeticLightningRodUp;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.setValue(FACING, mirrorIn.mirror(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        BlockState blockState = context.getLevel().getBlockState(context.getClickedPos().relative(direction.getOpposite()));
        BlockState replaceState = context.getLevel().getBlockState(context.getClickedPos());
        return blockState.is(this) &&
                blockState.getValue(FACING) == direction ? this.defaultBlockState().setValue(FACING, direction.getOpposite())
                .setValue(WATERLOGGED, replaceState.getBlock() == Blocks.WATER) : this.defaultBlockState().setValue(FACING, direction)
                .setValue(WATERLOGGED, replaceState.getBlock() == Blocks.WATER);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnergeticLightningRodBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> blockEntityType) {
        return blockEntityType == BlockEntityInit.ENERGETIC_LIGHTNING_ROD_BLOCK_ENTITY.get() ? (BlockEntityTicker<T>) EnergeticLightningRodBlockEntity.TICKER : null;
    }
}
