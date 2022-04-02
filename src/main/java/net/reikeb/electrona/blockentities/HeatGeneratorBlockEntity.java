package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.blocks.HeatGenerator;
import net.reikeb.electrona.misc.Keys;
import net.reikeb.electrona.misc.vm.EnergyFunction;

import javax.annotation.Nullable;
import java.util.Objects;

import static net.reikeb.electrona.init.BlockEntityInit.HEAT_GENERATOR_BLOCK_ENTITY;

public class HeatGeneratorBlockEntity extends BlockEntity {

    public static final BlockEntityTicker<HeatGeneratorBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    private double electronicPower;
    private int maxStorage;

    public HeatGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(HEAT_GENERATOR_BLOCK_ENTITY.get(), pos, state);
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
        // We set the NBT Tags
        this.getTileData().putInt("MaxStorage", 2000);
        double electronicPower = this.getTileData().getDouble("ElectronicPower");

        if (world != null) { // Avoid NullPointerExceptions

            // We generate the energy (this part is uncommon for all generators)
            ResourceLocation biomeRL = world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(world.getBiome(blockPos));

            if (electronicPower > 0) {
                world.setBlockAndUpdate(blockPos, this.getBlockState().setValue(HeatGenerator.HEATING, true));
            } else {
                world.setBlockAndUpdate(blockPos, this.getBlockState().setValue(HeatGenerator.HEATING, false));
            }

            if ((biomeRL != null) && (biomeRL.equals(Keys.DESERT_BIOME)
                    || biomeRL.equals(Keys.NETHER_WASTES_BIOME)
                    || biomeRL.equals(Keys.WARM_OCEAN_BIOME)
                    || biomeRL.equals(Keys.DEEP_WARM_OCEAN_BIOME)
                    || biomeRL.equals(Keys.MODIFIED_WOODED_BADLANDS_BIOME)
                    || biomeRL.equals(Keys.MODIFIED_BADLANDS_BIOME)
                    || biomeRL.equals(Keys.CRIMSON_FOREST_BIOME)
                    || biomeRL.equals(Keys.WARPED_FOREST_BIOME)
                    || biomeRL.equals(Keys.SOUL_SAND_VALLEY_BIOME)
                    || biomeRL.equals(Keys.BASALT_DELTAS_BIOME))) {
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
            EnergyFunction.generatorTransferEnergy(world, blockPos, Direction.values(), this.getTileData(), 3, electronicPower, true);

            this.setChanged();
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