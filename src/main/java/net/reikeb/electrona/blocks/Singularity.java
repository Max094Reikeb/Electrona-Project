package net.reikeb.electrona.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.loot.LootContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;

import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.tileentities.TileSingularity;
import net.reikeb.electrona.world.Gamerules;

import javax.annotation.Nullable;
import java.util.*;

public class Singularity extends AbstractWaterLoggableBlock {

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

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 0));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        Vector3d offset = state.getOffset(world, pos);
        return VoxelShapes.or(box(1, 1, 1, 15, 15, 15)).move(offset.x, offset.y, offset.z);
    }

    @Override
    public void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
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
    public void onPlace(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moving) {
        singularitySpawn(world, pos);
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack itemstack) {
        singularitySpawn(world, pos);
    }

    public static void singularitySpawn(World world, BlockPos pos) {
        if (world.isClientSide) return;
        ITagCollection<Block> tagCollection = BlockTags.getAllTags();
        ITag<Block> stopsHoleTag = tagCollection.getTagOrEmpty(new ResourceLocation("forge", "electrona/stops_black_hole"));

        if (world.getLevelData().getGameRules().getBoolean(Gamerules.DO_BLACK_HOLES_EXIST)) {

            for (BlockPos testPos : BlockPos.spiralAround(pos, 1000, Direction.EAST, Direction.SOUTH)) {
                if (!stopsHoleTag.contains(world.getBlockState(testPos).getBlock())) {
                    world.setBlockAndUpdate(testPos, BlockInit.HOLE.get().defaultBlockState());
                    return;
                }
            }
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileSingularity();
    }
}
