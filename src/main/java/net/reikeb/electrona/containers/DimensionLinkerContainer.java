package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.reikeb.electrona.blockentities.DimensionLinkerBlockEntity;
import net.reikeb.maxilib.abs.AbstractContainer;

import java.util.HashMap;

import static net.reikeb.electrona.init.ContainerInit.DIMENSION_LINKER_CONTAINER;

public class DimensionLinkerContainer extends AbstractContainer {

    public static HashMap textFieldWidget = new HashMap();
    public DimensionLinkerBlockEntity dimensionLinkerBlockEntity;

    public DimensionLinkerContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(DIMENSION_LINKER_CONTAINER.get(), id, 0);

        this.dimensionLinkerBlockEntity = (DimensionLinkerBlockEntity) player.getCommandSenderWorld().getBlockEntity(pos);

        this.layoutPlayerInventorySlots(inv);
        this.addSyncedInt(dimensionLinkerBlockEntity::setDimensionID, dimensionLinkerBlockEntity::getDimensionIntID);
    }

    public String getDimensionID() {
        return String.valueOf(this.dimensionLinkerBlockEntity.getDimensionID());
    }

    public void setDimensionID(String dimensionID) {
        this.dimensionLinkerBlockEntity.setDimensionID(Integer.parseInt(dimensionID));
    }

    public HashMap getTextFieldWidget() {
        return textFieldWidget;
    }
}
