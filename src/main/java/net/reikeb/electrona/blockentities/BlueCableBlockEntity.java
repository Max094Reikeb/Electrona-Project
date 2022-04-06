package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.misc.vm.CableFunction;
import net.reikeb.electrona.utils.ItemHandler;

import javax.annotation.Nullable;
import java.util.Objects;

import static net.reikeb.electrona.init.BlockEntityInit.BLUE_CABLE_BLOCK_ENTITY;

public class BlueCableBlockEntity extends BlockEntity implements AbstractEnergyBlockEntity {

    public static final BlockEntityTicker<BlueCableBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    public double electronicPower;
    public int maxStorage;
    private boolean cableLogic;

    public BlueCableBlockEntity(BlockPos pos, BlockState state) {
        super(BLUE_CABLE_BLOCK_ENTITY.get(), pos, state);
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
        this.setMaxStorage(36);

        // We pass energy to blocks around (this part is common to all cables)
        CableFunction.cableTransferEnergy(world, blockPos, Direction.values(), this, 6, true);

        t.setChanged();
    }

    public ItemHandler getItemInventory() {
        return null;
    }

    public int getElectronicPowerTimesHundred() {
        return (int) (this.electronicPower * 100);
    }

    public void setElectronicPowerTimesHundred(int electronicPowerTimesHundred) {
        this.electronicPower = electronicPowerTimesHundred / 100.0;
    }

    public double getElectronicPower() {
        return this.electronicPower;
    }

    public void setElectronicPower(double electronicPower) {
        this.electronicPower = electronicPower;
    }

    public int getMaxStorage() {
        return this.maxStorage;
    }

    public void setMaxStorage(int maxStorage) {
        this.maxStorage = maxStorage;
    }

    public boolean getLogic() {
        return false;
    }

    public void setLogic(boolean logic) {
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.cableLogic = compound.getBoolean("logic");
        this.maxStorage = compound.getInt("MaxStorage");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putBoolean("logic", this.cableLogic);
        compound.putInt("MaxStorage", this.maxStorage);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        CompoundTag nbt = new CompoundTag();
        saveAdditional(nbt);
        return ClientboundBlockEntityDataPacket.create(this, blockEntity -> nbt);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(Objects.requireNonNull(pkt.getTag()));
    }
}
