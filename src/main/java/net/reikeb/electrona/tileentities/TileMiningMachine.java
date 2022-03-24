package net.reikeb.electrona.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.containers.MiningMachineContainer;
import net.reikeb.electrona.init.BlockInit;

import java.util.Random;

import static net.reikeb.electrona.init.TileEntityInit.TILE_MINING_MACHINE;

public class TileMiningMachine extends AbstractTileEntity {

    public static final BlockEntityTicker<TileMiningMachine> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    public double electronicPower;
    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int p_39284_) {
            if (p_39284_ == 0) {
                return (int) TileMiningMachine.this.electronicPower;
            }
            return 0;
        }

        @Override
        public void set(int p_39285_, int p_39286_) {
            if (p_39285_ == 0) {
                TileMiningMachine.this.electronicPower = p_39286_;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };
    private int maxStorage;
    private int wait;

    public TileMiningMachine(BlockPos pos, BlockState state) {
        super(TILE_MINING_MACHINE.get(), pos, state, 3);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.electrona.mining_machine.name");
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("mining_machine");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new MiningMachineContainer(id, player, this, dataAccess);
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
        // We get the NBT Tags
        this.getTileData().putInt("MaxStorage", 6000);
        double electronicPower = this.getTileData().getDouble("ElectronicPower");

        wait++;
        if (wait < 30) return;
        wait = 0;

        if (world != null) { // Avoid NullPointerExceptions
            double ny;
            double sy;
            double x = blockPos.getX();
            double y = blockPos.getY();
            double z = blockPos.getZ();
            ItemStack item;

            if ((world.hasNeighborSignal(blockPos)) && (electronicPower >= 50)) {
                ny = 1;
                while ((BlockInit.MINING_PIPE.get() == (world.getBlockState(new BlockPos((int) x, (int) (y - (ny)), (int) z)))
                        .getBlock())) {
                    ny = (ny) + 1;
                }
                BlockPos _tempPos = new BlockPos(x, (y - ny), z);
                Block _tempBlock = world.getBlockState(_tempPos).getBlock();
                if ((Blocks.AIR == _tempBlock) || (Blocks.VOID_AIR == _tempBlock) || (Blocks.CAVE_AIR == _tempBlock)) {
                    world.setBlock(_tempPos, BlockInit.MINING_PIPE.get().defaultBlockState(), 3);
                } else {
                    if ((!(world.getBlockState(_tempPos)).getFluidState().isSource()) && (!(_tempBlock instanceof LiquidBlock))) {
                        item = this.inventory.getStackInSlot(0);
                        if (item.isCorrectToolForDrops(world.getBlockState(_tempPos)) && (Blocks.BEDROCK != _tempBlock)) {
                            if (!world.isClientSide()) {
                                ItemEntity entityToSpawn = new ItemEntity(world, x, (y + 1), z, (new ItemStack(_tempBlock)));
                                entityToSpawn.setPickUpDelay(10);
                                world.addFreshEntity(entityToSpawn);
                            }
                            world.destroyBlock(_tempPos, false);
                            if (this.inventory.getStackInSlot(0).hurt(1, new Random(), null)) {
                                this.inventory.getStackInSlot(0).shrink(1);
                                this.inventory.getStackInSlot(0).setDamageValue(0);
                            }
                            this.getTileData().putDouble("ElectronicPower", (electronicPower - 50));
                        }
                    } else {
                        if (world.getBlockState(_tempPos).getFluidState().isSource()) {
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
                                if (Blocks.WATER == (world.getFluidState(_tempPos).createLegacyBlock()).getBlock()) {
                                    this.inventory.setStackInSlot(slot, new ItemStack(Items.WATER_BUCKET, 1));
                                } else if (Blocks.LAVA == (world.getFluidState(_tempPos).createLegacyBlock()).getBlock()) {
                                    this.inventory.setStackInSlot(slot, new ItemStack(Items.LAVA_BUCKET, 1));
                                }
                                world.setBlock(_tempPos, Blocks.AIR.defaultBlockState(), 3);
                                this.getTileData().putDouble("ElectronicPower", (electronicPower - 50));
                            }
                        }
                    }
                }
            } else {
                sy = 1;
                while ((BlockInit.MINING_PIPE.get() == (world.getBlockState(new BlockPos(x, (y - (sy)), z)))
                        .getBlock())) {
                    sy = (sy) + 1;
                }
                BlockPos _tempPosNew = new BlockPos(x, (y - sy), z);
                Block _tempBlockNew = world.getBlockState(_tempPosNew).getBlock();
                if ((BlockInit.MINING_PIPE.get() != _tempBlockNew)
                        && (BlockInit.MINING_MACHINE.get() != (world.getBlockState(new BlockPos(x, ((y - (sy)) + 1), z))).getBlock())) {
                    world.setBlock(new BlockPos(x, ((y - (sy)) + 1), z), Blocks.AIR.defaultBlockState(), 3);
                }
            }

            this.setChanged();
            world.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        this.wait = compound.getInt("wait");
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundTag) compound.get("Inventory"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.putInt("wait", this.wait);
        compound.put("Inventory", inventory.serializeNBT());
    }
}
