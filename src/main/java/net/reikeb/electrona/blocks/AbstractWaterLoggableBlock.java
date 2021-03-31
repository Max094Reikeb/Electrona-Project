package net.reikeb.electrona.blocks;

import net.minecraft.block.*;
import net.minecraft.fluid.*;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.*;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;

public abstract class AbstractWaterLoggableBlock extends Block implements IWaterLoggable {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public AbstractWaterLoggableBlock(AbstractBlock.Properties properties) {
        super(properties);
    }

    /**
     * Handles ticking the water if waterlogged. Call "super.neighborChanged()" first.
     */
    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!world.isClientSide)
            if (state.getValue(WATERLOGGED))
                world.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
    }

    /**
     * Handles the "waterlogged" state for you. Substitute "this.getDefaultState().with(...)" with "super.getStateForPlacement().with(...)"
     */
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    /**
     * Gets the "waterlogged" state for you. Use "super.getFluidState(state).with(...)"
     */
    @Override
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    /**
     * Handles ticking the water if waterlogged. Call "super.updatePostPlacement()" first.
     */
    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED))
            world.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));

        return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
    }

    /**
     * Handles ticking the water if waterlogged. Call "super.onBlockAdded()" first.
     */
    @Override
    public void onPlace(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        world.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
    }

    /**
     * Adds the "waterlogged" state. Call "super.fillStateContainer()" first.
     */
    @Override
    public void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }
}
