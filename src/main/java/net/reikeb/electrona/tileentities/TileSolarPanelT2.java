package net.reikeb.electrona.tileentities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Locale;

import static net.reikeb.electrona.setup.RegistryHandler.TILE_SOLAR_PANEL_T_2;

public class TileSolarPanelT2 extends TileEntity implements ITickableTileEntity {

    private int ElectronicPower;
    private int MaxStorage;

    public TileSolarPanelT2() {
        super(TILE_SOLAR_PANEL_T_2.get());
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
        this.getTileData().putDouble("MaxStorage", 2000);
        double electronicPower = this.getTileData().getDouble("ElectronicPower");

        if (world != null) { // Avoid NullPointerExceptions

            // We generate the energy (this part is uncommon for all generators)
            if ((world.canSeeSkyFromBelowWater(new BlockPos(x, y + 1, z)))
                    && (world.isDay())) {
                if (electronicPower < 1996) {
                    if ((world.getLevelData().isRaining() || world.getLevelData().isThundering())) {
                        this.getTileData().putDouble("ElectronicPower", (electronicPower + 0.2));
                    } else {
                        this.getTileData().putDouble("ElectronicPower", (electronicPower + 0.3));
                    }
                } else if (electronicPower >= 1996 && electronicPower < 2000) {
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
            for (Direction dir : Direction.values()) {
                TileEntity tileEntity = world.getBlockEntity(blockPos.relative(dir));
                if (tileEntity != null) { // Avoid NullPointerExceptions
                    Block offsetBlock = world.getBlockState(blockPos.relative(dir)).getBlock();
                    double offsetElectronicPower = tileEntity.getTileData().getDouble("ElectronicPower");
                    double offsetMaxStorage = tileEntity.getTileData().getDouble("MaxStorage");
                    if ((BlockTags.getAllTags().getTagOrEmpty(new ResourceLocation(("forge:electrona/machines_all").toLowerCase(Locale.ENGLISH)))
                            .contains(offsetBlock)) || ((BlockTags.getAllTags().getTagOrEmpty(new ResourceLocation(("forge:electrona/cable")
                            .toLowerCase(Locale.ENGLISH))).contains(offsetBlock)))) {
                        if ((offsetElectronicPower < (offsetMaxStorage - 6)) && (electronicPower > 6)) {
                            this.getTileData().putDouble("ElectronicPower", (electronicPower - 0.3));
                            tileEntity.getTileData().putDouble("ElectronicPower", (offsetElectronicPower + 0.3));
                        } else if ((((offsetElectronicPower >= (offsetMaxStorage - 6)) && (offsetElectronicPower < offsetMaxStorage))
                                || ((offsetElectronicPower < (offsetMaxStorage - 6)) && ((electronicPower <= 6) && (electronicPower > 0))))) {
                            this.getTileData().putDouble("ElectronicPower", (electronicPower - 0.05));
                            tileEntity.getTileData().putDouble("ElectronicPower", (offsetElectronicPower + 0.05));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void load(BlockState blockState, CompoundNBT compound) {
        super.load(blockState, compound);
        ElectronicPower = compound.getInt("ElectronicPower");
        MaxStorage = compound.getInt("MaxStorage");
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound = super.save(compound);
        compound.putInt("ElectronicPower", ElectronicPower);
        compound.putInt("MaxStorage", MaxStorage);
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
