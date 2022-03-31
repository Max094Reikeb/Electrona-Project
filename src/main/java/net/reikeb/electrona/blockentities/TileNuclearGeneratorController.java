package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
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

import static net.reikeb.electrona.init.TileEntityInit.TILE_NUCLEAR_GENERATOR_CONTROLLER;

public class TileNuclearGeneratorController extends AbstractTileEntity {

    public static final BlockEntityTicker<TileNuclearGeneratorController> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
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
    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int p_39284_) {
            switch (p_39284_) {
                case 0:
                    return (int) TileNuclearGeneratorController.this.electronicPower;
                case 1:
                    BlockEntity tileUnder = TileNuclearGeneratorController.this.getLevel()
                            .getBlockEntity(TileNuclearGeneratorController.this.getBlockPos().below());
                    AtomicInteger underWater = new AtomicInteger();
                    if (tileUnder instanceof TileCooler) {
                        tileUnder.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                                .ifPresent(cap -> underWater.set(cap.getFluidInTank(1).getAmount()));
                        return underWater.get();
                    }
                    return 0;
                case 2:
                    return TileNuclearGeneratorController.this.temperature;
                case 3:
                    return TileNuclearGeneratorController.this.powered ? 1 : 0;
                case 4:
                    return TileNuclearGeneratorController.this.ubIn ? 1 : 0;
                case 5:
                    return TileNuclearGeneratorController.this.alert ? 1 : 0;
                case 6:
                    return TileNuclearGeneratorController.this.isOverCooler ? 1 : 0;
                case 7:
                    return TileNuclearGeneratorController.this.posXUnder;
                case 8:
                    return TileNuclearGeneratorController.this.posYUnder;
                case 9:
                    return TileNuclearGeneratorController.this.posZUnder;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int p_39285_, int p_39286_) {
            switch (p_39285_) {
                case 0:
                    TileNuclearGeneratorController.this.electronicPower = p_39286_;
                case 2:
                    TileNuclearGeneratorController.this.temperature = p_39286_;
                case 3:
                    TileNuclearGeneratorController.this.powered = (p_39286_ == 1);
                case 4:
                    TileNuclearGeneratorController.this.ubIn = (p_39286_ == 1);
                case 5:
                    TileNuclearGeneratorController.this.alert = (p_39286_ == 1);
                case 6:
                    TileNuclearGeneratorController.this.isOverCooler = (p_39286_ == 1);
                case 7:
                    TileNuclearGeneratorController.this.posXUnder = p_39286_;
                case 8:
                    TileNuclearGeneratorController.this.posYUnder = p_39286_;
                case 9:
                    TileNuclearGeneratorController.this.posZUnder = p_39286_;
            }
        }

        @Override
        public int getCount() {
            return 10;
        }
    };
    private int wait;

    public TileNuclearGeneratorController(BlockPos pos, BlockState state) {
        super(TILE_NUCLEAR_GENERATOR_CONTROLLER.get(), pos, state, 2);
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
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new NuclearGeneratorControllerContainer(id, player, this, dataAccess);
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

        if (tileUnder instanceof TileCooler) {
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
