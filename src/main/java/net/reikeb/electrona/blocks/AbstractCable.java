package net.reikeb.electrona.blocks;

import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.*;

public abstract class AbstractCable extends Block implements SimpleWaterloggedBlock {

    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final EnumProperty<MiddleState> MIDDLE = EnumProperty.create("middle", MiddleState.class);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty[] CONNECTIONS = new BooleanProperty[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};

    public final VoxelShape S_NORTH;
    public final VoxelShape S_SOUTH;
    public final VoxelShape S_WEST;
    public final VoxelShape S_EAST;
    public final VoxelShape S_UP;
    public final VoxelShape S_MIDDLE;

    public final VoxelShape[] SHAPES;

    public AbstractCable(String name, Material material, float hardness, float resistance, SoundType sound, int size) {
        super(Properties.of(material)
                .sound(sound)
                .strength(hardness, resistance));
        int halfSize = size / 2;
        this.S_NORTH = Block.box(8 - halfSize, 0, 0, 8 + halfSize, halfSize * 2, 8 - halfSize);
        this.S_SOUTH = Block.box(8 - halfSize, 0, 8 + halfSize, 8 + halfSize, halfSize * 2, 16);
        this.S_WEST = Block.box(0, 0, 8 - halfSize, 8 - halfSize, halfSize * 2, 8 + halfSize);
        this.S_EAST = Block.box(8 + halfSize, 0, 8 - halfSize, 16, halfSize * 2, 8 + halfSize);
        this.S_UP = Block.box(8 - halfSize, size, 8 - halfSize, 8 + halfSize, 16, 8 + halfSize);
        this.S_MIDDLE = Block.box(8 - halfSize, 0, 8 - halfSize, 8 + halfSize, size, 8 + halfSize);
        this.SHAPES = new VoxelShape[]{Shapes.empty(), S_UP, S_NORTH, S_SOUTH, S_WEST, S_EAST};
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.defaultFluidState() : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN, MIDDLE, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = updateState(this.defaultBlockState(), context.getLevel(), context.getClickedPos());

        BlockState replaceState = context.getLevel().getBlockState(context.getClickedPos());
        return state.setValue(WATERLOGGED, replaceState.getBlock() == Blocks.WATER);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        BlockState newState = updateState(state, worldIn, pos);
        if (!newState.equals(state)) worldIn.setBlockAndUpdate(pos, newState);
    }

    public BlockState updateState(BlockState state, Level worldIn, BlockPos pos) {
        for (Direction side : Direction.values()) {
            BooleanProperty prop = CONNECTIONS[side.get3DDataValue()];
            boolean mustConnect = canConnectTo(state, worldIn, pos, pos.relative(side), side.getOpposite());
            boolean isConnected = state.getValue(prop);
            if (mustConnect != isConnected) state = state.setValue(prop, mustConnect);
        }

        int connections = countConnections(state);
        MiddleState mState = MiddleState.CLOSE;
        if (connections == 1 && !state.getValue(DOWN)) {
            mState = MiddleState.NONE;
        } else if (connections == 1 || connections == 0) {
            mState = MiddleState.OPEN;
        }
        state = state.setValue(MIDDLE, mState);

        return state;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        VoxelShape shape = state.getValue(MIDDLE) != MiddleState.NONE ? S_MIDDLE : Shapes.empty();
        for (Direction direction : Direction.values()) {
            if (state.getValue(CONNECTIONS[direction.get3DDataValue()])) {
                shape = Shapes.or(shape, SHAPES[direction.get3DDataValue()]);
            }
        }
        return shape;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public boolean hasOpenEnd(BlockState state) {
        return countConnections(state) <= 1;
    }

    public int countConnections(BlockState state) {
        int connections = 0;
        for (BooleanProperty side : CONNECTIONS) {
            if (state.getValue(side)) connections++;
        }
        return connections;
    }

    public abstract boolean canConnectTo(BlockState wireState, Level worldIn, BlockPos wirePos, BlockPos connectPos, Direction direction);

    public static enum MiddleState implements StringRepresentable {
        CLOSE("close"), OPEN("open"), NONE("none");
        private String name;

        private MiddleState(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
