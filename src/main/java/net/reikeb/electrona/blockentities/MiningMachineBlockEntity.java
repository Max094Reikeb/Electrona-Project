package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.MiningMachineContainer;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.maxilib.abs.AbstractEnergyBlockEntity;
import net.reikeb.maxilib.intface.EnergyInterface;

import java.util.Random;

import static net.reikeb.electrona.init.BlockEntityInit.MINING_MACHINE_BLOCK_ENTITY;

public class MiningMachineBlockEntity extends AbstractEnergyBlockEntity {

    public static final BlockEntityTicker<MiningMachineBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    private int wait;

    public MiningMachineBlockEntity(BlockPos pos, BlockState state) {
        super(MINING_MACHINE_BLOCK_ENTITY.get(), pos, state, "mining_machine", Electrona.MODID, 3);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new MiningMachineContainer(id, this.getBlockPos(), playerInventory, player);
    }

    public <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState state, T t) {
        this.setMaxEnergy(6000);
        if (level == null) return;

        wait++;
        if (wait < 30) return;
        wait = 0;

        double ny;
        double sy;
        double x = blockPos.getX();
        double y = blockPos.getY();
        double z = blockPos.getZ();
        ItemStack item;

        if ((level.hasNeighborSignal(blockPos)) && (this.getEnergy() >= 50)) {
            ny = 1;
            while ((BlockInit.MINING_PIPE.get() == (level.getBlockState(new BlockPos((int) x, (int) (y - (ny)), (int) z)))
                    .getBlock())) {
                ny = (ny) + 1;
            }
            BlockPos _tempPos = new BlockPos(x, (y - ny), z);
            Block _tempBlock = level.getBlockState(_tempPos).getBlock();
            if ((Blocks.AIR == _tempBlock) || (Blocks.VOID_AIR == _tempBlock) || (Blocks.CAVE_AIR == _tempBlock)) {
                level.setBlock(_tempPos, BlockInit.MINING_PIPE.get().defaultBlockState(), 3);
            } else {
                if ((!(level.getBlockState(_tempPos)).getFluidState().isSource()) && (!(_tempBlock instanceof LiquidBlock))) {
                    item = this.inventory.getStackInSlot(0);
                    if (item.isCorrectToolForDrops(level.getBlockState(_tempPos)) && (Blocks.BEDROCK != _tempBlock)) {
                        if (!level.isClientSide()) {
                            ItemEntity entityToSpawn = new ItemEntity(level, x, (y + 1), z, (new ItemStack(_tempBlock)));
                            entityToSpawn.setPickUpDelay(10);
                            level.addFreshEntity(entityToSpawn);
                        }
                        level.destroyBlock(_tempPos, false);
                        if (this.inventory.getStackInSlot(0).hurt(1, new Random(), null)) {
                            this.inventory.getStackInSlot(0).shrink(1);
                            this.inventory.getStackInSlot(0).setDamageValue(0);
                        }
                        EnergyInterface.drainEnergy(this, 50);
                    }
                } else {
                    if (level.getBlockState(_tempPos).getFluidState().isSource()) {
                        boolean flag = false;
                        int slot = 0;
                        if (Items.BUCKET == this.inventory.getStackInSlot(1).getItem()) {
                            flag = true;
                            slot = 1;
                        } else if (Items.BUCKET == this.inventory.getStackInSlot(2).getItem()) {
                            flag = true;
                            slot = 2;
                        }
                        if (flag) {
                            if (Blocks.WATER == (level.getFluidState(_tempPos).createLegacyBlock()).getBlock()) {
                                this.inventory.setStackInSlot(slot, new ItemStack(Items.WATER_BUCKET, 1));
                            } else if (Blocks.LAVA == (level.getFluidState(_tempPos).createLegacyBlock()).getBlock()) {
                                this.inventory.setStackInSlot(slot, new ItemStack(Items.LAVA_BUCKET, 1));
                            }
                            level.setBlock(_tempPos, Blocks.AIR.defaultBlockState(), 3);
                            EnergyInterface.drainEnergy(this, 50);
                        }
                    }
                }
            }
        } else {
            sy = 1;
            while ((BlockInit.MINING_PIPE.get() == (level.getBlockState(new BlockPos(x, (y - (sy)), z)))
                    .getBlock())) {
                sy = (sy) + 1;
            }
            BlockPos _tempPosNew = new BlockPos(x, (y - sy), z);
            Block _tempBlockNew = level.getBlockState(_tempPosNew).getBlock();
            if ((BlockInit.MINING_PIPE.get() != _tempBlockNew)
                    && (BlockInit.MINING_MACHINE.get() != (level.getBlockState(new BlockPos(x, ((y - (sy)) + 1), z))).getBlock())) {
                level.setBlock(new BlockPos(x, ((y - (sy)) + 1), z), Blocks.AIR.defaultBlockState(), 3);
            }
        }

        this.setChanged();
        level.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(), 3);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.wait = compound.getInt("wait");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("wait", this.wait);
    }
}
