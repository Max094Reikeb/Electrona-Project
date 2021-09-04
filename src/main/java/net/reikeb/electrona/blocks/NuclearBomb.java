package net.reikeb.electrona.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.minecraftforge.fmllegacy.network.NetworkHooks;

import net.reikeb.electrona.entity.BombFallingEntity;
import net.reikeb.electrona.misc.vm.CustomShapes;
import net.reikeb.electrona.tileentities.TileNuclearBomb;
import net.reikeb.electrona.utils.ElectronaUtils;
import net.reikeb.electrona.world.Gamerules;
import net.reikeb.electrona.world.NuclearExplosion;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NuclearBomb extends FallingBlock implements EntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

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
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    VoxelShape shape = CustomShapes.NuclearBomb;
    VoxelShape southShape = ElectronaUtils.rotateShape(Direction.NORTH, Direction.SOUTH, shape);
    VoxelShape eastShape = ElectronaUtils.rotateShape(Direction.NORTH, Direction.EAST, shape);
    VoxelShape westShape = ElectronaUtils.rotateShape(Direction.NORTH, Direction.WEST, shape);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
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
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tileentity = world.getBlockEntity(pos);
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
        fallingBlockEntity.setHurtsEntities(2.0F, 40);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        if (world.isEmptyBlock(pos.below()) || isFree(world.getBlockState(pos.below())) && pos.getY() >= 0) {
            explode(world, pos);
        }
    }

    public void wasExploded(Level world, BlockPos pos, Explosion explosion) {
        if (!world.isClientSide) {
            BlockEntity tile = world.getBlockEntity(pos);
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

    private void explode(ServerLevel world, BlockPos pos) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof TileNuclearBomb) {
            BombFallingEntity bombFallingEntity = new BombFallingEntity(world, (double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, world.getBlockState(pos), ((TileNuclearBomb) tile).isCharged(), ((TileNuclearBomb) tile).getNuclearCharge());
            this.falling(bombFallingEntity);
            world.addFreshEntity(bombFallingEntity);
        }
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!worldIn.isClientSide) {
            BlockEntity tile = worldIn.getBlockEntity(pos);
            if (player.isCrouching() && player.getItemInHand(handIn).isEmpty()) {
                player.setItemInHand(handIn, new ItemStack(this, 1));
                worldIn.removeBlock(pos, true);
                return InteractionResult.SUCCESS;
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
                        return InteractionResult.SUCCESS;
                    }
                }
            } else {
                if (tile instanceof TileNuclearBomb) {
                    NetworkHooks.openGui((ServerPlayer) player, (MenuProvider) tile, pos);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        return tileEntity instanceof MenuProvider ? (MenuProvider) tileEntity : null;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileNuclearBomb(pos, state);
    }

    @Override
    public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int eventID, int eventParam) {
        super.triggerEvent(state, world, pos, eventID, eventParam);
        BlockEntity tileentity = world.getBlockEntity(pos);
        return tileentity != null && tileentity.triggerEvent(eventID, eventParam);
    }
}
