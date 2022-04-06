package net.reikeb.electrona.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.reikeb.electrona.blockentities.SingularityBlockEntity;
import net.reikeb.electrona.init.BlockEntityInit;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.misc.Tags;
import net.reikeb.electrona.world.Gamerules;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class Singularity extends AbstractWaterLoggableBlock implements EntityBlock {

    public Singularity() {
        super(Properties.of(Material.STONE)
                .sound(SoundType.STONE)
                .strength(-1, 3600000)
                .lightLevel(s -> 0)
                .noCollission()
                .noOcclusion()
                .isRedstoneConductor((bs, br, bp) -> false));
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(WATERLOGGED, false));
    }

    public static void singularitySpawn(Level world, BlockPos pos) {
        if (world.isClientSide) return;

        if (world.getLevelData().getGameRules().getBoolean(Gamerules.DO_BLACK_HOLES_EXIST)) {

            for (BlockPos testPos : BlockPos.spiralAround(pos, 1000, Direction.EAST, Direction.SOUTH)) {
                if (!Tags.STOPS_BLACK_HOLE.contains(world.getBlockState(testPos).getBlock())) {
                    world.setBlockAndUpdate(testPos, BlockInit.HOLE.get().defaultBlockState());
                    return;
                }
            }
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 0));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Vec3 offset = state.getOffset(world, pos);
        return Shapes.or(box(1, 1, 1, 15, 15, 15)).move(offset.x, offset.y, offset.z);
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState replaceState = context.getLevel().getBlockState(context.getClickedPos());
        return this.defaultBlockState()
                .setValue(WATERLOGGED, replaceState.getBlock() == Blocks.WATER);
    }

    @Override
    public MaterialColor defaultMaterialColor() {
        return MaterialColor.NONE;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.IGNORE;
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moving) {
        singularitySpawn(world, pos);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack itemstack) {
        singularitySpawn(world, pos);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SingularityBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> blockEntityType) {
        return blockEntityType == BlockEntityInit.SINGULARITY_BLOCK_ENTITY.get() ? (BlockEntityTicker<T>) SingularityBlockEntity.TICKER : null;
    }
}
