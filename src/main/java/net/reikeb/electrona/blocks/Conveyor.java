package net.reikeb.electrona.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.reikeb.electrona.blockentities.ConveyorBlockEntity;
import net.reikeb.electrona.init.BlockEntityInit;
import net.reikeb.electrona.misc.vm.CustomShapes;
import net.reikeb.electrona.utils.ElectronaUtils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class Conveyor extends AbstractWaterLoggableBlock implements EntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    public Conveyor() {
        super(Properties.of(Material.METAL)
                .sound(SoundType.METAL)
                .strength(5f, 6f)
                .lightLevel(s -> 0)
                .requiresCorrectToolForDrops()
                .noOcclusion()
                .isRedstoneConductor((bs, br, bp) -> false));
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(ACTIVATED, false)
                .setValue(WATERLOGGED, false));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemstack, BlockGetter world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(new TranslatableComponent("block.electrona.conveyor.desc1"));
        list.add(new TranslatableComponent("block.electrona.conveyor.desc2"));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        if (facing == Direction.NORTH) {
            return ElectronaUtils.rotateShape(Direction.NORTH, Direction.SOUTH, CustomShapes.Conveyor);
        } else if (facing == Direction.EAST) {
            return ElectronaUtils.rotateShape(Direction.NORTH, Direction.WEST, CustomShapes.Conveyor);
        } else if (facing == Direction.WEST) {
            return ElectronaUtils.rotateShape(Direction.NORTH, Direction.EAST, CustomShapes.Conveyor);
        }
        return CustomShapes.Conveyor;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVATED, WATERLOGGED);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState replaceState = context.getLevel().getBlockState(context.getClickedPos());
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, replaceState.getBlock() == Blocks.WATER);
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(world, pos, state, entity);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof ConveyorBlockEntity conveyorBlockEntity)) return;
        double electronicPower = conveyorBlockEntity.getElectronicPower();
        Direction facing = conveyorBlockEntity.getBlockState().getValue(FACING);
        if ((electronicPower > 0) && ((entity instanceof ItemEntity) || (entity instanceof LivingEntity))) {
            if (facing == Direction.NORTH) {
                entity.setDeltaMovement(0, 0, 0.3);
            } else if (facing == Direction.SOUTH) {
                entity.setDeltaMovement(0, 0, (-0.3));
            } else if (facing == Direction.WEST) {
                entity.setDeltaMovement(0.3, 0, 0);
            } else if (facing == Direction.EAST) {
                entity.setDeltaMovement((-0.3), 0, 0);
            }
        }
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        return blockEntity instanceof MenuProvider ? (MenuProvider) blockEntity : null;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ConveyorBlockEntity(pos, state);
    }

    @Override
    public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int eventID, int eventParam) {
        super.triggerEvent(state, world, pos, eventID, eventParam);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity != null && blockEntity.triggerEvent(eventID, eventParam);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> blockEntityType) {
        return blockEntityType == BlockEntityInit.CONVEYOR_BLOCK_ENTITY.get() ? (BlockEntityTicker<T>) ConveyorBlockEntity.TICKER : null;
    }
}
