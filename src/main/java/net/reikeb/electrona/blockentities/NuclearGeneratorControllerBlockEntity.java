package net.reikeb.electrona.blockentities;

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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.items.CapabilityItemHandler;

import net.reikeb.electrona.blocks.NuclearGeneratorController;
import net.reikeb.electrona.containers.NuclearGeneratorControllerContainer;
import net.reikeb.electrona.init.BlockInit;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.init.SoundsInit;
import net.reikeb.electrona.misc.vm.EnergyFunction;
import net.reikeb.electrona.misc.vm.FluidFunction;
import net.reikeb.electrona.misc.vm.NuclearFunction;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static net.reikeb.electrona.init.BlockEntityInit.NUCLEAR_GENERATOR_CONTROLLER_BLOCK_ENTITY;

public class NuclearGeneratorControllerBlockEntity extends AbstractBlockEntity {

    public static final BlockEntityTicker<NuclearGeneratorControllerBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    public double electronicPower;
    private int maxStorage;
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
        super(NUCLEAR_GENERATOR_CONTROLLER_BLOCK_ENTITY.get(), pos, state, 2);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.electrona.nuclear_generator_controller.name");
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("nuclear_generator_controller");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new NuclearGeneratorControllerContainer(id, this.getBlockPos(), playerInventory, player);
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
        if (world == null) return;
        ItemStack stackInSlot0 = this.inventory.getStackInSlot(0);

        BlockEntity tileUnder = world.getBlockEntity(blockPos.below());
        Block blockUnder = world.getBlockState(blockPos.below()).getBlock();

        this.isOverCooler = blockUnder == BlockInit.COOLER.get();
        this.posXUnder = blockPos.below().getX();
        this.posYUnder = blockPos.below().getY();
        this.posZUnder = blockPos.below().getZ();

        double electronicPower = this.getTileData().getDouble("ElectronicPower");
        int temperature = this.getTileData().getInt("temperature");
        boolean ubIn = this.getTileData().getBoolean("UBIn");
        this.getTileData().putInt("MaxStorage", 10000);

        world.setBlockAndUpdate(blockPos, this.getBlockState()
                .setValue(NuclearGeneratorController.ACTIVATED, this.getTileData().getBoolean("powered")));

        if (tileUnder instanceof CoolerBlockEntity) {
            AtomicReference<ItemStack> stackInSlot1 = new AtomicReference<>();
            tileUnder.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                stackInSlot1.set(h.getStackInSlot(0));
            });

            AtomicInteger waterLevel = FluidFunction.getFluidAmount(tileUnder);
            AtomicInteger tankCapacity = FluidFunction.getTankCapacity(tileUnder);

            // Input slot - Handling slots
            if ((stackInSlot0.getItem() == Items.WATER_BUCKET)
                    && (waterLevel.get() <= (tankCapacity.get() - 1000)) && (blockUnder == BlockInit.COOLER.get())) {
                this.inventory.decrStackSize(0, 1);
                this.inventory.insertItem(0, new ItemStack(Items.BUCKET, 1), false);
                FluidFunction.fillWater(tileUnder, 1000);
            }

            if (blockUnder == BlockInit.COOLER.get()) {
                if (((stackInSlot1.get().getItem() == ItemInit.URANIUM_BAR.get())
                        || (stackInSlot1.get().getItem() == ItemInit.URANIUM_DUAL_BAR.get())
                        || (stackInSlot1.get().getItem() == ItemInit.URANIUM_QUAD_BAR.get()))) {
                    this.inventory.insertItem(1, stackInSlot1.get(), false);
                    this.getTileData().putBoolean("UBIn", true);
                }
            }

            if (ubIn && (blockUnder != BlockInit.COOLER.get())) {
                this.getTileData().putBoolean("UBIn", false);
                this.getTileData().putBoolean("powered", false);
                this.inventory.decrStackSize(1, 1);
            }

            // Generation function
            NuclearFunction.nuclearGeneration(this, tileUnder, stackInSlot1.get(), this.inventory.getStackInSlot(1), electronicPower, temperature, waterLevel.get());
        }

        if ((this.getTileData().getBoolean("alert")) && (this.level.getGameTime() % 20 == 0)) {
            world.playSound(null, blockPos, SoundsInit.NUCLEAR_GENERATOR_CONTROLLER_ALERT.get(),
                    SoundSource.BLOCKS, 0.6F, 1.0F);
        }

        // Transfer energy
        EnergyFunction.generatorTransferEnergy(world, blockPos, Direction.values(), this.getTileData(), 10, electronicPower, true);

        this.setChanged();
        world.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(), 3);
    }

    public int getElectronicPower() {
        return (int) this.electronicPower;
    }

    public void setElectronicPower(double electronicPower) {
        this.electronicPower = electronicPower;
    }

    public int getUnderWater() {
        BlockEntity tileUnder = this.getLevel().getBlockEntity(this.getBlockPos().below());
        if (tileUnder instanceof CoolerBlockEntity) {
            return FluidFunction.getFluidAmount(tileUnder).get();
        }
        return 0;
    }

    public void setUnderWater(int amount) {
        BlockEntity tileUnder = this.getLevel().getBlockEntity(this.getBlockPos().below());
        if (!(tileUnder instanceof CoolerBlockEntity)) return;
        AtomicInteger waterLevel = FluidFunction.getFluidAmount(tileUnder);
        FluidFunction.drainWater(tileUnder, waterLevel.get());
        FluidFunction.fillWater(tileUnder, amount);
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

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        this.temperature = compound.getInt("temperature");
        this.powered = compound.getBoolean("powered");
        this.ubIn = compound.getBoolean("UBIn");
        this.alert = compound.getBoolean("alert");
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundTag) compound.get("Inventory"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.putInt("temperature", this.temperature);
        compound.putBoolean("powered", this.powered);
        compound.putBoolean("UBIn", this.ubIn);
        compound.putBoolean("alert", this.alert);
        compound.put("Inventory", inventory.serializeNBT());
    }
}