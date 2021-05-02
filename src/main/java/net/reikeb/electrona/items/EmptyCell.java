package net.reikeb.electrona.items;

import net.minecraft.block.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

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
    public void appendHoverText(ItemStack itemstack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(new TranslationTextComponent("item.electrona.empty_cell.desc"));
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        BlockRayTraceResult raytraceresult = getPlayerPOVHitResult(worldIn, playerIn, RayTraceContext.FluidMode.SOURCE_ONLY);
        ActionResult<ItemStack> ret = ForgeEventFactory.onBucketUse(playerIn, worldIn, itemstack, raytraceresult);
        if (ret != null) return ret;
        if (raytraceresult.getType() == RayTraceResult.Type.MISS) return ActionResult.pass(itemstack);
        if (raytraceresult.getType() != RayTraceResult.Type.BLOCK) return ActionResult.pass(itemstack);
        BlockPos blockpos = raytraceresult.getBlockPos();
        Direction direction = raytraceresult.getDirection();
        BlockPos blockpos1 = blockpos.relative(direction);
        if (worldIn.mayInteract(playerIn, blockpos) && playerIn.mayUseItemAt(blockpos1, direction, itemstack)) {
            BlockState blockstate1 = worldIn.getBlockState(blockpos);
            if (blockstate1.getBlock() instanceof IBucketPickupHandler) {
                Fluid fluid = ((IBucketPickupHandler) blockstate1.getBlock()).takeLiquid(worldIn, blockpos, blockstate1);
                if ((fluid != Fluids.WATER) && (fluid != Fluids.LAVA)) return ActionResult.fail(itemstack);
                worldIn.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 3);
                if (playerIn.isCreative()) {
                    if (!((fluid == Fluids.WATER && playerIn.inventory.contains(new ItemStack(ItemInit.WATER_CELL.get(), 1)))
                            || (fluid == Fluids.LAVA && playerIn.inventory.contains(new ItemStack(ItemInit.LAVA_CELL.get(), 1))))) {
                        playerIn.inventory.add(new ItemStack((fluid == Fluids.WATER ? ItemInit.WATER_CELL.get() : ItemInit.LAVA_CELL.get()), 1));
                    }
                } else {
                    playerIn.setItemInHand(handIn, new ItemStack((fluid == Fluids.WATER ? ItemInit.WATER_CELL.get() : ItemInit.LAVA_CELL.get()), 1));
                }
                return ActionResult.success(itemstack);
            }
        }
        return ActionResult.fail(itemstack);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        ActionResultType action = super.onItemUseFirst(stack, context);
        World world = context.getLevel();
        BlockState state = world.getBlockState(context.getClickedPos());
        PlayerEntity entity = context.getPlayer();

        if (entity == null) return ActionResultType.FAIL;

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
                if (entity instanceof ServerPlayerEntity) {
                    entity.inventory.setChanged();
                } else {
                    NetworkManager.INSTANCE.sendToServer(new PlayerInventoryChangedPacket());
                }
            }
        }
        return action;
    }
}
