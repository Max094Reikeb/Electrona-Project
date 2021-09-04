package net.reikeb.electrona.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.misc.vm.EnergyFunction;

import static net.reikeb.electrona.init.TileEntityInit.TILE_SOLAR_PANEL_T_1;

public class TileSolarPanelT1 extends BlockEntity {

    private double electronicPower;
    private int maxStorage;

    public TileSolarPanelT1(BlockPos pos, BlockState state) {
        super(TILE_SOLAR_PANEL_T_1.get(), pos, state);
    }

    public void tick() {
        // We get the variables
        Level world = this.level;
        BlockPos blockPos = this.getBlockPos();

        // We get the NBT Tags
        this.getTileData().putInt("MaxStorage", 1000);
        double electronicPower = this.getTileData().getDouble("ElectronicPower");

        if (world != null) { // Avoid NullPointerExceptions

            // We generate the energy (this part is uncommon for all generators)
            if ((world.canSeeSky(new BlockPos(blockPos.getX(), (blockPos.getY() + 1), blockPos.getZ())))
                    && (world.isDay())) {
                if (electronicPower < 996) {
                    if ((world.getLevelData().isRaining() || world.getLevelData().isThundering())) {
                        this.getTileData().putDouble("ElectronicPower", (electronicPower + 0.1));
                    } else {
                        this.getTileData().putDouble("ElectronicPower", (electronicPower + 0.2));
                    }
                } else if (electronicPower >= 996 && electronicPower <= 999.95) {
                    this.getTileData().putDouble("ElectronicPower", (electronicPower + 0.05));
                }
            } else {
                if (electronicPower > 0.2) {
                    this.getTileData().putDouble("ElectronicPower", (electronicPower - 0.2));
                } else if (electronicPower <= 0.2 && electronicPower >= 0.05) {
                    this.getTileData().putDouble("ElectronicPower", (electronicPower - 0.05));
                }
            }

            // We pass energy to blocks around (this part is common to all generators)
            EnergyFunction.generatorTransferEnergy(world, blockPos, Direction.values(), this.getTileData(), 4, electronicPower, true);
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound = super.save(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
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
