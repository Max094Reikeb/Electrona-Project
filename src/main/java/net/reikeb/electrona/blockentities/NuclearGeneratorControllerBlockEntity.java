package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.blocks.NuclearGeneratorController;
import net.reikeb.electrona.containers.NuclearGeneratorControllerContainer;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.init.SoundsInit;
import net.reikeb.electrona.misc.vm.EnergyFunction;
import net.reikeb.electrona.misc.vm.NuclearFunction;
import net.reikeb.maxilib.abs.AbstractEnergyBlockEntity;
import net.reikeb.maxilib.intface.FluidInterface;
import net.reikeb.maxilib.intface.IFluid;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static net.reikeb.electrona.init.BlockEntityInit.NUCLEAR_GENERATOR_CONTROLLER_BLOCK_ENTITY;

public class NuclearGeneratorControllerBlockEntity extends AbstractEnergyBlockEntity implements FluidInterface {

    public static final BlockEntityTicker<NuclearGeneratorControllerBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    private int temperature;
    private boolean powered;
    private boolean ubIn;
    private boolean alert;
    private boolean isOverCooler;
    private int posXUnder;
    private int posYUnder;
    private int posZUnder;
    private int wait;

    public NuclearGeneratorControllerBlockEntity(BlockPos pos, BlockState state) {
        super(NUCLEAR_GENERATOR_CONTROLLER_BLOCK_ENTITY.get(), pos, state, "nuclear_generator_controller", Electrona.MODID, 2);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new NuclearGeneratorControllerContainer(id, this.getBlockPos(), playerInventory, player);
    }

    public <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState state, T t) {
        if (level == null) return;
        ItemStack stackInSlot0 = this.inventory.getStackInSlot(0);

        BlockEntity blockEntityUnder = level.getBlockEntity(blockPos.below());
        Block blockUnder = level.getBlockState(blockPos.below()).getBlock();

        this.isOverCooler = blockUnder == BlockInit.COOLER.get();
        this.posXUnder = blockPos.below().getX();
        this.posYUnder = blockPos.below().getY();
        this.posZUnder = blockPos.below().getZ();

        this.setMaxStorage(10000);

        level.setBlockAndUpdate(blockPos, this.getBlockState()
                .setValue(NuclearGeneratorController.ACTIVATED, this.powered));

        if (blockEntityUnder instanceof CoolerBlockEntity coolerBlockEntity) {
            AtomicReference<ItemStack> stackInSlot1 = new AtomicReference<>();
            coolerBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                stackInSlot1.set(h.getStackInSlot(0));
            });

            int waterLevel = IFluid.getFluidAmount(coolerBlockEntity);
            int tankCapacity = IFluid.getTankCapacity(coolerBlockEntity);

            // Input slot - Handling slots
            if ((stackInSlot0.getItem() == Items.WATER_BUCKET)
                    && (waterLevel <= (tankCapacity - 1000)) && (blockUnder == BlockInit.COOLER.get())) {
                this.inventory.decrStackSize(0, 1);
                this.inventory.insertItem(0, new ItemStack(Items.BUCKET, 1), false);
                IFluid.fillWater(coolerBlockEntity, 1000);
            }

            if (blockUnder == BlockInit.COOLER.get()) {
                if (((stackInSlot1.get().getItem() == ItemInit.URANIUM_BAR.get())
                        || (stackInSlot1.get().getItem() == ItemInit.URANIUM_DUAL_BAR.get())
                        || (stackInSlot1.get().getItem() == ItemInit.URANIUM_QUAD_BAR.get()))) {
                    this.inventory.insertItem(1, stackInSlot1.get(), false);
                    this.ubIn = true;
                }
            }

            if (this.ubIn && (blockUnder != BlockInit.COOLER.get())) {
                this.ubIn = false;
                this.powered = false;
                this.inventory.decrStackSize(1, 1);
            }

            // Generation function
            NuclearFunction.nuclearGeneration(this, coolerBlockEntity, stackInSlot1.get());
        }

        if ((this.alert) && (level.getGameTime() % 20 == 0)) {
            level.playSound(null, blockPos, SoundsInit.NUCLEAR_GENERATOR_CONTROLLER_ALERT.get(),
                    SoundSource.BLOCKS, 0.6F, 1.0F);
        }

        // Transfer energy
        EnergyFunction.generatorTransferEnergy(level, blockPos, this, 10, true);

        this.setChanged();
        level.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(), 3);
    }

    public int getUnderWater() {
        BlockEntity underBlockEntity = this.getLevel().getBlockEntity(this.getBlockPos().below());
        if (underBlockEntity instanceof CoolerBlockEntity coolerBlockEntity) {
            return IFluid.getFluidAmount(coolerBlockEntity);
        }
        return 0;
    }

    public void setUnderWater(int amount) {
        BlockEntity underBlockEntity = this.getLevel().getBlockEntity(this.getBlockPos().below());
        if (!(underBlockEntity instanceof CoolerBlockEntity coolerBlockEntity)) return;
        int waterLevel = IFluid.getFluidAmount(coolerBlockEntity);
        IFluid.drainWater(coolerBlockEntity, waterLevel);
        IFluid.fillWater(coolerBlockEntity, amount);
    }

    public int getWaterLevel() {
        AtomicInteger amount = new AtomicInteger();
        this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                .ifPresent(cap -> amount.set(cap.getFluidInTank(1).getAmount()));
        return amount.get();
    }

    public void setWaterLevel(int amount) {
        this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                .ifPresent(cap -> cap.drain(getWaterLevel(), IFluidHandler.FluidAction.EXECUTE));
        this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                .ifPresent(cap -> cap.fill(new FluidStack(Fluids.WATER, amount), IFluidHandler.FluidAction.EXECUTE));
    }

    public int getMaxCapacity() {
        AtomicInteger capacity = new AtomicInteger();
        this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                .ifPresent(cap -> capacity.set(cap.getTankCapacity(1)));
        return capacity.get();
    }

    public int getTemperature() {
        return this.temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int isPowered() {
        return this.powered ? 1 : 0;
    }

    public void setPowered(int isPowered) {
        this.powered = (isPowered == 1);
    }

    public int areUbIn() {
        return this.ubIn ? 1 : 0;
    }

    public void setUbIn(int areUbIn) {
        this.ubIn = (areUbIn == 1);
    }

    public int isAlert() {
        return this.alert ? 1 : 0;
    }

    public void setAlert(int isAlert) {
        this.alert = (isAlert == 1);
    }

    public int isOverCooler() {
        return this.isOverCooler ? 1 : 0;
    }

    public void setOverCooler(int isOverCooler) {
        this.isOverCooler = (isOverCooler == 1);
    }

    public int getPosXUnder() {
        return this.posXUnder;
    }

    public void setPosXUnder(int posXUnder) {
        this.posXUnder = posXUnder;
    }

    public int getPosYUnder() {
        return this.posYUnder;
    }

    public void setPosYUnder(int posYUnder) {
        this.posYUnder = posYUnder;
    }

    public int getPosZUnder() {
        return this.posZUnder;
    }

    public void setPosZUnder(int posZUnder) {
        this.posZUnder = posZUnder;
    }

    public boolean getLogic() {
        return false;
    }

    public void setLogic(boolean logic) {
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.temperature = compound.getInt("temperature");
        this.powered = compound.getBoolean("powered");
        this.ubIn = compound.getBoolean("UBIn");
        this.alert = compound.getBoolean("alert");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("temperature", this.temperature);
        compound.putBoolean("powered", this.powered);
        compound.putBoolean("UBIn", this.ubIn);
        compound.putBoolean("alert", this.alert);
    }
}
