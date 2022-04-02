package net.reikeb.electrona.blockentities;

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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.containers.BatteryContainer;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.misc.vm.EnergyFunction;

import static net.reikeb.electrona.init.BlockEntityInit.BATTERY_BLOCK_ENTITY;

public class BatteryBlockEntity extends AbstractBlockEntity {

    public static final BlockEntityTicker<BatteryBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    public double electronicPower;
    private int maxStorage;

    public BatteryBlockEntity(BlockPos pos, BlockState state) {
        super(BATTERY_BLOCK_ENTITY.get(), pos, state, 2);
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
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new BatteryContainer(id, this.getBlockPos(), playerInventory, player);
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
        // We get the NBT Tags
        t.getTileData().putInt("MaxStorage", 10000);
        double electronicPower = t.getTileData().getDouble("ElectronicPower");

        if (world != null) { // Avoid NullPointerExceptions

            // Input slots - Handling slots
            EnergyFunction.transferEnergyWithItemSlot(t.getTileData(), ItemInit.PORTABLE_BATTERY.get().asItem(), inventory, true, electronicPower, 1, 4);
            EnergyFunction.transferEnergyWithItemSlot(t.getTileData(), ItemInit.MECHANIC_WINGS.get().asItem(), inventory, true, electronicPower, 1, 8);
            EnergyFunction.transferEnergyWithItemSlot(t.getTileData(), ItemInit.PORTABLE_TELEPORTER.get().asItem(), inventory, true, electronicPower, 1, 8);

            // Output slot - Handling slots
            EnergyFunction.transferEnergyWithItemSlot(t.getTileData(), ItemInit.PORTABLE_BATTERY.get().asItem(), inventory, false, electronicPower, 0, 4);

            // We pass energy to blocks around (this part is common to all generators)
            EnergyFunction.generatorTransferEnergy(world, blockPos, Direction.values(), t.getTileData(), 6, electronicPower, false);

            t.setChanged();
            world.sendBlockUpdated(blockPos, t.getBlockState(), t.getBlockState(), 3);
        }
    }

    public int getElectronicPower() {
        return (int) this.electronicPower;
    }

    public void setElectronicPower(double electronicPower) {
        this.electronicPower = electronicPower;
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
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.put("Inventory", inventory.serializeNBT());
    }
}