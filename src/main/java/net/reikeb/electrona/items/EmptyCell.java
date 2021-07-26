package net.reikeb.electrona.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.ForgeEventFactory;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.PlayerInventoryChangedPacket;
import net.reikeb.electrona.setup.ItemGroups;

import java.util.List;

public class EmptyCell extends Item {

    public EmptyCell() {
        super(new Properties()
                .stacksTo(1)
                .rarity(Rarity.COMMON)
                .tab(ItemGroups.ELECTRONA_ITEMS));
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public int getUseDuration(ItemStack itemstack) {
        return 0;
    }

    @Override
    public float getDestroySpeed(ItemStack par1ItemStack, BlockState par2Block) {
        return 1F;
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(new TranslatableComponent("item.electrona.empty_cell.desc"));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        BlockHitResult raytraceresult = getPlayerPOVHitResult(worldIn, playerIn, ClipContext.Fluid.SOURCE_ONLY);
        InteractionResultHolder<ItemStack> ret = ForgeEventFactory.onBucketUse(playerIn, worldIn, itemstack, raytraceresult);
        if (ret != null) return ret;
        if (raytraceresult.getType() == HitResult.Type.MISS) return InteractionResultHolder.pass(itemstack);
        if (raytraceresult.getType() != HitResult.Type.BLOCK) return InteractionResultHolder.pass(itemstack);
        BlockPos blockpos = raytraceresult.getBlockPos();
        Direction direction = raytraceresult.getDirection();
        BlockPos blockpos1 = blockpos.relative(direction);
        if (worldIn.mayInteract(playerIn, blockpos) && playerIn.mayUseItemAt(blockpos1, direction, itemstack)) {
            BlockState blockstate1 = worldIn.getBlockState(blockpos);
            if (blockstate1.getBlock() instanceof BucketPickup) {
                Fluid fluid = ((BucketPickup) blockstate1.getBlock()).takeLiquid(worldIn, blockpos, blockstate1);
                if ((fluid != Fluids.WATER) && (fluid != Fluids.LAVA)) return InteractionResultHolder.fail(itemstack);
                worldIn.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 3);
                if (playerIn.isCreative()) {
                    if (!((fluid == Fluids.WATER && playerIn.inventory.contains(new ItemStack(ItemInit.WATER_CELL.get(), 1)))
                            || (fluid == Fluids.LAVA && playerIn.inventory.contains(new ItemStack(ItemInit.LAVA_CELL.get(), 1))))) {
                        playerIn.inventory.add(new ItemStack((fluid == Fluids.WATER ? ItemInit.WATER_CELL.get() : ItemInit.LAVA_CELL.get()), 1));
                    }
                } else {
                    playerIn.setItemInHand(handIn, new ItemStack((fluid == Fluids.WATER ? ItemInit.WATER_CELL.get() : ItemInit.LAVA_CELL.get()), 1));
                }
                return InteractionResultHolder.success(itemstack);
            }
        }
        return InteractionResultHolder.fail(itemstack);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        InteractionResult action = super.onItemUseFirst(stack, context);
        Level world = context.getLevel();
        BlockState state = world.getBlockState(context.getClickedPos());
        Player entity = context.getPlayer();

        if (entity == null) return InteractionResult.FAIL;

        if (state.is(Blocks.COMPOSTER)) {
            int level = state.getValue(BlockStateProperties.LEVEL_COMPOSTER);
            if (level >= 4) {
                if (entity.isCreative()) {
                    if (!entity.inventory.contains(new ItemStack(ItemInit.BIOMASS_CELL.get(), 1))) {
                        entity.inventory.add(new ItemStack(ItemInit.BIOMASS_CELL.get(), 1));
                    }
                } else {
                    entity.setItemInHand(context.getHand(), new ItemStack(ItemInit.BIOMASS_CELL.get(), 1));
                }
                world.setBlock(context.getClickedPos(), state.setValue(BlockStateProperties.LEVEL_COMPOSTER, (level - 4)), 3);
                if (entity instanceof ServerPlayer) {
                    entity.inventory.setChanged();
                } else {
                    NetworkManager.INSTANCE.sendToServer(new PlayerInventoryChangedPacket());
                }
            }
        }
        return action;
    }
}
