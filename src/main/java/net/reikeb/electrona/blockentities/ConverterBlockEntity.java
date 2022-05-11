package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.ConverterContainer;
import net.reikeb.electrona.misc.vm.EnergyFunction;
import net.reikeb.maxilib.abs.AbstractEnergyBlockEntity;
import net.reikeb.maxilib.intface.IEnergy;

import java.util.concurrent.atomic.AtomicInteger;

import static net.reikeb.electrona.init.BlockEntityInit.CONVERTER_BLOCK_ENTITY;

public class ConverterBlockEntity extends AbstractEnergyBlockEntity {

    public static final BlockEntityTicker<ConverterBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    private final EnergyStorage energyStorage = new EnergyStorage(10000, 40, 40, 0) {
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
    private boolean toVP;
    private boolean toOthers;
    private double vp;
    private int wait;

    public ConverterBlockEntity(BlockPos pos, BlockState state) {
        super(CONVERTER_BLOCK_ENTITY.get(), pos, state, "el_converter", Electrona.MODID, 3);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new ConverterContainer(id, this.getBlockPos(), playerInventory, player);
    }

    public <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState state, T t) {
        this.setMaxStorage(10000);

        if (level == null) return;

        wait += 1;
        if (wait >= 15) {
            if (((!this.toVP) && (!this.toOthers)) || (this.toVP && this.toOthers)) {
                this.toVP = true;
                this.toOthers = false;
            }

            if (this.toVP) {
                if (this.getElectronicPower() >= 1) {
                    this.vp += 3;
                    IEnergy.drainEnergy(this, 1);
                }
            } else {
                if (this.getElectronicPower() >= 1) {
                    this.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(cap -> cap.receiveEnergy(3, false));
                    IEnergy.drainEnergy(this, 1);
                }
            }
            wait = 0;
        }

        // Output slot - Handling slots
        EnergyFunction.transferEnergyWithItemSlot(this, false, 0, 4);

        this.setChanged();
        level.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(), 3);
    }

    public int getForgeEnergy() {
        AtomicInteger energy = new AtomicInteger();
        this.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(cap -> energy.set(cap.getEnergyStored()));
        return energy.get();
    }

    public void setForgeEnergy(int energy) {
        AtomicInteger energyStored = new AtomicInteger();
        this.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(cap -> energyStored.set(cap.getEnergyStored()));
        this.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(cap -> cap.extractEnergy(energyStored.get(), false));
        this.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(cap -> cap.receiveEnergy(energy, false));
    }

    public int getVPTimesHundred() {
        return (int) (this.vp * 100);
    }

    public void setVPTimesHundred(int vpTimesHundred) {
        this.vp = vpTimesHundred / 100.0;
    }

    public double getVP() {
        return this.vp;
    }

    public void setVP(double vp) {
        this.vp = vp;
    }

    public int isToVP() {
        return this.toVP ? 1 : 0;
    }

    public void setToVP(int vp) {
        this.toVP = vp == 1;
    }

    public int isToOthers() {
        return this.toOthers ? 1 : 0;
    }

    public void setToOthers(int toOthers) {
        this.toOthers = toOthers == 1;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.toVP = compound.getBoolean("toVP");
        this.toOthers = compound.getBoolean("toOthers");
        this.vp = compound.getDouble("vp");
        this.wait = compound.getInt("wait");
        if (compound.contains("energyStorage")) {
            energyStorage.deserializeNBT(compound.get("energyStorage"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putBoolean("toVP", this.toVP);
        compound.putBoolean("toOthers", this.toOthers);
        compound.putDouble("vp", this.vp);
        compound.putInt("wait", this.wait);
        compound.put("energyStorage", energyStorage.serializeNBT());
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == CapabilityEnergy.ENERGY)
            return LazyOptional.of(() -> energyStorage).cast();
        if (!this.remove && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> this.inventory).cast();
        return super.getCapability(cap, side);
    }
}