package net.reikeb.electrona.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import net.reikeb.electrona.blocks.HeatGenerator;
import net.reikeb.electrona.misc.vm.EnergyFunction;

import static net.reikeb.electrona.init.TileEntityInit.*;

public class TileHeatGenerator extends TileEntity implements ITickableTileEntity {

    private double electronicPower;
    private int maxStorage;

    public TileHeatGenerator() {
        super(TILE_HEAT_GENERATOR.get());
    }

    @Override
    public void tick() {
        // We get the variables
        World world = this.level;
        BlockPos pos = this.getBlockPos();

        // We set the NBT Tags
        this.getTileData().putInt("MaxStorage", 2000);
        double electronicPower = this.getTileData().getDouble("ElectronicPower");

        if (world != null) { // Avoid NullPointerExceptions

            // We generate the energy (this part is uncommon for all generators)
            ResourceLocation biomeRL = world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(world.getBiome(pos));

            if (electronicPower > 0) {
                world.setBlockAndUpdate(pos, this.getBlockState().setValue(HeatGenerator.HEATING, true));
            } else {
                world.setBlockAndUpdate(pos, this.getBlockState().setValue(HeatGenerator.HEATING, false));
            }

            if (((biomeRL != null)) && (biomeRL.equals(new ResourceLocation("desert"))
                    || (biomeRL.equals(new ResourceLocation("nether_wastes"))
                    || (biomeRL.equals(new ResourceLocation("warm_ocean"))
                    || (biomeRL.equals(new ResourceLocation("deep_warm_ocean")))
                    || (biomeRL.equals(new ResourceLocation("modified_wooded_badlands_plateau"))
                    || (biomeRL.equals(new ResourceLocation("modified_badlands_plateau"))
                    || (biomeRL.equals(new ResourceLocation("crimson_forest"))
                    || (biomeRL.equals(new ResourceLocation("warped_forest"))
                    || (biomeRL.equals(new ResourceLocation("soul_sand_valley"))
                    || (biomeRL.equals(new ResourceLocation("basalt_deltas")))))))))))) {
                if (electronicPower < 1996) {
                    this.getTileData().putDouble("ElectronicPower", (electronicPower + 0.15));
                } else if ((electronicPower >= 1996) && (electronicPower <= 1999.95)) {
                    this.getTileData().putDouble("ElectronicPower", (electronicPower + 0.05));
                }
            } else {
                if (electronicPower > 0.15) {
                    this.getTileData().putDouble("ElectronicPower", (electronicPower - 0.15));
                } else if ((electronicPower <= 0.15) && (electronicPower >= 0.05)) {
                    this.getTileData().putDouble("ElectronicPower", (electronicPower - 0.05));
                }
            }

            // We pass energy to blocks around (this part is common to all generators)
            EnergyFunction.generatorTransferEnergy(world, pos, Direction.values(), this.getTileData(), 3, electronicPower, true);

            this.setChanged();
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
