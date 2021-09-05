package net.reikeb.electrona.containers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

import net.reikeb.electrona.tileentities.TileDimensionLinker;

import java.util.HashMap;

import static net.reikeb.electrona.init.ContainerInit.DIMENSION_LINKER_CONTAINER;

public class DimensionLinkerContainer extends AbstractContainer {

    public TileDimensionLinker tileEntity;
    public static HashMap textFieldWidget = new HashMap();

    public DimensionLinkerContainer(MenuType<?> type, int id) {
        super(type, id, 0);
    }

    // Client
    public DimensionLinkerContainer(int id, Inventory inv, FriendlyByteBuf buf) {
        super(DIMENSION_LINKER_CONTAINER.get(), id, 0);
        this.init(inv, this.tileEntity = (TileDimensionLinker) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    // Server
    public DimensionLinkerContainer(int id, Inventory inv, TileDimensionLinker tile) {
        super(DIMENSION_LINKER_CONTAINER.get(), id, 0);
        this.init(inv, this.tileEntity = tile);
    }

    public void init(Inventory playerInv, TileDimensionLinker tile) {
    }

    public TileDimensionLinker getTileEntity() {
        return this.tileEntity;
    }

    public HashMap getTextFieldWidget() {
        return textFieldWidget;
    }
}
