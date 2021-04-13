package net.reikeb.electrona.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.loot.LootContext;
import net.minecraft.state.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;

import net.minecraftforge.fml.network.NetworkHooks;

import net.reikeb.electrona.entity.BombFallingEntity;
import net.reikeb.electrona.misc.vm.CustomShapes;
import net.reikeb.electrona.tileentities.TileNuclearBomb;
import net.reikeb.electrona.utils.ElectronaUtils;
import net.reikeb.electrona.world.*;

import java.util.*;

public class NuclearBomb extends FallingBlock {

    public static final DirectionProperty FACING = HorizontalBlock.FACING;

    public NuclearBomb() {
        super(Properties.of(Material.METAL)
                .sound(SoundType.METAL)
                .strength(-1f, 6f)
                .lightLevel(s -> 0)
                .noOcclusion()
                .isRedstoneConductor((bs, br, bp) -> false)
                .requiresCorrectToolForDrops());
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }

    VoxelShape shape = CustomShapes.NuclearBomb;
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
            if (tileentity instanceof TileNuclearBomb) {
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Collections.emptyList();
    }

    @Override
    protected void falling(FallingBlockEntity fallingBlockEntity) {
        fallingBlockEntity.setHurtsEntities(true);
    }

    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.isEmptyBlock(pos.below()) || isFree(world.getBlockState(pos.below())) && pos.getY() >= 0) {
            explode(world, pos);
        }
    }

    public void wasExploded(World world, BlockPos pos, Explosion explosion) {
        if (!world.isClientSide) {
            TileEntity tile = world.getBlockEntity(pos);
            if (tile instanceof TileNuclearBomb) {
                new NuclearExplosion(world, pos.getX(), pos.getY(), pos.getZ(), ((TileNuclearBomb) tile).getNuclearCharge());
            }
        }
    }

    protected void falling(BombFallingEntity p_149829_1_) {
    }

    public boolean dropFromExplosion(Explosion explosion) {
        return false;
    }

    private void explode(ServerWorld world, BlockPos pos) {
        TileEntity tile = world.getBlockEntity(pos);
        if (tile instanceof TileNuclearBomb) {
            BombFallingEntity bombFallingEntity = new BombFallingEntity(world, (double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, world.getBlockState(pos), ((TileNuclearBomb) tile).isCharged(), ((TileNuclearBomb) tile).getNuclearCharge());
            this.falling(bombFallingEntity);
            world.addFreshEntity(bombFallingEntity);
        }
    }

    @Override
    public void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isClientSide) {
            TileEntity tile = worldIn.getBlockEntity(pos);
            if (player.isCrouching() && player.getItemInHand(handIn).isEmpty()) {
                player.setItemInHand(handIn, new ItemStack(this, 1));
                worldIn.removeBlock(pos, true);
                return ActionResultType.SUCCESS;
            } else if (player.getItemInHand(handIn).getItem() == Items.FLINT_AND_STEEL) {
                if (tile instanceof TileNuclearBomb) {
                    if (((TileNuclearBomb) tile).isCharged()
                            && worldIn.getLevelData().getGameRules().getBoolean(Gamerules.DO_NUCLEAR_BOMBS_EXPLODE)) {
                        if (!player.isCreative()) {
                            player.getItemInHand(handIn).hurtAndBreak(1, player, (p_220287_1_) -> {
                                p_220287_1_.broadcastBreakEvent(handIn);
                            });
                        }
                        new NuclearExplosion(worldIn, pos.getX(), pos.getY(), pos.getZ(), ((TileNuclearBomb) tile).getNuclearCharge());
                        return ActionResultType.SUCCESS;
                    }
                }
            } else {
                if (tile instanceof TileNuclearBomb) {
                    NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tile, pos);
                    return ActionResultType.SUCCESS;
                }
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
        return new TileNuclearBomb();
    }

    @Override
    public boolean triggerEvent(BlockState state, World world, BlockPos pos, int eventID, int eventParam) {
        super.triggerEvent(state, world, pos, eventID, eventParam);
        TileEntity tileentity = world.getBlockEntity(pos);
        return tileentity != null && tileentity.triggerEvent(eventID, eventParam);
    }
}
