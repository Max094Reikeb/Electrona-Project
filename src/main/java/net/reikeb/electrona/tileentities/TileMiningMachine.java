package net.reikeb.electrona.tileentities;

import net.minecraft.block.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.entity.player.*;
import net.minecraft.world.Containers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.level.Level;

import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.*;
import net.minecraftforge.items.*;

import net.reikeb.electrona.containers.MiningMachineContainer;
import net.reikeb.electrona.init.*;
import net.reikeb.electrona.utils.ItemHandler;

import static net.reikeb.electrona.init.TileEntityInit.*;

import java.util.Random;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileMiningMachine extends RandomizableContainerBlockEntity implements TickableBlockEntity {

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
    public Component getDisplayName() {
        return new TranslatableComponent("gui.electrona.mining_machine.name");
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("mining_machine");
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
    public AbstractContainerMenu createMenu(final int windowID, final Inventory playerInv, final Player playerIn) {
        return new MiningMachineContainer(windowID, playerInv, this);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new MiningMachineContainer(ContainerInit.MINING_MACHINE_CONTAINER.get(), id);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public void tick() {
        // We get the variables
        Level world = this.level;
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
                    if ((!(world.getBlockState(_tempPos)).getFluidState().isSource()) && (!(_tempBlock instanceof LiquidBlock))) {
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
    public void load(BlockState blockState, CompoundTag compound) {
        super.load(blockState, compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        this.wait = compound.getInt("wait");
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundTag) compound.get("Inventory"));
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
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

    public void dropItems(Level world, BlockPos pos) {
        for (int i = 0; i < 3; i++)
            if (!inventory.getStackInSlot(i).isEmpty()) {
                Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(i));
            }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 0, this.getUpdateTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(this.getBlockState(), pkt.getTag());
    }

    @Override
    public int getContainerSize() {
        return 3;
    }
}
