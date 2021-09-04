package net.reikeb.electrona.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.misc.vm.CableFunction;

import static net.reikeb.electrona.init.TileEntityInit.TILE_CABLE;

public class TileCable extends BlockEntity {

    private double electronicPower;
    private boolean cableLogic;
    private int maxStorage;

    public TileCable(BlockPos pos, BlockState state) {
        super(TILE_CABLE.get(), pos, state);
    }

    public void tick() {

        // We get NBT Tags
        double electronicPower = this.getTileData().getDouble("ElectronicPower");
        this.getTileData().putInt("MaxStorage", 36);

        // We pass energy to blocks around (this part is common to all cables)
        CableFunction.cableTransferEnergy(this.level, this.getBlockPos(), Direction.values(), this.getTileData(), electronicPower, 6, false);

        this.setChanged();
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
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
        this.load(pkt.getTag());
    }
}
