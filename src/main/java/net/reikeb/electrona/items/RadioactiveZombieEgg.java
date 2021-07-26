package net.reikeb.electrona.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.common.util.Constants;

import net.reikeb.electrona.init.EntityInit;

import java.util.Objects;

public class RadioactiveZombieEgg extends SpawnEggItem {

    public RadioactiveZombieEgg() {
        super(EntityInit.RADIOACTIVE_ZOMBIE_TYPE,
                7958625,
                7969893,
                new Properties().stacksTo(1).tab(CreativeModeTab.TAB_MISC));

        DispenserBlock.registerBehavior(this,
                new DefaultDispenseItemBehavior() {
                    public ItemStack dispenseStack(BlockSource source, ItemStack stack) {
                        Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                        EntityType<?> entityType = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());
                        entityType.spawn(source.getLevel(), stack, null, source.getPos().relative(direction), MobSpawnType.DISPENSER, direction != Direction.UP, false);
                        stack.shrink(1);
                        return stack;
                    }
                });
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if (!world.isClientSide) {
            ItemStack itemstack = context.getItemInHand();
            BlockPos blockpos = context.getClickedPos();
            Direction direction = context.getClickedFace();
            BlockState blockstate = world.getBlockState(blockpos);
            BlockEntity tileentity = world.getBlockEntity(blockpos);
            if (tileentity instanceof SpawnerBlockEntity) {
                BaseSpawner abstractspawner = ((SpawnerBlockEntity) tileentity).getSpawner();
                abstractspawner.setEntityId(EntityInit.RADIOACTIVE_ZOMBIE.get());
                tileentity.setChanged();
                world.sendBlockUpdated(blockpos, blockstate, blockstate, Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
                itemstack.shrink(1);
                return InteractionResult.SUCCESS;
            }
            BlockPos blockpos1;
            if (blockstate.getCollisionShape(world, blockpos).isEmpty()) {
                blockpos1 = blockpos;
            } else {
                blockpos1 = blockpos.relative(direction);
            }

            if (EntityInit.RADIOACTIVE_ZOMBIE.get().spawn((ServerLevel) world, itemstack, context.getPlayer(), blockpos1, MobSpawnType.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP) != null) {
                itemstack.shrink(1);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
