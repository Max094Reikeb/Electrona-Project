package net.reikeb.electrona.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.*;
import net.minecraftforge.items.*;

import net.reikeb.electrona.containers.BiomassGeneratorContainer;
import net.reikeb.electrona.init.*;
import net.reikeb.electrona.misc.vm.EnergyFunction;
import net.reikeb.electrona.utils.*;

import static net.reikeb.electrona.init.TileEntityInit.*;

public class TileBiomassGenerator extends LockableLootTileEntity implements ITickableTileEntity {

    private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
    private final ItemHandler inventory;

    public double electronicPower;
    private int maxStorage;
    private int wait;

    public TileBiomassGenerator() {
        super(TILE_BIOMASS_GENERATOR.get());

        this.inventory = new ItemHandler(1);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gui.electrona.biomass_generator.name");
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new StringTextComponent("biomass_generator");
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
        return new BiomassGeneratorContainer(windowID, playerInv, this);
    }

    @Override
    public Container createMenu(int id, PlayerInventory player) {
        return new BiomassGeneratorContainer(ContainerInit.BIOMASS_GENERATOR_CONTAINER.get(), id);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public void tick() {
        // We get the variables
        World world = this.level;
        BlockPos blockPos = this.getBlockPos();

        // We get the NBT Tags
        this.getTileData().putInt("MaxStorage", 3000);
        double electronicPower = this.getTileData().getDouble("ElectronicPower");

        if (world != null) { // Avoid NullPointerExceptions

            // Handle slot
            if (ItemTags.getAllTags().getTagOrEmpty(new ResourceLocation(("forge:biomass").toLowerCase(java.util.Locale.ENGLISH)))
                    .contains(this.inventory.getStackInSlot(0).getItem()) && electronicPower < 3000) {
                wait += 1;
                if (wait >= 20) {
                    if (electronicPower <= 2990) {
                        this.getTileData().putDouble("ElectronicPower", electronicPower + 10);
                    } else {
                        this.getTileData().putDouble("ElectronicPower", 3000);
                    }
                    this.inventory.decrStackSize(0, 1);
                    ElectronaUtils.playSound(world, this.getBlockPos(), SoundsInit.BIOMASS_GENERATOR_ACTIVE.get(), SoundCategory.BLOCKS);
                    wait = 0;
                }
            } else {
                wait = 0;
            }

            // Transfer energy
            EnergyFunction.generatorTransferEnergy(world, blockPos, Direction.values(), this.getTileData(), 3, electronicPower, true);

            this.setChanged();
            world.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(),
                    Constants.BlockFlags.NOTIFY_NEIGHBORS);
        }
    }

    public final IItemHandlerModifiable getInventory() {
        return this.inventory;
    }

    @Override
    public void load(BlockState blockState, CompoundNBT compound) {
        super.load(blockState, compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        this.wait = compound.getInt("wait");
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundNBT) compound.get("Inventory"));
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.putInt("wait", this.wait);
        compound.put("Inventory", inventory.serializeNBT());
        return compound;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this.inventory));
    }

    public void dropItems(World world, BlockPos pos) {
        for (int i = 0; i < 1; i++)
            if (!inventory.getStackInSlot(i).isEmpty()) {
                InventoryHelper.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(i));
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
        return 1;
    }
}
