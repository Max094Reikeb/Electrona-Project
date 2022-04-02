package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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

import net.reikeb.electrona.containers.ConverterContainer;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.misc.vm.EnergyFunction;

import java.util.concurrent.atomic.AtomicInteger;

import static net.reikeb.electrona.init.BlockEntityInit.CONVERTER_BLOCK_ENTITY;

public class ConverterBlockEntity extends AbstractBlockEntity {

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
    public double electronicPower;
    private int maxStorage;
    private boolean toVP;
    private boolean toOthers;
    private double vp;
    private int wait;

    public ConverterBlockEntity(BlockPos pos, BlockState state) {
        super(CONVERTER_BLOCK_ENTITY.get(), pos, state, 3);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.electrona.el_converter.name");
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("el_converter");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new ConverterContainer(id, this.getBlockPos(), playerInventory, player);
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
        // We get the NBT Tags
        this.getTileData().putInt("MaxStorage", 10000);
        double electronicPower = this.getTileData().getDouble("ElectronicPower");
        boolean toVP = this.getTileData().getBoolean("toVP");
        boolean toOthers = this.getTileData().getBoolean("toOthers");

        if (world != null) { // Avoid NullPointerExceptions

            wait += 1;
            if (wait >= 15) {
                if (((!toVP) && (!toOthers)) || (toVP && toOthers)) {
                    this.getTileData().putBoolean("toVP", true);
                    this.getTileData().putBoolean("toOthers", false);
                }

                if (toVP) {
                    if (electronicPower >= 1) {
                        this.getTileData().putDouble("vp", (this.getTileData().getDouble("vp") + 3));
                        this.getTileData().putDouble("ElectronicPower", electronicPower - 1);
                    }
                } else if (toOthers) {
                    if (electronicPower >= 1) {
                        {
                            this.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(cap -> cap.receiveEnergy(3, false));
                        }
                        this.getTileData().putDouble("ElectronicPower", electronicPower - 1);
                    }
                }
                wait = 0;
            }

            // Output slot - Handling slots
            EnergyFunction.transferEnergyWithItemSlot(this.getTileData(), ItemInit.PORTABLE_BATTERY.get().asItem(), inventory, false, electronicPower, 0, 4);

            this.setChanged();
            world.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public int getElectronicPower() {
        return (int) this.electronicPower;
    }

    public void setElectronicPower(double electronicPower) {
        this.electronicPower = electronicPower;
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

    public int getVP() {
        return (int) this.vp;
    }

    public void setVP(int vp) {
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
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        this.toVP = compound.getBoolean("toVP");
        this.toOthers = compound.getBoolean("toOthers");
        this.vp = compound.getDouble("vp");
        this.wait = compound.getInt("wait");
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundTag) compound.get("Inventory"));
        }
        if (compound.contains("energyStorage")) {
            energyStorage.deserializeNBT(compound.get("energyStorage"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.putBoolean("toVP", this.toVP);
        compound.putBoolean("toOthers", this.toOthers);
        compound.putDouble("vp", this.vp);
        compound.putInt("wait", this.wait);
        compound.put("Inventory", inventory.serializeNBT());
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