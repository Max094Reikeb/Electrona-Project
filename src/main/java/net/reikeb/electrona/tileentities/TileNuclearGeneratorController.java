package net.reikeb.electrona.tileentities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import net.reikeb.electrona.blocks.NuclearGeneratorController;
import net.reikeb.electrona.containers.NuclearGeneratorControllerContainer;
import net.reikeb.electrona.init.*;
import net.reikeb.electrona.misc.vm.*;
import net.reikeb.electrona.utils.ItemHandler;
import net.reikeb.electrona.world.gamerules.DoBlackholesExist;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static net.reikeb.electrona.init.TileEntityInit.*;

public class TileNuclearGeneratorController extends LockableLootTileEntity implements ITickableTileEntity {

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
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("electrona.nuclear_generator_controller_gui.name");
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new StringTextComponent("nuclear_generator_controller");
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
    public Container createMenu(final int windowID, final PlayerInventory playerInv, final PlayerEntity playerIn) {
        return new NuclearGeneratorControllerContainer(windowID, playerInv, this);
    }

    @Override
    public Container createMenu(int id, PlayerInventory player) {
        return new NuclearGeneratorControllerContainer(ContainerInit.NUCLEAR_GENERATOR_CONTAINER.get(), id);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public void tick() {
        World world = this.level;
        if (world == null) return;
        BlockPos blockPos = this.getBlockPos();
        ItemStack stackInSlot0 = this.inventory.getStackInSlot(0);

        BlockPos posUnder = new BlockPos(blockPos.getX(), (blockPos.getY() - 1), blockPos.getZ());
        TileEntity tileUnder = world.getBlockEntity(posUnder);
        Block blockUnder = world.getBlockState(posUnder).getBlock();

        double electronicPower = this.getTileData().getDouble("ElectronicPower");
        int temperature = this.getTileData().getInt("temperature");
        boolean ubIn = this.getTileData().getBoolean("UBIn");
        this.getTileData().putInt("MaxStorage", 10000);

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
                    && (waterLevel.get() < (tankCapacity.get() - 1000)) && (blockUnder == BlockInit.COOLER.get())) {
                this.inventory.decrStackSize(0, 1);
                this.inventory.insertItem(0, new ItemStack(Items.BUCKET, 1), false);
                FluidFunction.fillWater(tileUnder, 1000);
            }

            // Generation function
            NuclearFunction.nuclearGeneration(this, tileUnder, stackInSlot1.get(), this.inventory.getStackInSlot(1), electronicPower, temperature, waterLevel.get());

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
        }

        if ((this.getTileData().getBoolean("alert")) && (this.level.getGameTime() % 20 == 0)) {
            if (this.level.isClientSide) {
                this.level.playLocalSound(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(),
                        SoundsInit.NUCLEAR_GENERATOR_CONTROLLER_ALERT.get(), SoundCategory.BLOCKS, 1F, 1F, false);
            } else {
                this.level.playSound(null, this.getBlockPos(), SoundsInit.NUCLEAR_GENERATOR_CONTROLLER_ALERT.get(), SoundCategory.BLOCKS, 1F, 1F);
            }
        }

        // Transfer energy
        EnergyFunction.generatorTransferEnergy(world, blockPos, Direction.values(), this.getTileData(), 10, electronicPower, true);

        world.setBlockAndUpdate(blockPos, this.getBlockState()
                .setValue(NuclearGeneratorController.ACTIVATED, this.getTileData().getBoolean("powered")));

        this.setChanged();
        world.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(),
                Constants.BlockFlags.BLOCK_UPDATE);
    }

    public final IItemHandlerModifiable getInventory() {
        return this.inventory;
    }

    public void explodes() {
        this.level.explode(null, this.getBlockPos().getX(), this.getBlockPos().getY(),
                this.getBlockPos().getZ(), 20, Explosion.Mode.BREAK);
        if ((Math.random() < 0.25) && this.level.getLevelData().getGameRules().getBoolean(DoBlackholesExist.DO_BLACK_HOLES_EXIST)) {
            this.level.setBlockAndUpdate(this.getBlockPos(), BlockInit.SINGULARITY.get().defaultBlockState());
            // Advancement "I am... Inevitable!"
        }
    }

    @Override
    public void load(BlockState blockState, CompoundNBT compound) {
        super.load(blockState, compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        this.temperature = compound.getInt("temperature");
        this.powered = compound.getBoolean("powered");
        this.ubIn = compound.getBoolean("UBIn");
        this.alert = compound.getBoolean("alert");
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundNBT) compound.get("Inventory"));
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
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

    public void dropItems(World world, BlockPos pos) {
        for (int i = 0; i < 2; i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                if (!(this.getTileData().getBoolean("UBIn") && (i == 1))) {
                    InventoryHelper.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(i));
                }
            }
        }
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 0, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.load(this.getBlockState(), pkt.getTag());
    }

    @Override
    public int getContainerSize() {
        return 2;
    }
}
