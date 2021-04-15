package net.reikeb.electrona.tileentities;

import net.minecraft.block.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.*;
import net.minecraftforge.items.*;

import net.reikeb.electrona.containers.MiningMachineContainer;
import net.reikeb.electrona.init.*;
import net.reikeb.electrona.utils.ItemHandler;

import static net.reikeb.electrona.init.TileEntityInit.*;

import java.util.Random;

public class TileMiningMachine extends LockableLootTileEntity implements ITickableTileEntity {

    private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
    private final ItemHandler inventory;

    public double electronicPower;
    private int maxStorage;
    private int wait;

    public TileMiningMachine() {
        super(TILE_MINING_MACHINE.get());

        this.inventory = new ItemHandler(3);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gui.electrona.mining_machine.name");
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new StringTextComponent("mining_machine");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.stacks;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> stacks) {
        this.stacks = stacks;
    }

    @Override
    public Container createMenu(final int windowID, final PlayerInventory playerInv, final PlayerEntity playerIn) {
        return new MiningMachineContainer(windowID, playerInv, this);
    }

    @Override
    public Container createMenu(int id, PlayerInventory player) {
        return new MiningMachineContainer(ContainerInit.MINING_MACHINE_CONTAINER.get(), id);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public void tick() {
        // We get the variables
        World world = this.level;
        BlockPos blockPos = this.getBlockPos();

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
                    if ((!(world.getBlockState(_tempPos)).getFluidState().isSource()) && (!(_tempBlock instanceof FlowingFluidBlock))) {
                        item = this.inventory.getStackInSlot(0);
                        if ((item.getHarvestLevel(ToolType.PICKAXE, null, world.getBlockState(_tempPos)) >=
                                (world.getBlockState(_tempPos)).getHarvestLevel()) && (Blocks.BEDROCK != _tempBlock)) {
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
            world.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(),
                    Constants.BlockFlags.NOTIFY_NEIGHBORS);
        }
    }

    public final IItemHandlerModifiable getInventory() {
        return this.inventory;
    }

    @Override
    public void load(BlockState blockState, CompoundNBT compound) {
        super.load(blockState, compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        this.wait = compound.getInt("wait");
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundNBT) compound.get("Inventory"));
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.putInt("wait", this.wait);
        compound.put("Inventory", inventory.serializeNBT());
        return compound;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this.inventory));
    }

    public void dropItems(World world, BlockPos pos) {
        for (int i = 0; i < 3; i++)
            if (!inventory.getStackInSlot(i).isEmpty()) {
                InventoryHelper.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(i));
            }
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 0, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.load(this.getBlockState(), pkt.getTag());
    }

    @Override
    public int getContainerSize() {
        return 3;
    }
}
