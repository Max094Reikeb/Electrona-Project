package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.BatteryContainer;
import net.reikeb.electrona.misc.vm.EnergyFunction;
import net.reikeb.maxilib.abs.AbstractEnergyBlockEntity;

import static net.reikeb.electrona.init.BlockEntityInit.BATTERY_BLOCK_ENTITY;

public class BatteryBlockEntity extends AbstractEnergyBlockEntity {

    public static final BlockEntityTicker<BatteryBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);

    public BatteryBlockEntity(BlockPos pos, BlockState state) {
        super(BATTERY_BLOCK_ENTITY.get(), pos, state, "battery", Electrona.MODID, 2);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new BatteryContainer(id, this.getBlockPos(), playerInventory, player);
    }

    public <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState state, T t) {
        this.setMaxStorage(10000);
        if (level == null) return;

        // Input & Output slots - Handling slots
        EnergyFunction.transferEnergyWithItemSlot(this, true, 1, 4);
        EnergyFunction.transferEnergyWithItemSlot(this, false, 0, 4);

        // We pass energy to blocks around (this part is common to all generators)
        EnergyFunction.generatorTransferEnergy(level, blockPos, this, 6, false);

        t.setChanged();
        level.sendBlockUpdated(blockPos, t.getBlockState(), t.getBlockState(), 3);
    }
}
