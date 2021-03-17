package net.reikeb.electrona.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.common.util.Constants;

import net.reikeb.electrona.utils.ElectronaUtils;

import static net.reikeb.electrona.init.TileEntityInit.*;

public class TileSolarPanelT1 extends TileEntity implements ITickableTileEntity {

    private double electronicPower;
    private int maxStorage;

    public TileSolarPanelT1() {
        super(TILE_SOLAR_PANEL_T_1.get());
    }

    @Override
    public void tick() {
        // We get the variables
        World world = this.level;
        BlockPos blockPos = this.getBlockPos();

        // We get the NBT Tags
        this.getTileData().putInt("MaxStorage", 1000);
        double electronicPower = this.getTileData().getDouble("ElectronicPower");

        if (world != null) { // Avoid NullPointerExceptions

            // We generate the energy (this part is uncommon for all generators)
            if ((world.canSeeSkyFromBelowWater(blockPos)) && (world.isDay())) {
                if (electronicPower < 996) {
                    if ((world.getLevelData().isRaining() || world.getLevelData().isThundering())) {
                        this.getTileData().putDouble("ElectronicPower", (electronicPower + 0.1));
                    } else {
                        this.getTileData().putDouble("ElectronicPower", (electronicPower + 0.2));
                    }
                } else if (electronicPower >= 996 && electronicPower < 1000) {
                    this.getTileData().putDouble("ElectronicPower", (electronicPower + 0.05));
                }
            } else {
                if (electronicPower > 4) {
                    this.getTileData().putDouble("ElectronicPower", (electronicPower - 0.2));
                } else if (electronicPower > 0 && electronicPower <= 4) {
                    this.getTileData().putDouble("ElectronicPower", (electronicPower - 0.05));
                }
            }

            // We pass energy to blocks around (this part is common to all generators)
            ElectronaUtils.generatorTransferEnergy(world, blockPos, Direction.values(), this.getTileData(), 4, electronicPower, true);

            this.setChanged();
            world.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(),
                    Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    @Override
    public void load(BlockState blockState, CompoundNBT compound) {
        super.load(blockState, compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound = super.save(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
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
