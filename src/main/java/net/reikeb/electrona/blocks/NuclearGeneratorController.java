package net.reikeb.electrona.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import net.reikeb.electrona.tileentities.TileNuclearGeneratorController;
import net.reikeb.electrona.utils.ElectronaUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class NuclearGeneratorController extends Block {

    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    public NuclearGeneratorController() {
        super(Properties.of(Material.METAL)
                .sound(SoundType.METAL)
                .strength(4f, 6f)
                .lightLevel(s -> 0)
                .harvestLevel(2)
                .harvestTool(ToolType.PICKAXE)
                .requiresCorrectToolForDrops()
                .noOcclusion()
                .isRedstoneConductor((bs, br, bp) -> false));
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(ACTIVATED, false));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemstack, IBlockReader world, List<ITextComponent> list, ITooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(new TranslationTextComponent("block.electrona.ngc.desc1"));
        list.add(new TranslationTextComponent("block.electrona.ngc.desc2"));
    }

    VoxelShape shape = Stream.of(
            Block.box(14, 12, 15, 15, 13, 15.5),
            Block.box(1, 12, 15, 2, 13, 15.5),
            Block.box(1, 12, 0.5, 2, 13, 1),
            Block.box(14, 0, 15, 15, 1, 15.5),
            Block.box(1, 0, 15, 2, 1, 15.5),
            Block.box(1, 0, 0.5, 2, 1, 1),
            Block.box(14, 0, 0.5, 15, 1, 1),
            Block.box(14, 12, 0.5, 15, 13, 1),
            Block.box(15, 12, 14, 15.5, 13, 15),
            Block.box(0.5, 12, 14, 1, 13, 15),
            Block.box(0.5, 12, 1, 1, 13, 2),
            Block.box(15, 0, 14, 15.5, 1, 15),
            Block.box(15, 0, 14.5, 15.5, 1, 15.5),
            Block.box(0.5, 0, 14, 1, 1, 15),
            Block.box(15, 0, 1, 15.5, 1, 2),
            Block.box(0.5, 0, 1, 1, 1, 2),
            Block.box(15, 12, 1, 15.5, 13, 2),
            Block.box(0.5, 12, 15, 1.5, 13, 15.5),
            Block.box(14.5, 12, 15, 15.5, 13, 15.5),
            Block.box(0.5, 12, 0.5, 1.5, 13, 1),
            Block.box(0.5, 0, 15, 1.5, 1, 15.5),
            Block.box(0.5, 0, 0.5, 1.5, 1, 1),
            Block.box(14.5, 0, 0.5, 15.5, 1, 1),
            Block.box(14.5, 12, 0.5, 15.5, 13, 1),
            Block.box(15, 11, 14.5, 15.5, 12, 15.5),
            Block.box(0.5, 11, 15, 1.5, 12, 15.5),
            Block.box(14.5, 11, 15, 15.5, 12, 15.5),
            Block.box(15, 11, 0.5, 15.5, 12, 1.5),
            Block.box(15, 1, 0.5, 15.5, 2, 1.5),
            Block.box(0.5, 1, 14.5, 1, 2, 15.5),
            Block.box(0.5, 1, 0.5, 1, 2, 1.5),
            Block.box(15, 1, 14.5, 15.5, 2, 15.5),
            Block.box(0.5, 11, 0.5, 1, 12, 1.5),
            Block.box(0.5, 11, 14.5, 1, 12, 15.5),
            Block.box(0.5, 11, 0.5, 1.5, 12, 1),
            Block.box(14.4, 1, 15, 15.4, 2, 15.5),
            Block.box(0.5, 1, 15, 1.5, 2, 15.5),
            Block.box(0.5, 1, 0.5, 1.5, 2, 1),
            Block.box(14.4, 1, 0.5, 15.4, 2, 1),
            Block.box(14.4, 11, 0.5, 15.4, 12, 1),
            Block.box(1, 0, 1, 2, 13, 3),
            Block.box(14, 0, 1, 15, 13, 3),
            Block.box(1, 0, 14, 15, 13, 15),
            Block.box(2, 12, 1, 14, 13, 3),
            Block.box(2, 0, 1, 14, 1, 3),
            Block.box(5, 9.2, 1, 11, 12, 3),
            Block.box(11, 6.6, 1, 14, 12, 3),
            Block.box(1, 0, 3, 15, 13, 14),
            Block.box(2, 13, 2, 14, 13.100000000000001, 14),
            Block.box(15, 0.5, 2, 15.1, 12.5, 14),
            Block.box(0.9, 0.5, 2, 0.9999999999999997, 12.5, 14),
            Block.box(2, 0.5, 15, 14, 12.5, 15.100000000000001),
            Block.box(2, 1, 1, 14, 6.6, 3),
            Block.box(2, 6.6, 1, 5, 12, 3),
            Block.box(11, 5, 0.8, 11.5, 5.5, 1),
            Block.box(5.5, 5.45, 3.45, 10.5, 5.95, 4.45),
            Block.box(5.5, 3.0999999999999996, 3.45, 10.5, 3.5999999999999996, 4.45),
            Block.box(10.5, 3.0999999999999996, 3.45, 11, 6.1, 4.45),
            Block.box(5, 3.0999999999999996, 3.45, 5.5, 6.1, 4.45),
            Block.box(5, 3, 3.5, 11, 6, 4.5),
            Block.box(10, 5, 0.8, 10.5, 5.5, 1),
            Block.box(10, 4, 0.8, 10.5, 4.5, 1),
            Block.box(6, 4, 0.8, 6.5, 4.5, 1),
            Block.box(6, 3, 0.7999999999999999, 6.5, 3.5, 1),
            Block.box(6, 2, 0.8, 6.5, 2.5, 1),
            Block.box(4.5, 3, 0.7999999999999999, 5, 3.5, 1),
            Block.box(10, 3, 0.8, 10.5, 3.5, 1),
            Block.box(3, 2, 0.7999999999999999, 3.5, 2.5, 1),
            Block.box(6, 5, 0.8, 6.5, 5.5, 1),
            Block.box(3, 3, 0.7999999999999999, 3.5, 3.5, 1),
            Block.box(3, 4, 0.8, 3.5, 4.5, 1),
            Block.box(3, 5, 0.8, 3.5, 5.5, 1),
            Block.box(4.5, 4, 0.7999999999999999, 5, 4.5, 1),
            Block.box(4.5, 2, 0.8, 5, 2.5, 1),
            Block.box(4.5, 5, 0.7999999999999999, 5, 5.5, 1),
            Block.box(10, 2, 0.8, 10.5, 2.5, 1),
            Block.box(11, 3, 0.8, 11.5, 3.5, 1),
            Block.box(11, 2, 0.8, 11.5, 2.5, 1),
            Block.box(12, 3, 0.8, 12.5, 3.5, 1),
            Block.box(12, 2, 0.8, 12.5, 2.5, 1),
            Block.box(12, 4, 0.8, 12.5, 4.5, 1),
            Block.box(12, 5, 0.8, 12.5, 5.5, 1),
            Block.box(11, 4, 0.7999999999999999, 11.5, 4.5, 1),
            Block.box(8, 5, 0.8, 8.5, 5.5, 1),
            Block.box(8.2, 5.699999999999999, -7.1, 8.299999999999999, 6.199999999999999, -6.999999999999998),
            Block.box(8, 3, 0.8, 8.5, 3.5, 1),
            Block.box(8.2, -2.8, -1.9, 8.299999999999999, -2.6999999999999984, -1.4),
            Block.box(9, 3.6000000000000005, 3.45, 10, 5.5, 4.45),
            Block.box(7.5, 3.6000000000000005, 3.45, 8.5, 5.5, 4.45),
            Block.box(6, 3.6000000000000005, 3.45, 7, 5.5, 4.45)
    ).reduce((v1, v2) -> {
        return VoxelShapes.join(v1, v2, IBooleanFunction.OR);
    }).get();

    VoxelShape southShape = ElectronaUtils.rotateShape(Direction.NORTH, Direction.SOUTH, shape);
    VoxelShape eastShape = ElectronaUtils.rotateShape(Direction.NORTH, Direction.EAST, shape);
    VoxelShape westShape = ElectronaUtils.rotateShape(Direction.NORTH, Direction.WEST, shape);

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        Direction facing = state.getValue(FACING);
        if (facing == Direction.SOUTH) {
            return southShape;
        } else if (facing == Direction.EAST) {
            return eastShape;
        } else if (facing == Direction.WEST) {
            return westShape;
        }
        return shape;
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = world.getBlockEntity(pos);
            if (tileentity instanceof TileNuclearGeneratorController) {
                ((TileNuclearGeneratorController) tileentity).dropItems(world, pos);
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVATED);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isClientSide) {
            TileEntity tile = worldIn.getBlockEntity(pos);
            if (tile instanceof TileNuclearGeneratorController) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tile, pos);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public INamedContainerProvider getMenuProvider(BlockState state, World worldIn, BlockPos pos) {
        TileEntity tileEntity = worldIn.getBlockEntity(pos);
        return tileEntity instanceof INamedContainerProvider ? (INamedContainerProvider) tileEntity : null;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileNuclearGeneratorController();
    }

    @Override
    public boolean triggerEvent(BlockState state, World world, BlockPos pos, int eventID, int eventParam) {
        super.triggerEvent(state, world, pos, eventID, eventParam);
        TileEntity tileentity = world.getBlockEntity(pos);
        return tileentity != null && tileentity.triggerEvent(eventID, eventParam);
    }
}
