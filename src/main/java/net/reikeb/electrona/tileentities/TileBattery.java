package net.reikeb.electrona.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import net.reikeb.electrona.setup.RegistryHandler;
import net.reikeb.electrona.utils.ElectronaUtils;
import static net.reikeb.electrona.setup.RegistryHandler.TILE_BATTERY;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileBattery extends TileEntity implements ITickableTileEntity {

    public ItemStackHandler itemHandler = createHandler();

    // Never create lazy optionals in getCapability. Always place them as fields in the tile entity:
    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    private int ElectronicPower;
    private int MaxStorage;

    public TileBattery() {
        super(TILE_BATTERY.get());
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        handler.invalidate();
    }

    @Override
    public void tick() {
        // We get the variables
        World world = this.level;
        int x = this.worldPosition.getX();
        int y = this.worldPosition.getY();
        int z = this.worldPosition.getZ();
        BlockPos blockPos = this.getBlockPos();

        // We get the NBT Tags
        this.getTileData().putDouble("MaxStorage", 10000);
        double electronicPower = this.getTileData().getDouble("ElectronicPower");

        if (world != null) { // Avoid NullPointerExceptions

            // Slot handling
            ItemStack stackInSlot0 = itemHandler.getStackInSlot(0);
            ItemStack stackInSlot1 = itemHandler.getStackInSlot(1);

            double epStack0 = stackInSlot0.getOrCreateTag().getDouble("ElectronicPower");
            double epStack1 = stackInSlot1.getOrCreateTag().getDouble("ElectronicPower");

            if (stackInSlot1.getItem() == RegistryHandler.PORTABLE_BATTERY.get().asItem()) {
                if (electronicPower > 0.2) {
                    stackInSlot1.getOrCreateTag().putDouble("ElectronicPower", (epStack1 + 0.2));
                    this.getTileData().putDouble("ElectronicPower", (electronicPower - 0.2));
                } else if ((electronicPower <= 0.2) && (electronicPower > 0)) {
                    stackInSlot1.getOrCreateTag().putDouble("ElectronicPower", (epStack1 + 0.05));
                    this.getTileData().putDouble("ElectronicPower", (electronicPower - 0.05));
                }
            }
            if (stackInSlot1.getItem() == RegistryHandler.MECHANIC_WINGS.get().asItem()) {
                if (electronicPower > 0.4) {
                    stackInSlot1.getOrCreateTag().putDouble("ElectronicPower", (epStack1 + 0.4));
                    this.getTileData().putDouble("ElectronicPower", (electronicPower - 0.4));
                } else if ((electronicPower <= 0.4) && (electronicPower > 0)) {
                    stackInSlot1.getOrCreateTag().putDouble("ElectronicPower", (epStack1 + 0.05));
                    this.getTileData().putDouble("ElectronicPower", (electronicPower - 0.05));
                }
            }
            if (stackInSlot0.getItem() == RegistryHandler.PORTABLE_BATTERY.get().asItem()) {
                if (epStack0 > 0.2) {
                    stackInSlot0.getOrCreateTag().putDouble("ElectronicPower", (epStack0 - 0.2));
                    if (!world.isClientSide()) {
                        this.getTileData().putDouble("ElectronicPower", (electronicPower + 0.2));
                    }
                } else if ((epStack0 <= 0.2) && (epStack0 > 0)) {
                    stackInSlot0.getOrCreateTag().putDouble("ElectronicPower", (epStack0 - 0.05));
                    if (!world.isClientSide()) {
                        this.getTileData().putDouble("ElectronicPower", (electronicPower + 0.05));
                    }
                }
            }

            // We pass energy to blocks around (this part is common to all generators)
            ElectronaUtils.generatorTransferEnergy(world, blockPos, Direction.values(), this.getTileData(), 6, electronicPower, false);
        }
    }

    @Override
    public void load(BlockState blockState, CompoundNBT compound) {
        super.load(blockState, compound);
        ElectronicPower = compound.getInt("ElectronicPower");
        MaxStorage = compound.getInt("MaxStorage");
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound = super.save(compound);
        compound.putInt("ElectronicPower", ElectronicPower);
        compound.putInt("MaxStorage", MaxStorage);
        return compound;
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(2) {

            @Override
            protected void onContentsChanged(int slot) {
                // To make sure the TE persists when the chunk is saved later we need to
                // mark it dirty every time the item handler changes
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(capability, facing);
    }

    public void dropItems(World world, BlockPos pos) {
        for (int i = 0; i < 2; i++)
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                InventoryHelper.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemHandler.getStackInSlot(i));
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
}
