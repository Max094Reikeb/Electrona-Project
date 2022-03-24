package net.reikeb.electrona.containers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

import java.util.HashMap;

import static net.reikeb.electrona.init.ContainerInit.DIMENSION_LINKER_CONTAINER;

public class DimensionLinkerContainer extends AbstractContainer {

    public static HashMap textFieldWidget = new HashMap();
    private final ContainerData dimensionLinkerData;

    public DimensionLinkerContainer(int id, Inventory inv) {
        this(id, inv, new SimpleContainerData(1));
    }

    public DimensionLinkerContainer(int id, Inventory inv, ContainerData containerData) {
        super(DIMENSION_LINKER_CONTAINER.get(), id, 0);

        this.dimensionLinkerData = containerData;

        this.layoutPlayerInventorySlots(inv);
    }

    public String getDimensionID() {
        return String.valueOf(this.dimensionLinkerData.get(0));
    }

    public void setDimensionID(String dimensionID) {
        this.dimensionLinkerData.set(0, Integer.parseInt(dimensionID));
    }

    public HashMap getTextFieldWidget() {
        return textFieldWidget;
    }
}
