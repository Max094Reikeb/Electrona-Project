package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;

import net.minecraftforge.items.CapabilityItemHandler;

import net.reikeb.electrona.tileentities.TileBiomassGenerator;

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
        this.trackData();
    }

    public int getElectronicPower() {
        return (int) tileBiomassGenerator.getElectronicPower();
    }

    private void trackData() {
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return getElectronicPower() & 0xffff;
            }

            @Override
            public void set(int value) {
                int energyStored = getElectronicPower() & 0xffff0000;
                tileBiomassGenerator.setElectronicPower(energyStored + (value & 0xffff));
            }
        });
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return (getElectronicPower() >> 16) & 0xffff;
            }

            @Override
            public void set(int value) {
                int energyStored = getElectronicPower() & 0x0000ffff;
                tileBiomassGenerator.setElectronicPower(energyStored | (value << 16));
            }
        });
    }
}
