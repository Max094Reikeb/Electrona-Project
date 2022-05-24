package net.reikeb.electrona.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
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
import net.minecraft.world.level.material.FluidState;
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
    public void appendHoverText(ItemStack itemstack, Level level, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, level, list, flag);
        list.add(new TranslatableComponent("item.electrona.empty_cell.desc"));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        BlockHitResult raytraceresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        InteractionResultHolder<ItemStack> ret = ForgeEventFactory.onBucketUse(player, level, itemstack, raytraceresult);
        if (ret != null) return ret;
        if (raytraceresult.getType() == HitResult.Type.MISS) return InteractionResultHolder.pass(itemstack);
        if (raytraceresult.getType() != HitResult.Type.BLOCK) return InteractionResultHolder.pass(itemstack);
        BlockPos blockpos = raytraceresult.getBlockPos();
        Direction direction = raytraceresult.getDirection();
        BlockPos blockpos1 = blockpos.relative(direction);
        if (level.mayInteract(player, blockpos) && player.mayUseItemAt(blockpos1, direction, itemstack)) {
            BlockState blockstate1 = level.getBlockState(blockpos);
            FluidState fluidState = level.getFluidState(blockpos);
            if (!fluidState.is(FluidTags.WATER) && !fluidState.is(FluidTags.LAVA))
                return InteractionResultHolder.fail(itemstack);
            if (blockstate1.getBlock() instanceof BucketPickup && !((BucketPickup) blockstate1.getBlock()).pickupBlock(level, blockpos, blockstate1).isEmpty()) {
                level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 3);
                if (player.isCreative()) {
                    if (!((fluidState.is(FluidTags.WATER) && player.inventory.contains(new ItemStack(ItemInit.WATER_CELL.get(), 1)))
                            || (fluidState.is(FluidTags.LAVA) && player.inventory.contains(new ItemStack(ItemInit.LAVA_CELL.get(), 1))))) {
                        player.inventory.add(new ItemStack((fluidState.is(FluidTags.WATER) ? ItemInit.WATER_CELL.get() : ItemInit.LAVA_CELL.get()), 1));
                    }
                } else {
                    player.setItemInHand(hand, new ItemStack((fluidState.is(FluidTags.WATER) ? ItemInit.WATER_CELL.get() : ItemInit.LAVA_CELL.get())));
                }
                return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide);
            }
        }
        return InteractionResultHolder.fail(itemstack);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        InteractionResult action = super.onItemUseFirst(stack, context);
        Level level = context.getLevel();
        BlockState state = level.getBlockState(context.getClickedPos());
        Player entity = context.getPlayer();

        if (entity == null) return InteractionResult.FAIL;

        if (state.is(Blocks.COMPOSTER)) {
            int composterLevel = state.getValue(BlockStateProperties.LEVEL_COMPOSTER);
            if (composterLevel >= 4) {
                if (entity.isCreative()) {
                    if (!entity.inventory.contains(new ItemStack(ItemInit.BIOMASS_CELL.get(), 1))) {
                        entity.inventory.add(new ItemStack(ItemInit.BIOMASS_CELL.get(), 1));
                    }
                } else {
                    entity.setItemInHand(context.getHand(), new ItemStack(ItemInit.BIOMASS_CELL.get(), 1));
                }
                level.setBlock(context.getClickedPos(), state.setValue(BlockStateProperties.LEVEL_COMPOSTER, (composterLevel - 4)), 3);
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
