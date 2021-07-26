package net.reikeb.electrona.tileentities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tileentity.*;
import net.minecraft.core.Direction;

import net.reikeb.electrona.misc.vm.CableFunction;

import static net.reikeb.electrona.init.TileEntityInit.*;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;

public class TileBlueCable extends BlockEntity implements TickableBlockEntity {

    private double electronicPower;
    private boolean cableLogic;
    private int maxStorage;

    public TileBlueCable() {
        super(TILE_BLUE_CABLE.get());
    }

    @Override
    public void tick() {

        // We get NBT Tags
        double electronicPower = this.getTileData().getDouble("ElectronicPower");
        this.getTileData().putInt("MaxStorage", 36);

        // We pass energy to blocks around (this part is common to all cables)
        CableFunction.cableTransferEnergy(this.level, this.getBlockPos(), Direction.values(), this.getTileData(), electronicPower, 6, true);

        this.setChanged();
    }

    @Override
    public void load(BlockState blockState, CompoundTag compound) {
        super.load(blockState, compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.cableLogic = compound.getBoolean("logic");
        this.maxStorage = compound.getInt("MaxStorage");
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound = super.save(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putBoolean("logic", this.cableLogic);
        compound.putInt("MaxStorage", this.maxStorage);
        return compound;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 0, this.getUpdateTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(this.getBlockState(), pkt.getTag());
    }
}
