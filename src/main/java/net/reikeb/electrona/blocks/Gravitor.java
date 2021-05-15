package net.reikeb.electrona.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.*;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.*;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.*;

import net.reikeb.electrona.tileentities.TileGravitor;

import javax.annotation.Nullable;

public class Gravitor extends ContainerBlock implements IWaterLoggable {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape SHAPE = Block.box(5.0D, 5.0D, 5.0D, 11.0D, 11.0D, 11.0D);

    public Gravitor() {
        super(Properties.of(Material.HEAVY_METAL)
                .sound(SoundType.METAL)
                .strength(4f, 6f)
                .lightLevel(s -> 15)
                .noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
    }

    @Override
    public boolean isPathfindable(BlockState state, IBlockReader blockReader, BlockPos pos, PathType pathType) {
        return false;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state2, IWorld world, BlockPos pos, BlockPos pos2) {
        if (state.getValue(WATERLOGGED)) {
            world.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        return super.updateShape(state, direction, state2, world, pos, pos2);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader blockReader, BlockPos pos, ISelectionContext selectionContext) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            TileEntity tileentity = world.getBlockEntity(pos);
            if (tileentity instanceof BeaconTileEntity) {
                ((BeaconTileEntity) tileentity).setCustomName(stack.getHoverName());
            }
        }

    }

    @Override
    public TileEntity newBlockEntity(IBlockReader blockReader) {
        return new TileGravitor();
    }
}
