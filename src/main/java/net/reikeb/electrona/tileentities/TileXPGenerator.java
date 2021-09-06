package net.reikeb.electrona.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.common.util.Constants;

import net.reikeb.electrona.blocks.XPGenerator;
import net.reikeb.electrona.containers.XPGeneratorContainer;
import net.reikeb.electrona.init.ContainerInit;

import static net.reikeb.electrona.init.TileEntityInit.TILE_XP_GENERATOR;

public class TileXPGenerator extends AbstractTileEntity {

    public static final BlockEntityTicker<TileXPGenerator> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    public double electronicPower;
    private int maxStorage;
    private int wait;
    private int xpLevels;

    public TileXPGenerator(BlockPos pos, BlockState state) {
        super(TILE_XP_GENERATOR.get(), pos, state, 1);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.electrona.xp_generator.name");
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("xp_generator");
    }

    @Override
    public AbstractContainerMenu createMenu(final int windowID, final Inventory playerInv, final Player playerIn) {
        return new XPGeneratorContainer(windowID, playerInv, this);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new XPGeneratorContainer(ContainerInit.XP_GENERATOR_CONTAINER.get(), id);
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
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

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        this.xpLevels = compound.getInt("XPLevels");
        this.wait = compound.getInt("wait");
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundTag) compound.get("Inventory"));
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.putInt("XPLevels", this.xpLevels);
        compound.putInt("wait", this.wait);
        compound.put("Inventory", inventory.serializeNBT());
        return compound;
    }
}
