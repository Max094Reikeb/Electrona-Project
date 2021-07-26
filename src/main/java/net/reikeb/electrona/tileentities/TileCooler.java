package net.reikeb.electrona.tileentities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.Containers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;

import net.reikeb.electrona.utils.ItemHandler;

import static net.reikeb.electrona.init.TileEntityInit.*;

public class TileCooler extends BlockEntity {

    private final ItemHandler inventory;

    public TileCooler() {
        super(TILE_COOLER.get());

        this.inventory = new ItemHandler(1);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public void load(BlockState blockState, CompoundTag compound) {
        super.load(blockState, compound);
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundTag) compound.get("Inventory"));
        }
        if (compound.get("fluidTank") != null)
            CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(fluidTank, null, compound.get("fluidTank"));
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        compound.put("Inventory", inventory.serializeNBT());
        compound.put("fluidTank", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(fluidTank, null));
        return compound;
    }

    private final FluidTank fluidTank = new FluidTank(10000, fs -> {
        return fs.getFluid() == Fluids.WATER;
    }) {
        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            setChanged();
            level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 2);
        }
    };

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> fluidTank).cast();
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this.inventory));
    }

    public void dropItems(Level world, BlockPos pos) {
        for (int i = 0; i < 1; i++)
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
}
