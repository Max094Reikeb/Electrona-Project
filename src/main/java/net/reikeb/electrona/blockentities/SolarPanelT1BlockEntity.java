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

import net.reikeb.electrona.misc.vm.EnergyFunction;

import javax.annotation.Nullable;
import java.util.Objects;

import static net.reikeb.electrona.init.BlockEntityInit.SOLAR_PANEL_T_1_BLOCK_ENTITY;

public class SolarPanelT1BlockEntity extends BlockEntity {

    public static final BlockEntityTicker<SolarPanelT1BlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    private double electronicPower;
    private int maxStorage;

    public SolarPanelT1BlockEntity(BlockPos pos, BlockState state) {
        super(SOLAR_PANEL_T_1_BLOCK_ENTITY.get(), pos, state);
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
        // We get the NBT Tags
        t.getTileData().putInt("MaxStorage", 1000);
        double electronicPower = t.getTileData().getDouble("ElectronicPower");

        if (world != null) { // Avoid NullPointerExceptions

            // We generate the energy (this part is uncommon for all generators)
            if (world.canSeeSky(blockPos.above()) && world.isDay()) {
                if (electronicPower < 996) {
                    if ((world.getLevelData().isRaining() || world.getLevelData().isThundering())) {
                        t.getTileData().putDouble("ElectronicPower", (electronicPower + 0.1));
                    } else {
                        t.getTileData().putDouble("ElectronicPower", (electronicPower + 0.2));
                    }
                } else if (electronicPower >= 996 && electronicPower <= 999.95) {
                    t.getTileData().putDouble("ElectronicPower", (electronicPower + 0.05));
                }
            } else {
                if (electronicPower > 0.2) {
                    t.getTileData().putDouble("ElectronicPower", (electronicPower - 0.2));
                } else if (electronicPower <= 0.2 && electronicPower >= 0.05) {
                    t.getTileData().putDouble("ElectronicPower", (electronicPower - 0.05));
                }
            }

            // We pass energy to blocks around (this part is common to all generators)
            EnergyFunction.generatorTransferEnergy(world, blockPos, Direction.values(), t.getTileData(), 4, electronicPower, true);
        }
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
