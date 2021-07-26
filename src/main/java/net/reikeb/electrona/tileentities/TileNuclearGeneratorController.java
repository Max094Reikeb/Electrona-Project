package net.reikeb.electrona.tileentities;

import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.Containers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.*;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.*;

import net.reikeb.electrona.blocks.NuclearGeneratorController;
import net.reikeb.electrona.containers.NuclearGeneratorControllerContainer;
import net.reikeb.electrona.init.*;
import net.reikeb.electrona.misc.vm.*;
import net.reikeb.electrona.utils.ItemHandler;

import static net.reikeb.electrona.init.TileEntityInit.*;

import java.util.concurrent.atomic.*;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileNuclearGeneratorController extends RandomizableContainerBlockEntity implements TickableBlockEntity {

    private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY);
    private final ItemHandler inventory;

    public double electronicPower;
    private int maxStorage;
    private int temperature;

    private boolean powered;
    private boolean ubIn;
    private boolean alert;

    public TileNuclearGeneratorController() {
        super(TILE_NUCLEAR_GENERATOR_CONTROLLER.get());

        this.inventory = new ItemHandler(2);
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
    protected NonNullList<ItemStack> getItems() {
        return this.stacks;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> stacks) {
        this.stacks = stacks;
    }

    @Override
    public AbstractContainerMenu createMenu(final int windowID, final Inventory playerInv, final Player playerIn) {
        return new NuclearGeneratorControllerContainer(windowID, playerInv, this);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new NuclearGeneratorControllerContainer(ContainerInit.NUCLEAR_GENERATOR_CONTAINER.get(), id);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public void tick() {
        Level world = this.level;
        if (world == null) return;
        BlockPos blockPos = this.getBlockPos();
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

            AtomicInteger waterLevel = new AtomicInteger();
            tileUnder.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).ifPresent(cap -> waterLevel.set(cap.getFluidInTank(1).getAmount()));
            AtomicInteger tankCapacity = new AtomicInteger();
            tileUnder.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).ifPresent(cap -> tankCapacity.set(cap.getTankCapacity(1)));

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

    public final IItemHandlerModifiable getInventory() {
        return this.inventory;
    }

    @Override
    public void load(BlockState blockState, CompoundTag compound) {
        super.load(blockState, compound);
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

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this.inventory));
    }

    public void dropItems(Level world, BlockPos pos) {
        for (int i = 0; i < 2; i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                if (!(this.getTileData().getBoolean("UBIn") && (i == 1))) {
                    Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(i));
                }
            }
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 0, this.getUpdateTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(this.getBlockState(), pkt.getTag());
    }

    @Override
    public int getContainerSize() {
        return 2;
    }
}
