package net.reikeb.electrona.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.common.util.Constants;

import net.reikeb.electrona.containers.BatteryContainer;
import net.reikeb.electrona.init.ContainerInit;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.misc.vm.EnergyFunction;

import static net.reikeb.electrona.init.TileEntityInit.TILE_BATTERY;

public class TileBattery extends AbstractTileEntity {

    public double electronicPower;
    private int maxStorage;

    public TileBattery(BlockPos pos, BlockState state) {
        super(TILE_BATTERY.get(), pos, state, 2);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.electrona.battery.name");
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("battery");
    }

    @Override
    public AbstractContainerMenu createMenu(final int windowID, final Inventory playerInv, final Player playerIn) {
        return new BatteryContainer(windowID, playerInv, this);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new BatteryContainer(ContainerInit.BATTERY_CONTAINER.get(), id);
    }

    public void tick() {
        // We get the variables
        Level world = this.level;
        BlockPos blockPos = this.getBlockPos();

        // We get the NBT Tags
        this.getTileData().putInt("MaxStorage", 10000);
        double electronicPower = this.getTileData().getDouble("ElectronicPower");

        if (world != null) { // Avoid NullPointerExceptions

            // Input slots - Handling slots
            EnergyFunction.transferEnergyWithItemSlot(this.getTileData(), ItemInit.PORTABLE_BATTERY.get().asItem(), inventory, true, electronicPower, 1, 4);
            EnergyFunction.transferEnergyWithItemSlot(this.getTileData(), ItemInit.MECHANIC_WINGS.get().asItem(), inventory, true, electronicPower, 1, 8);
            EnergyFunction.transferEnergyWithItemSlot(this.getTileData(), ItemInit.PORTABLE_TELEPORTER.get().asItem(), inventory, true, electronicPower, 1, 8);

            // Output slot - Handling slots
            EnergyFunction.transferEnergyWithItemSlot(this.getTileData(), ItemInit.PORTABLE_BATTERY.get().asItem(), inventory, false, electronicPower, 0, 4);

            // We pass energy to blocks around (this part is common to all generators)
            EnergyFunction.generatorTransferEnergy(world, blockPos, Direction.values(), this.getTileData(), 6, electronicPower, false);

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
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundTag) compound.get("Inventory"));
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.put("Inventory", inventory.serializeNBT());
        return compound;
    }
}
