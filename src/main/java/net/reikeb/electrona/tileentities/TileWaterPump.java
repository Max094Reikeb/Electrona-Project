package net.reikeb.electrona.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;

import net.reikeb.electrona.blocks.WaterPump;
import net.reikeb.electrona.containers.WaterPumpContainer;
import net.reikeb.electrona.init.ContainerInit;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.init.SoundsInit;
import net.reikeb.electrona.misc.vm.EnergyFunction;
import net.reikeb.electrona.misc.vm.FluidFunction;

import java.util.concurrent.atomic.AtomicInteger;

import static net.reikeb.electrona.init.TileEntityInit.TILE_WATER_PUMP;

public class TileWaterPump extends AbstractTileEntity {

    public double electronicPower;
    private int maxStorage;
    private boolean isOn;
    private int wait;

    public TileWaterPump(BlockPos pos, BlockState state) {
        super(TILE_WATER_PUMP.get(), pos, state, 2);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.electrona.water_pump.name");
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("water_pump");
    }

    @Override
    public AbstractContainerMenu createMenu(final int windowID, final Inventory playerInv, final Player playerIn) {
        return new WaterPumpContainer(windowID, playerInv, this);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new WaterPumpContainer(ContainerInit.WATER_PUMP_CONTAINER.get(), id);
    }

    public void tick() {
        // We get the variables
        Level world = this.level;
        BlockPos blockPos = this.getBlockPos();
        BlockPos posUnder = new BlockPos(blockPos.getX(), (blockPos.getY() - 1), blockPos.getZ());

        // We get the NBT Tags
        this.getTileData().putInt("MaxStorage", 1000);
        double electronicPower = this.getTileData().getDouble("ElectronicPower");
        boolean isOn = this.getTileData().getBoolean("isOn");

        AtomicInteger waterLevel = new AtomicInteger();
        this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).ifPresent(cap -> waterLevel.set(cap.getFluidInTank(1).getAmount()));
        AtomicInteger tankCapacity = new AtomicInteger();
        this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).ifPresent(cap -> tankCapacity.set(cap.getTankCapacity(1)));

        if (world != null) { // Avoid NullPointerExceptions

            world.setBlockAndUpdate(blockPos, this.getBlockState().setValue(WaterPump.PUMPING, isOn));

            // Pump water
            if (isOn) {
                wait += 1;
                if (wait >= 15) {
                    if (electronicPower >= 20
                            && Blocks.WATER == world.getBlockState(posUnder).getBlock()) {
                        if (tankCapacity.get() >= (waterLevel.get() + 100)) {
                            FluidFunction.fillWater(this, 100);
                            electronicPower -= 20;
                            world.setBlockAndUpdate(posUnder, Blocks.AIR.defaultBlockState());
                            world.playSound(null, this.getBlockPos(), SoundsInit.WATER_PUMPING.get(), SoundSource.BLOCKS, 0.6F, 1.0F);
                        } else if (tankCapacity.get() >= (waterLevel.get() + 50)) {
                            FluidFunction.fillWater(this, 50);
                            electronicPower -= 10;
                            world.setBlockAndUpdate(posUnder, Blocks.AIR.defaultBlockState());
                            world.playSound(null, this.getBlockPos(), SoundsInit.WATER_PUMPING.get(), SoundSource.BLOCKS, 0.6F, 1.0F);
                        } else if (tankCapacity.get() >= (waterLevel.get() + 10)) {
                            FluidFunction.fillWater(this, 10);
                            electronicPower -= 2;
                            world.setBlockAndUpdate(posUnder, Blocks.AIR.defaultBlockState());
                            world.playSound(null, this.getBlockPos(), SoundsInit.WATER_PUMPING.get(), SoundSource.BLOCKS, 0.6F, 1.0F);
                        } else if (tankCapacity.get() > waterLevel.get()) {
                            FluidFunction.fillWater(this, 1);
                            electronicPower -= 0.2;
                            world.setBlockAndUpdate(posUnder, Blocks.AIR.defaultBlockState());
                            world.playSound(null, this.getBlockPos(), SoundsInit.WATER_PUMPING.get(), SoundSource.BLOCKS, 0.6F, 1.0F);
                        } else {
                            isOn = false;
                        }
                        this.getTileData().putDouble("ElectronicPower", electronicPower);
                    } else if (electronicPower < 20) {
                        isOn = false;
                    }
                    wait = 0;
                }
            } else {
                wait = 0;
            }
            this.getTileData().putBoolean("isOn", isOn);

            // Input slot - Handling slots
            if ((this.inventory.getStackInSlot(0).getItem() == Items.BUCKET)
                    && (waterLevel.get() >= 1000)) {
                this.inventory.decrStackSize(0, 1);
                this.inventory.insertItem(0, new ItemStack(Items.WATER_BUCKET, 1), false);
                FluidFunction.drainWater(this, 1000);
            }

            // Output slot - Handling slots
            EnergyFunction.transferEnergyWithItemSlot(this.getTileData(), ItemInit.PORTABLE_BATTERY.get().asItem(), inventory, false, electronicPower, 1, 4);

            // We pass water to blocks around
            FluidFunction.generatorTransferFluid(world, blockPos, Direction.values(), this, waterLevel.get(), 100);

            this.setChanged();
            world.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(),
                    Constants.BlockFlags.NOTIFY_NEIGHBORS);
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        this.isOn = compound.getBoolean("isOn");
        this.wait = compound.getInt("wait");
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundTag) compound.get("Inventory"));
        }
        if (compound.get("fluidTank") != null) {
            fluidTank.readFromNBT((CompoundTag) compound.get("fluidTank"));
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.putBoolean("isOn", this.isOn);
        compound.putInt("wait", this.wait);
        compound.put("Inventory", inventory.serializeNBT());
        fluidTank.writeToNBT(compound);
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
}
