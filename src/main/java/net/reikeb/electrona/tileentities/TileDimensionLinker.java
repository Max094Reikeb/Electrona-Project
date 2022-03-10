package net.reikeb.electrona.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.containers.DimensionLinkerContainer;
import net.reikeb.electrona.init.ContainerInit;

import static net.reikeb.electrona.init.TileEntityInit.TILE_DIMENSION_LINKER;

public class TileDimensionLinker extends AbstractTileEntity {

    public String dimensionID;

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
    public AbstractContainerMenu createMenu(final int windowID, final Inventory playerInv, final Player playerIn) {
        return new DimensionLinkerContainer(windowID, playerInv, this);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new DimensionLinkerContainer(ContainerInit.DIMENSION_LINKER_CONTAINER.get(), id);
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
