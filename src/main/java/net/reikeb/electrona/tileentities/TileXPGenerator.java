package net.reikeb.electrona.tileentities;

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
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import net.reikeb.electrona.blocks.XPGenerator;
import net.reikeb.electrona.containers.XPGeneratorContainer;
import net.reikeb.electrona.init.ContainerInit;
import net.reikeb.electrona.utils.ItemHandler;

import static net.reikeb.electrona.init.TileEntityInit.*;

public class TileXPGenerator extends LockableLootTileEntity implements ITickableTileEntity {

    private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
    private final ItemHandler inventory;

    public double electronicPower;
    private int maxStorage;
    private int wait;
    private int xpLevels;

    public TileXPGenerator() {
        super(TILE_XP_GENERATOR.get());

        this.inventory = new ItemHandler(1);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("electrona.xp_generator_gui.name");
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new StringTextComponent("xp_generator");
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
        return new XPGeneratorContainer(windowID, playerInv, this);
    }

    @Override
    public Container createMenu(int id, PlayerInventory player) {
        return new XPGeneratorContainer(ContainerInit.XP_GENERATOR_CONTAINER.get(), id);
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
        this.getTileData().putInt("MaxStorage", 10000);
        double electronicPower = this.getTileData().getDouble("ElectronicPower");
        int xpLevel = this.getTileData().getInt("XPLevels");
        int xp = this.getTileData().getInt("wait");

        if (world != null) { // Avoid NullPointerExceptions

            // Handle slot
            if ((electronicPower >= 0.8) && (this.inventory.getStackInSlot(0).getItem() == Items.EMERALD)) {
                xp += 1;
                this.getTileData().putInt("wait", xp);
                this.getTileData().putDouble("ElectronicPower", electronicPower - 0.8);
                if (xp >= 4800) {
                    this.inventory.decrStackSize(0, 1);
                    this.getTileData().putInt("XPLevels", xpLevel + 1);
                    this.getTileData().putInt("wait", 0);
                }
            } else {
                this.getTileData().putInt("wait", 0);
            }
            world.setBlockAndUpdate(blockPos, this.getBlockState()
                    .setValue(XPGenerator.ACTIVATED, (xpLevel > 0 || xp > 0)));

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
        this.xpLevels = compound.getInt("XPLevels");
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
        compound.putInt("XPLevels", this.xpLevels);
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
