package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.reikeb.electrona.misc.vm.EnergyFunction;
import net.reikeb.maxilib.intface.EnergyInterface;
import net.reikeb.maxilib.inventory.ItemHandler;

import javax.annotation.Nullable;
import java.util.Objects;

import static net.reikeb.electrona.init.BlockEntityInit.ENERGETIC_LIGHTNING_ROD_BLOCK_ENTITY;

public class EnergeticLightningRodBlockEntity extends BlockEntity implements EnergyInterface {

    public static final BlockEntityTicker<EnergeticLightningRodBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    private double electronicPower;
    private int maxStorage;

    public EnergeticLightningRodBlockEntity(BlockPos pos, BlockState state) {
        super(ENERGETIC_LIGHTNING_ROD_BLOCK_ENTITY.get(), pos, state);
    }

    public <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState state, T t) {
        this.setMaxEnergy(3000);

        if (level == null) return;

        // We pass energy to blocks around (this part is common to all generators)
        EnergyFunction.generatorTransferEnergy(level, blockPos, this, 50, true);
    }

    public void struckByLightning() {
        this.electronicPower = (this.electronicPower <= 2000 ? this.electronicPower + 1000 : 3000);
    }

    public ItemHandler getItemInventory() {
        return null;
    }

    public void setHundredEnergy(int hundredEnergy) {
        this.electronicPower = hundredEnergy / 100.0;
    }

    public double getEnergy() {
        return this.electronicPower;
    }

    public void setEnergy(double energy) {
        this.electronicPower = energy;
    }

    public int getMaxEnergy() {
        return this.maxStorage;
    }

    public void setMaxEnergy(int maxEnergy) {
        this.maxStorage = maxEnergy;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
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
