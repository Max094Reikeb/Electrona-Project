package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

import net.minecraftforge.items.CapabilityItemHandler;

import net.reikeb.electrona.blockentities.BiomassGeneratorBlockEntity;

import static net.reikeb.electrona.init.ContainerInit.BIOMASS_GENERATOR_CONTAINER;

public class BiomassGeneratorContainer extends AbstractContainer {

    public BiomassGeneratorBlockEntity biomassGeneratorBlockEntity;

    public BiomassGeneratorContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(BIOMASS_GENERATOR_CONTAINER.get(), id, 1);

        this.biomassGeneratorBlockEntity = (BiomassGeneratorBlockEntity) player.getCommandSenderWorld().getBlockEntity(pos);
        if (biomassGeneratorBlockEntity == null) return;

        biomassGeneratorBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new BasicInputSlot(h, 0, 80, 36));
        });

        this.layoutPlayerInventorySlots(inv);
        this.addSyncedInt(biomassGeneratorBlockEntity::setElectronicPower, biomassGeneratorBlockEntity::getElectronicPower);
    }

    public int getElectronicPower() {
        return biomassGeneratorBlockEntity.getElectronicPower();
    }
}
