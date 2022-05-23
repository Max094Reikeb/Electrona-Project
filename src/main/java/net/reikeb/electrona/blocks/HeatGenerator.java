package net.reikeb.electrona.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.reikeb.electrona.blockentities.HeatGeneratorBlockEntity;
import net.reikeb.electrona.init.BlockEntityInit;
import net.reikeb.electrona.misc.BlockStateProperties;
import net.reikeb.maxilib.intface.EnergyInterface;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class HeatGenerator extends Block implements EntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty HEATING = BlockStateProperties.HEATING;

    public HeatGenerator() {
        super(Properties.of(Material.STONE)
                .sound(SoundType.METAL)
                .strength(4f, 6f)
                .lightLevel(s -> (s.getValue(HEATING) ? 15 : 0)));
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HEATING, false));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemstack, BlockGetter blockGetter, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, blockGetter, list, flag);
        list.add(new TranslatableComponent("block.electrona.heat_generator.desc1"));
        list.add(new TranslatableComponent("block.electrona.heat_generator.desc2"));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HEATING);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile instanceof HeatGeneratorBlockEntity tileHeatGenerator) {
            if (player instanceof ServerPlayer serverPlayer) {
                double electronicPower = tileHeatGenerator.getEnergy();
                if ((serverPlayer.getMainHandItem().getItem() == Items.LAVA_BUCKET) && (electronicPower <= 800)) {
                    if (!level.isClientSide()) {
                        EnergyInterface.fillEnergy(tileHeatGenerator, 200);
                    }
                    if (!serverPlayer.isCreative()) {
                        serverPlayer.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BUCKET, 1));
                        serverPlayer.inventory.setChanged();
                    }
                } else if ((serverPlayer.getMainHandItem().getItem() == Items.LAVA_BUCKET) && (electronicPower > 800)) {
                    if (!level.isClientSide()) {
                        tileHeatGenerator.setEnergy(electronicPower + (1000 - electronicPower));
                    }
                    if (!serverPlayer.isCreative()) {
                        serverPlayer.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BUCKET, 1));
                        serverPlayer.inventory.setChanged();
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HeatGeneratorBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return blockEntityType == BlockEntityInit.HEAT_GENERATOR_BLOCK_ENTITY.get() ? (BlockEntityTicker<T>) HeatGeneratorBlockEntity.TICKER : null;
    }
}
