package net.reikeb.electrona.items;

import net.minecraft.block.*;
import net.minecraft.dispenser.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.*;
import net.minecraft.world.spawner.AbstractSpawner;

import net.minecraftforge.common.util.Constants;

import net.reikeb.electrona.init.EntityInit;

import java.util.Objects;

public class RadioactiveZombieEgg extends SpawnEggItem {

    public RadioactiveZombieEgg() {
        super(EntityInit.RADIOACTIVE_ZOMBIE_TYPE,
                0xFFCC00,
                0xFCA800,
                new Properties().stacksTo(1).tab(ItemGroup.TAB_MISC));

        DispenserBlock.registerBehavior(this,
                new DefaultDispenseItemBehavior() {
                    public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                        Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                        EntityType<?> entityType = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());
                        entityType.spawn(source.getLevel(), stack, null, source.getPos().relative(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
                        stack.shrink(1);
                        return stack;
                    }
                });
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        if (!world.isClientSide) {
            ItemStack itemstack = context.getItemInHand();
            BlockPos blockpos = context.getClickedPos();
            Direction direction = context.getClickedFace();
            BlockState blockstate = world.getBlockState(blockpos);
            TileEntity tileentity = world.getBlockEntity(blockpos);
            if (tileentity instanceof MobSpawnerTileEntity) {
                AbstractSpawner abstractspawner = ((MobSpawnerTileEntity) tileentity).getSpawner();
                abstractspawner.setEntityId(EntityInit.RADIOACTIVE_ZOMBIE.get());
                tileentity.setChanged();
                world.sendBlockUpdated(blockpos, blockstate, blockstate, Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
                itemstack.shrink(1);
                return ActionResultType.SUCCESS;
            }
            BlockPos blockpos1;
            if (blockstate.getCollisionShape(world, blockpos).isEmpty()) {
                blockpos1 = blockpos;
            } else {
                blockpos1 = blockpos.relative(direction);
            }

            if (EntityInit.RADIOACTIVE_ZOMBIE.get().spawn((ServerWorld) world, itemstack, context.getPlayer(), blockpos1, SpawnReason.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP) != null) {
                itemstack.shrink(1);
            }
        }
        return ActionResultType.SUCCESS;
    }
}
