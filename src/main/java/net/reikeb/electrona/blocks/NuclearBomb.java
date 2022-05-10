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
import net.minecraftforge.network.NetworkHooks;
import net.reikeb.electrona.blockentities.NuclearBombBlockEntity;
import net.reikeb.electrona.entity.BombFallingEntity;
import net.reikeb.electrona.misc.vm.CustomShapes;
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

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return CustomShapes.getVoxelShape(facing, CustomShapes.NuclearBomb);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof NuclearBombBlockEntity) {
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, isMoving);
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
    public void tick(BlockState state, ServerLevel serverLevel, BlockPos pos, Random random) {
        if (serverLevel.isEmptyBlock(pos.below()) || isFree(serverLevel.getBlockState(pos.below())) && pos.getY() >= 0) {
            explode(serverLevel, pos);
        }
    }

    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof NuclearBombBlockEntity nuclearBombBlockEntity) {
                new NuclearExplosion(level, null, pos, nuclearBombBlockEntity.getNuclearCharge());
            }
        }
    }

    protected void falling(BombFallingEntity p_149829_1_) {
    }

    public boolean dropFromExplosion(Explosion explosion) {
        return false;
    }

    private void explode(ServerLevel serverLevel, BlockPos pos) {
        BlockEntity blockEntity = serverLevel.getBlockEntity(pos);
        if (blockEntity instanceof NuclearBombBlockEntity nuclearBombBlockEntity) {
            BombFallingEntity bombFallingEntity = new BombFallingEntity(serverLevel, (double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, serverLevel.getBlockState(pos), nuclearBombBlockEntity.isCharged(), nuclearBombBlockEntity.getNuclearCharge());
            this.falling(bombFallingEntity);
            serverLevel.addFreshEntity(bombFallingEntity);
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (player.isCrouching() && player.getItemInHand(hand).isEmpty()) {
                player.setItemInHand(hand, new ItemStack(this, 1));
                level.removeBlock(pos, true);
                return InteractionResult.SUCCESS;
            } else if (player.getItemInHand(hand).getItem() == Items.FLINT_AND_STEEL) {
                if (blockEntity instanceof NuclearBombBlockEntity nuclearBombBlockEntity) {
                    if (nuclearBombBlockEntity.isCharged()
                            && level.getLevelData().getGameRules().getBoolean(Gamerules.DO_NUCLEAR_BOMBS_EXPLODE)) {
                        if (!player.isCreative()) {
                            player.getItemInHand(hand).hurtAndBreak(1, player, (p_220287_1_) -> {
                                p_220287_1_.broadcastBreakEvent(hand);
                            });
                        }
                        new NuclearExplosion(level, player, pos, nuclearBombBlockEntity.getNuclearCharge());
                        return InteractionResult.SUCCESS;
                    }
                }
            } else {
                if (blockEntity instanceof NuclearBombBlockEntity) {
                    NetworkHooks.openGui((ServerPlayer) player, (MenuProvider) blockEntity, pos);
                } else {
                    throw new IllegalStateException("Our named container provider is missing!");
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity instanceof MenuProvider ? (MenuProvider) blockEntity : null;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NuclearBombBlockEntity(pos, state);
    }

    @Override
    public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int eventID, int eventParam) {
        super.triggerEvent(state, level, pos, eventID, eventParam);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity != null && blockEntity.triggerEvent(eventID, eventParam);
    }
}
