package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.containers.DimensionLinkerContainer;

import static net.reikeb.electrona.init.BlockEntityInit.DIMENSION_LINKER_BLOCK_ENTITY;

public class DimensionLinkerBlockEntity extends AbstractBlockEntity {

    public String dimensionID;

    public DimensionLinkerBlockEntity(BlockPos pos, BlockState state) {
        super(DIMENSION_LINKER_BLOCK_ENTITY.get(), pos, state, "dimension_linker", 1);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new DimensionLinkerContainer(id, this.getBlockPos(), playerInventory, player);
    }

    public String getDimensionID() {
        return this.dimensionID;
    }

    public void setDimensionID(int dimensionID) {
        this.dimensionID = String.valueOf(dimensionID);
    }

    public int getDimensionIntID() {
        return Integer.parseInt(this.dimensionID);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.dimensionID = compound.getString("dimensionID");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        if (this.dimensionID != null) {
            compound.putString("dimensionID", this.dimensionID);
        }
    }
}
