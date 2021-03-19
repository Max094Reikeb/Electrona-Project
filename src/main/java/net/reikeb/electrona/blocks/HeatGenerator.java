package net.reikeb.electrona.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;

import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

import net.reikeb.electrona.tileentities.TileCompressor;
import net.reikeb.electrona.tileentities.TileHeatGenerator;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class HeatGenerator extends Block {

    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public static final BooleanProperty HEATING = BooleanProperty.create("heating");

    public HeatGenerator() {
        super(Properties.of(Material.STONE)
                .sound(SoundType.METAL)
                .strength(4f, 6f)
                .lightLevel(s -> (s.getValue(HEATING) ? 15 : 0))
                .harvestLevel(2)
                .harvestTool(ToolType.PICKAXE));
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HEATING, false));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemstack, IBlockReader world, List<ITextComponent> list, ITooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(new TranslationTextComponent("block.electrona.heat_generator.desc1"));
        list.add(new TranslationTextComponent("block.electrona.heat_generator.desc2"));
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
        builder.add(FACING, HEATING);
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
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        TileEntity tile = world.getBlockEntity(pos);
        if (tile instanceof TileHeatGenerator) {
            if (player instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                TileHeatGenerator tileHeatGenerator = (TileHeatGenerator) tile;
                double electronicPower = tileHeatGenerator.getTileData().getDouble("ElectronicPower");
                if ((serverPlayer.getMainHandItem().getItem() == Items.LAVA_BUCKET) && (electronicPower <= 800)) {
                    if (!world.isClientSide()) {
                        tileHeatGenerator.getTileData().putDouble("ElectronicPower", electronicPower + 200);
                    }
                    if (!serverPlayer.abilities.instabuild) {
                        serverPlayer.setItemInHand(Hand.MAIN_HAND, new ItemStack(Items.BUCKET, 1));
                        serverPlayer.inventory.setChanged();
                    }
                } else if ((serverPlayer.getMainHandItem().getItem() == Items.LAVA_BUCKET) && (electronicPower > 800)) {
                    if (!world.isClientSide()) {
                        tileHeatGenerator.getTileData().putDouble("ElectronicPower", electronicPower + (1000 - electronicPower));
                    }
                    if (!serverPlayer.abilities.instabuild) {
                        serverPlayer.setItemInHand(Hand.MAIN_HAND, new ItemStack(Items.BUCKET, 1));
                        serverPlayer.inventory.setChanged();
                    }
                }
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileHeatGenerator();
    }
}
