package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.containers.DimensionLinkerContainer;

import static net.reikeb.electrona.init.TileEntityInit.TILE_DIMENSION_LINKER;

public class TileDimensionLinker extends AbstractTileEntity {

    public String dimensionID;
    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int p_39284_) {
            if (p_39284_ == 0) {
                return Integer.parseInt(TileDimensionLinker.this.dimensionID);
            }
            return 0;
        }

        @Override
        public void set(int p_39285_, int p_39286_) {
            if (p_39285_ == 0) {
                TileDimensionLinker.this.dimensionID = String.valueOf(p_39286_);
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public TileDimensionLinker(BlockPos pos, BlockState state) {
        super(TILE_DIMENSION_LINKER.get(), pos, state, 1);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.electrona.dimension_linker.name");
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("dimension_linker");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new DimensionLinkerContainer(id, player, dataAccess);
    }

    public String getDimensionID() {
        return this.dimensionID;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.dimensionID = compound.getString("dimensionID");
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundTag) compound.get("Inventory"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        if (this.dimensionID != null) {
            compound.putString("dimensionID", this.dimensionID);
        }
        compound.put("Inventory", inventory.serializeNBT());
    }
}
