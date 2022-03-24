package net.reikeb.electrona.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
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

import static net.reikeb.electrona.init.TileEntityInit.TILE_CONVERTER;

public class TileConverter extends AbstractTileEntity {

    public static final BlockEntityTicker<TileConverter> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
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
    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int p_39284_) {
            switch (p_39284_) {
                case 0:
                    return (int) TileConverter.this.electronicPower;
                case 1:
                    AtomicInteger energy = new AtomicInteger();
                    TileConverter.this.getCapability(CapabilityEnergy.ENERGY, null)
                            .ifPresent(cap -> energy.set(cap.getEnergyStored()));
                    return energy.get();
                case 2:
                    return (int) TileConverter.this.vp;
                case 3:
                    return (TileConverter.this.toVP ? 1 : 0);
                case 4:
                    return (TileConverter.this.toOthers ? 1 : 0);
                default:
                    return 0;
            }
        }

        @Override
        public void set(int p_39285_, int p_39286_) {
            switch (p_39285_) {
                case 0:
                    TileConverter.this.electronicPower = p_39286_;
                case 1:
                    TileConverter.this.getCapability(CapabilityEnergy.ENERGY, null)
                            .ifPresent(cap -> cap.receiveEnergy(p_39286_, false));
                case 2:
                    TileConverter.this.vp = p_39286_;
                case 3:
                    TileConverter.this.toVP = (p_39286_ == 1);
                case 4:
                    TileConverter.this.toOthers = (p_39286_ == 1);
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    };
    private int wait;

    public TileConverter(BlockPos pos, BlockState state) {
        super(TILE_CONVERTER.get(), pos, state, 3);
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
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new ConverterContainer(id, player, this, dataAccess);
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