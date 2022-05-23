package net.reikeb.electrona.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.reikeb.electrona.blockentities.CompressorBlockEntity;
import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.CompressionPacket;
import net.reikeb.maxilib.abs.AbstractContainer;
import net.reikeb.maxilib.inventory.Slots;
import org.jetbrains.annotations.NotNull;

import static net.reikeb.electrona.init.ContainerInit.COMPRESSOR_CONTAINER;

public class CompressorContainer extends AbstractContainer {

    public CompressorBlockEntity compressorBlockEntity;

    public CompressorContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(COMPRESSOR_CONTAINER.get(), id, 3);

        this.compressorBlockEntity = (CompressorBlockEntity) player.getCommandSenderWorld().getBlockEntity(pos);
        if (compressorBlockEntity == null) return;

        compressorBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new Slots(h, 0, 27, 39));
            addSlot(new Slots(h, 1, 81, 39));
            addSlot(new CompressorSlot(h, 2, 135, 39));
        });

        this.layoutPlayerInventorySlots(inv);
        this.addSyncedInt(compressorBlockEntity::setHundredEnergy, compressorBlockEntity::getHundredEnergy);
        this.addSyncedInt(compressorBlockEntity::setCompressingTime, compressorBlockEntity::getCompressingTime);
        this.addSyncedInt(compressorBlockEntity::setCurrentCompressingTime, compressorBlockEntity::getCurrentCompressingTime);
    }

    public double getEnergy() {
        return compressorBlockEntity.getEnergy();
    }

    public int getCompressingTime() {
        return compressorBlockEntity.getCompressingTime();
    }

    public int getCurrentCompressingTime() {
        return compressorBlockEntity.getCurrentCompressingTime();
    }

    public static class CompressorSlot extends SlotItemHandler {

        public CompressorSlot(IItemHandler itemHandler, int id, int x, int y) {
            super(itemHandler, id, x, y);
        }

        public boolean mayPlace(@NotNull ItemStack itemStack) {
            return false;
        }

        public void onTake(@NotNull Player playerEntity, @NotNull ItemStack stack) {
            // Trigger Advancement
            NetworkManager.INSTANCE.sendToServer(new CompressionPacket());
        }
    }
}
