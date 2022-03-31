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

import javax.annotation.Nullable;
import java.util.Objects;

import static net.reikeb.electrona.init.TileEntityInit.TILE_CABLE;

public class TileCable extends BlockEntity {

    public static final BlockEntityTicker<TileCable> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    private double electronicPower;
    private boolean cableLogic;
    private int maxStorage;

    public TileCable(BlockPos pos, BlockState state) {
        super(TILE_CABLE.get(), pos, state);
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
        // We get NBT Tags
        double electronicPower = t.getTileData().getDouble("ElectronicPower");
        t.getTileData().putInt("MaxStorage", 36);

        // We pass energy to blocks around (this part is common to all cables)
        CableFunction.cableTransferEnergy(world, blockPos, Direction.values(), t.getTileData(), electronicPower, 6, false);

        t.setChanged();
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
