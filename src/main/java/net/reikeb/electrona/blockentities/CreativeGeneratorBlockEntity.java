package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.reikeb.electrona.misc.vm.EnergyFunction;
import net.reikeb.maxilib.intface.EnergyInterface;
import net.reikeb.maxilib.inventory.ItemHandler;

import javax.annotation.Nullable;
import java.util.Objects;

import static net.reikeb.electrona.init.BlockEntityInit.CREATIVE_GENERATOR_BLOCK_ENTITY;

public class CreativeGeneratorBlockEntity extends BlockEntity implements EnergyInterface {

    public static final BlockEntityTicker<CreativeGeneratorBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    private final EnergyStorage energyStorage = new EnergyStorage(999999999, 999999999, 999999999, 999999999) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int retval = super.receiveEnergy(maxReceive, simulate);
            if (!simulate) {
                setChanged();
                level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 2);
            }
            return retval;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            int retval = super.extractEnergy(maxExtract, simulate);
            if (!simulate) {
                setChanged();
                level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 2);
            }
            return retval;
        }
    };
    private double electronicPower;
    private int maxStorage;

    public CreativeGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(CREATIVE_GENERATOR_BLOCK_ENTITY.get(), pos, state);
    }

    public <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState state, T t) {
        this.setMaxEnergy(999999999);
        this.setEnergy(999999999);

        if (level == null) return;

        this.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(cap ->
                cap.receiveEnergy(999999999, false));

        // We pass energy to blocks around (this part is common to all generators)
        EnergyFunction.generatorTransferEnergy(level, blockPos, this, 20, true);
    }

    public ItemHandler getItemInventory() {
        return null;
    }

    public void setHundredEnergy(int hundredEnergy) {
        this.electronicPower = hundredEnergy / 100.0;
    }

    public double getEnergy() {
        return this.electronicPower;
    }

    public void setEnergy(double energy) {
        this.electronicPower = energy;
    }

    public int getMaxEnergy() {
        return this.maxStorage;
    }

    public void setMaxEnergy(int maxEnergy) {
        this.maxStorage = maxEnergy;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        if (compound.contains("energyStorage")) {
            energyStorage.deserializeNBT(compound.get("energyStorage"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.put("energyStorage", energyStorage.serializeNBT());
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == CapabilityEnergy.ENERGY)
            return LazyOptional.of(() -> energyStorage).cast();
        return super.getCapability(cap, side);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        CompoundTag nbt = new CompoundTag();
        saveAdditional(nbt);
        return ClientboundBlockEntityDataPacket.create(this, blockEntity -> nbt);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(Objects.requireNonNull(pkt.getTag()));
    }
}
