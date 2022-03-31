package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

import net.minecraftforge.items.CapabilityItemHandler;

import net.reikeb.electrona.blockentities.TileBiomassGenerator;

import static net.reikeb.electrona.init.ContainerInit.BIOMASS_GENERATOR_CONTAINER;

public class BiomassGeneratorContainer extends AbstractContainer {

    public TileBiomassGenerator tileBiomassGenerator;

    public BiomassGeneratorContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(BIOMASS_GENERATOR_CONTAINER.get(), id, 1);

        this.tileBiomassGenerator = (TileBiomassGenerator) player.getCommandSenderWorld().getBlockEntity(pos);
        if (tileBiomassGenerator == null) return;

        tileBiomassGenerator.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new BasicInputSlot(h, 0, 80, 36));
        });

        this.layoutPlayerInventorySlots(inv);
        this.addSyncedInt(tileBiomassGenerator::setElectronicPower, tileBiomassGenerator::getElectronicPower);
    }

    public int getElectronicPower() {
        return tileBiomassGenerator.getElectronicPower();
    }
}
