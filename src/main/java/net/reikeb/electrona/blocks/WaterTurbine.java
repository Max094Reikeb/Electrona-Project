package net.reikeb.electrona.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

import net.reikeb.electrona.misc.vm.CustomShapes;
import net.reikeb.electrona.tileentities.TileWaterTurbine;
import net.reikeb.electrona.utils.ElectronaUtils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class WaterTurbine extends AbstractWaterLoggableBlock {

    public static final DirectionProperty FACING = HorizontalBlock.FACING;

    public WaterTurbine() {
        super(Properties.of(Material.METAL)
                .sound(SoundType.METAL)
                .strength(4f, 6f)
                .lightLevel(s -> 0)
                .harvestLevel(1)
                .harvestTool(ToolType.PICKAXE));
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemstack, IBlockReader world, List<ITextComponent> list, ITooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(new TranslationTextComponent("block.electrona.water_turbine.desc1"));
        list.add(new TranslationTextComponent("block.electrona.water_turbine.desc2"));
    }

    VoxelShape shape = CustomShapes.WaterTurbine;
    VoxelShape northShape = ElectronaUtils.rotateShape(Direction.NORTH, Direction.SOUTH, shape);
    VoxelShape westShape = ElectronaUtils.rotateShape(Direction.NORTH, Direction.EAST, shape);
    VoxelShape eastShape = ElectronaUtils.rotateShape(Direction.NORTH, Direction.WEST, shape);

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        Direction facing = state.getValue(FACING);
        if (facing == Direction.NORTH) {
            return northShape;
        } else if (facing == Direction.EAST) {
            return eastShape;
        } else if (facing == Direction.WEST) {
            return westShape;
        }
        return shape;
    }

    @Override
    public void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState replaceState = context.getLevel().getBlockState(context.getClickedPos());
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, replaceState.getBlock() == Blocks.WATER);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
        return true;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileWaterTurbine();
    }
}
