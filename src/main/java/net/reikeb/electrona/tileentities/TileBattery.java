package net.reikeb.electrona.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.containers.BatteryContainer;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.misc.vm.EnergyFunction;

import static net.reikeb.electrona.init.TileEntityInit.TILE_BATTERY;

public class TileBattery extends AbstractTileEntity {

    public static final BlockEntityTicker<TileBattery> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    public double electronicPower;
    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int p_39284_) {
            if (p_39284_ == 0) {
                return (int) TileBattery.this.electronicPower;
            }
            return 0;
        }

        @Override
        public void set(int p_39285_, int p_39286_) {
            if (p_39285_ == 0) {
                TileBattery.this.electronicPower = p_39286_;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };
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
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new BatteryContainer(id, player, this, dataAccess);
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
