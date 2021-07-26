package net.reikeb.electrona.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.minecraftforge.common.ToolType;

import net.reikeb.electrona.misc.vm.CustomShapes;
import net.reikeb.electrona.tileentities.TileEnergeticLightningRod;
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
                .harvestLevel(1)
                .harvestTool(ToolType.PICKAXE)
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

    VoxelShape upShape = CustomShapes.EnergeticLightningRodUp;
    VoxelShape downShape = CustomShapes.EnergeticLightningRodDown;
    VoxelShape northShape = CustomShapes.EnergeticLightningRod;
    VoxelShape westShape = ElectronaUtils.rotateShape(Direction.NORTH, Direction.WEST, northShape);
    VoxelShape eastShape = ElectronaUtils.rotateShape(Direction.NORTH, Direction.EAST, northShape);
    VoxelShape southShape = ElectronaUtils.rotateShape(Direction.NORTH, Direction.SOUTH, northShape);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        if (facing == Direction.DOWN) {
            return downShape;
        } else if (facing == Direction.NORTH) {
            return northShape;
        } else if (facing == Direction.SOUTH) {
            return southShape;
        } else if (facing == Direction.EAST) {
            return eastShape;
        } else if (facing == Direction.WEST) {
            return westShape;
        }
        return upShape;
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
        return new TileEnergeticLightningRod();
    }
}
