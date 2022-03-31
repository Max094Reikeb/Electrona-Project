package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;

import net.minecraftforge.items.CapabilityItemHandler;

import net.reikeb.electrona.tileentities.TileCompressor;

import static net.reikeb.electrona.init.ContainerInit.COMPRESSOR_CONTAINER;

public class CompressorContainer extends AbstractContainer {

    public TileCompressor tileCompressor;

    public CompressorContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(COMPRESSOR_CONTAINER.get(), id, 3);

        this.tileCompressor = (TileCompressor) player.getCommandSenderWorld().getBlockEntity(pos);
        if (tileCompressor == null) return;

        tileCompressor.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new BasicInputSlot(h, 0, 27, 39));
            addSlot(new BasicInputSlot(h, 1, 81, 39));
            addSlot(new CompressorOutputSlot(h, 2, 135, 39));
        });

        this.layoutPlayerInventorySlots(inv);
        this.trackData();
    }

    public int getElectronicPower() {
        return tileCompressor.getElectronicPower();
    }

    public int getCompressingTime() {
        return tileCompressor.getCompressingTime();
    }

    public int getCurrentCompressingTime() {
        return tileCompressor.getCurrentCompressingTime();
    }

    private void trackData() {
        // ElectronicPower
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return getElectronicPower() & 0xffff;
            }

            @Override
            public void set(int value) {
                int energyStored = getElectronicPower() & 0xffff0000;
                tileCompressor.setElectronicPower(energyStored + (value & 0xffff));
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
                tileCompressor.setElectronicPower(energyStored | (value << 16));
            }
        });
        // CompressingTime
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return getCompressingTime() & 0xffff;
            }

            @Override
            public void set(int value) {
                int compressingTime = getCompressingTime() & 0xffff0000;
                tileCompressor.setCompressingTime(compressingTime + (value & 0xffff));
            }
        });
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return (getCompressingTime() >> 16) & 0xffff;
            }

            @Override
            public void set(int value) {
                int compressingTime = getCompressingTime() & 0x0000ffff;
                tileCompressor.setCompressingTime(compressingTime | (value << 16));
            }
        });
        // CurrentCompressingTime
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return getCurrentCompressingTime() & 0xffff;
            }

            @Override
            public void set(int value) {
                int currentCompressingTime = getCurrentCompressingTime() & 0xffff0000;
                tileCompressor.setCurrentCompressingTime(currentCompressingTime + (value & 0xffff));
            }
        });
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return (getCurrentCompressingTime() >> 16) & 0xffff;
            }

            @Override
            public void set(int value) {
                int currentCompressingTime = getCurrentCompressingTime() & 0x0000ffff;
                tileCompressor.setCurrentCompressingTime(currentCompressingTime | (value << 16));
            }
        });
    }
}
