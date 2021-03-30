package net.reikeb.electrona.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.loot.LootContext;
import net.minecraft.state.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.*;
import net.minecraft.util.text.*;
import net.minecraft.world.*;

import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.common.ToolType;

import net.reikeb.electrona.misc.vm.CustomShapes;
import net.reikeb.electrona.tileentities.TileConveyor;
import net.reikeb.electrona.utils.ElectronaUtils;

import java.util.*;

public class Conveyor extends Block {

    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    public Conveyor() {
        super(Properties.of(Material.METAL)
                .sound(SoundType.METAL)
                .strength(5f, 6f)
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
        list.add(new TranslationTextComponent("block.electrona.conveyor.desc1"));
        list.add(new TranslationTextComponent("block.electrona.conveyor.desc2"));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }

    VoxelShape shape = CustomShapes.Conveyor;
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
        ;
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void stepOn(World world, BlockPos pos, Entity entity) {
        super.stepOn(world, pos, entity);
        TileEntity tile = world.getBlockEntity(pos);
        if (!(tile instanceof TileConveyor)) return;
        TileConveyor tileConveyor = (TileConveyor) tile;
        double electronicPower = tileConveyor.getTileData().getDouble("ElectronicPower");
        Direction facing = tileConveyor.getBlockState().getValue(FACING);
        if ((electronicPower > 0) && ((entity instanceof ItemEntity) || (entity instanceof LivingEntity))) {
            if (facing == Direction.NORTH) {
                entity.setDeltaMovement(0, 0, (-0.3));
            } else if (facing == Direction.SOUTH) {
                entity.setDeltaMovement(0, 0, 0.3);
            } else if (facing == Direction.WEST) {
                entity.setDeltaMovement((-0.3), 0, 0);
            } else if (facing == Direction.EAST) {
                entity.setDeltaMovement(0.3, 0, 0);
            }
        }
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
        return new TileConveyor();
    }

    @Override
    public boolean triggerEvent(BlockState state, World world, BlockPos pos, int eventID, int eventParam) {
        super.triggerEvent(state, world, pos, eventID, eventParam);
        TileEntity tileentity = world.getBlockEntity(pos);
        return tileentity != null && tileentity.triggerEvent(eventID, eventParam);
    }
}
