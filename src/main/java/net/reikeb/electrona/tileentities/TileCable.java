package net.reikeb.electrona.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

import net.reikeb.electrona.misc.vm.CableFunction;

import static net.reikeb.electrona.init.TileEntityInit.*;

public class TileCable extends TileEntity implements ITickableTileEntity {

    private double electronicPower;
    private boolean cableLogic;
    private int maxStorage;

    public TileCable() {
        super(TILE_CABLE.get());
    }

    @Override
    public void tick() {

        // We get NBT Tags
        double electronicPower = this.getTileData().getDouble("ElectronicPower");
        boolean cableLogic = this.getTileData().getBoolean("logic");
        this.getTileData().putInt("MaxStorage", 36);

        // We pass energy to blocks around (this part is common to all cables)
        CableFunction.cableTransferEnergy(this.level, this.getBlockPos(), Direction.values(), this.getTileData(), electronicPower, cableLogic, 6, false);

        this.setChanged();
    }

    @Override
    public void load(BlockState blockState, CompoundNBT compound) {
        super.load(blockState, compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.cableLogic = compound.getBoolean("logic");
        this.maxStorage = compound.getInt("MaxStorage");
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound = super.save(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putBoolean("logic", this.cableLogic);
        compound.putInt("MaxStorage", this.maxStorage);
        return compound;
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
}
