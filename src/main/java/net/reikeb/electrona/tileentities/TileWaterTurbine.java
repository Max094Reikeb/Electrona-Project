package net.reikeb.electrona.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.common.util.Constants;

import net.reikeb.electrona.utils.ElectronaUtils;

import static net.reikeb.electrona.init.TileEntityInit.*;

public class TileWaterTurbine extends TileEntity implements ITickableTileEntity {

    private double electronicPower;
    private int maxStorage;

    public TileWaterTurbine() {
        super(TILE_WATER_TURBINE.get());
    }

    public Direction getDirection() {
        try {
            DirectionProperty property = (DirectionProperty) this.getBlockState().getBlock().getStateDefinition().getProperty("facing");
            if (property != null)
                return this.getBlockState().getValue(property);
            return Direction.fromAxisAndDirection(
                    this.getBlockState().getValue((EnumProperty<Direction.Axis>) this.getBlockState().getBlock().getStateDefinition().getProperty("axis")),
                    Direction.AxisDirection.POSITIVE);
        } catch (Exception e) {
            return Direction.NORTH;
        }
    }

    @Override
    public void tick() {
        // We get the variables
        World world = this.level;
        int x = this.worldPosition.getX();
        int y = this.worldPosition.getY();
        int z = this.worldPosition.getZ();
        BlockPos blockPos = this.getBlockPos();

        // We get the NBT Tags
        this.getTileData().putInt("MaxStorage", 1000);
        double electronicPower = this.getTileData().getDouble("ElectronicPower");

        if (world != null) { // Avoid NullPointerExceptions

            // We generate the energy (this part is uncommon for all generators)
            if ((this.getDirection() == Direction.WEST) || (this.getDirection() == Direction.EAST)) {
                if ((this.getDirection() == Direction.WEST)
                        && (Blocks.AIR == world.getBlockState(new BlockPos(x - 1, y, z)).getBlock())
                        && (world.getBlockState(new BlockPos(x + 1, y, z)).getMaterial() == Material.WATER)) {
                    world.setBlock(new BlockPos(x - 1, y, z), Blocks.WATER.defaultBlockState(), 3);
                } else if (((this.getDirection() == Direction.EAST)
                        && (Blocks.AIR == world.getBlockState(new BlockPos(x + 1, y, z)).getBlock())
                        && (world.getBlockState(new BlockPos(x - 1, y, z)).getMaterial() == Material.WATER))) {
                    world.setBlock(new BlockPos(x + 1, y, z), Blocks.WATER.defaultBlockState(), 3);
                }
                if ((((world.getBlockState(new BlockPos(x - 1, y, z))).getMaterial() == Material.WATER)
                        && ((world.getBlockState(new BlockPos(x + 1, y, z))).getMaterial() == Material.WATER))) {
                    if ((electronicPower < 996)) {
                        this.getTileData().putDouble("ElectronicPower", (electronicPower + 0.15));
                    } else if (((electronicPower >= 996) && (electronicPower < 1000))) {
                        this.getTileData().putDouble("ElectronicPower", (electronicPower + 0.05));
                    }
                } else {
                    if ((electronicPower > 4)) {
                        this.getTileData().putDouble("ElectronicPower", (electronicPower - 0.2));
                    } else if (((electronicPower <= 4) && (electronicPower > 0))) {
                        this.getTileData().putDouble("ElectronicPower", (electronicPower - 0.05));
                    }
                }
            } else if (((this.getDirection() == Direction.NORTH) || (this.getDirection() == Direction.SOUTH))) {
                if (((this.getDirection() == Direction.NORTH)
                        && ((Blocks.AIR == (world.getBlockState(new BlockPos((int) x, y, z - 1))).getBlock())
                        && ((world.getBlockState(new BlockPos(x, y, z + 1))).getMaterial() == Material.WATER)))) {
                    world.setBlock(new BlockPos(x, y, z - 1), Blocks.WATER.defaultBlockState(), 3);
                } else if (((this.getDirection() == Direction.SOUTH)
                        && ((Blocks.AIR == (world.getBlockState(new BlockPos(x, y, z + 1))).getBlock())
                        && ((world.getBlockState(new BlockPos(x, y, z - 1))).getMaterial() == Material.WATER)))) {
                    world.setBlock(new BlockPos(x, y, z + 1), Blocks.WATER.defaultBlockState(), 3);
                }
                if ((((world.getBlockState(new BlockPos(x, y, z + 1))).getMaterial() == Material.WATER)
                        && ((world.getBlockState(new BlockPos(x, y, z - 1))).getMaterial() == Material.WATER))) {
                    if ((electronicPower < 996)) {
                        this.getTileData().putDouble("ElectronicPower", (electronicPower + 0.15));
                    } else if (((electronicPower >= 996) && (electronicPower < 1000))) {
                        this.getTileData().putDouble("ElectronicPower", (electronicPower + 0.05));
                    }
                } else {
                    if ((electronicPower > 4)) {
                        this.getTileData().putDouble("ElectronicPower", (electronicPower - 0.2));
                    } else if (((electronicPower <= 4) && (electronicPower > 0))) {
                        this.getTileData().putDouble("ElectronicPower", (electronicPower - 0.05));
                    }
                }
            }

            // We pass energy to blocks around (this part is common to all generators)
            ElectronaUtils.generatorTransferEnergy(world, blockPos, Direction.values(), this.getTileData(), 4, electronicPower, true);
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
