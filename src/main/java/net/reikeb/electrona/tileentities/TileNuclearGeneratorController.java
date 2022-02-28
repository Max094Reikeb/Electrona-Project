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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import net.reikeb.electrona.blocks.NuclearGeneratorController;
import net.reikeb.electrona.containers.NuclearGeneratorControllerContainer;
import net.reikeb.electrona.init.*;
import net.reikeb.electrona.misc.vm.*;

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
    public AbstractContainerMenu createMenu(final int windowID, final Inventory playerInv, final Player playerIn) {
        return new NuclearGeneratorControllerContainer(windowID, playerInv, this);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new NuclearGeneratorControllerContainer(ContainerInit.NUCLEAR_GENERATOR_CONTAINER.get(), id);
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
        if (world == null) return;
        ItemStack stackInSlot0 = this.inventory.getStackInSlot(0);

        BlockPos posUnder = new BlockPos(blockPos.getX(), (blockPos.getY() - 1), blockPos.getZ());
        BlockEntity tileUnder = world.getBlockEntity(posUnder);
        Block blockUnder = world.getBlockState(posUnder).getBlock();

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
        world.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(),
                Constants.BlockFlags.BLOCK_UPDATE);
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
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.putInt("temperature", this.temperature);
        compound.putBoolean("powered", this.powered);
        compound.putBoolean("UBIn", this.ubIn);
        compound.putBoolean("alert", this.alert);
        compound.put("Inventory", inventory.serializeNBT());
        return compound;
    }
}
