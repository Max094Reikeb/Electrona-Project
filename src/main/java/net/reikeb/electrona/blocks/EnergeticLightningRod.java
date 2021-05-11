package net.reikeb.electrona.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.*;
import net.minecraft.loot.LootContext;
import net.minecraft.state.*;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.IBlockReader;

import net.minecraftforge.common.ToolType;

import net.reikeb.electrona.misc.vm.CustomShapes;
import net.reikeb.electrona.tileentities.TileEnergeticLightningRod;
import net.reikeb.electrona.utils.ElectronaUtils;

import javax.annotation.Nullable;
import java.util.*;

public class EnergeticLightningRod extends AbstractWaterLoggableBlock {

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
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
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
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }

    @Override
    public void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.setValue(FACING, mirrorIn.mirror(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction direction = context.getClickedFace();
        BlockState blockState = context.getLevel().getBlockState(context.getClickedPos().relative(direction.getOpposite()));
        BlockState replaceState = context.getLevel().getBlockState(context.getClickedPos());
        return blockState.is(this) &&
                blockState.getValue(FACING) == direction ? this.defaultBlockState().setValue(FACING, direction.getOpposite())
                .setValue(WATERLOGGED, replaceState.getBlock() == Blocks.WATER) : this.defaultBlockState().setValue(FACING, direction)
                .setValue(WATERLOGGED, replaceState.getBlock() == Blocks.WATER);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEnergeticLightningRod();
    }
}
