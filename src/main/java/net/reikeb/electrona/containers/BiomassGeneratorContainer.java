package net.reikeb.electrona.containers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import net.reikeb.electrona.tileentities.TileBiomassGenerator;

import static net.reikeb.electrona.init.ContainerInit.BIOMASS_GENERATOR_CONTAINER;

public class BiomassGeneratorContainer extends AbstractContainer {

    public TileBiomassGenerator tileEntity;

    public BiomassGeneratorContainer(MenuType<?> type, int id) {
        super(type, id, 1);
    }

    // Client
    public BiomassGeneratorContainer(int id, Inventory inv, FriendlyByteBuf buf) {
        super(BIOMASS_GENERATOR_CONTAINER.get(), id, 1);
        this.init(inv, this.tileEntity = (TileBiomassGenerator) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    // Server
    public BiomassGeneratorContainer(int id, Inventory inv, TileBiomassGenerator tile) {
        super(BIOMASS_GENERATOR_CONTAINER.get(), id, 1);
        this.init(inv, this.tileEntity = tile);
    }

    public void init(Inventory playerInv, TileBiomassGenerator tile) {

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 80, 36));
            });
        }
        this.layoutPlayerInventorySlots(playerInv);
    }

    public TileBiomassGenerator getTileEntity() {
        return this.tileEntity;
    }
}
