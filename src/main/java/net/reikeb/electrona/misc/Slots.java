package net.reikeb.electrona.misc;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.reikeb.electrona.blockentities.NuclearGeneratorControllerBlockEntity;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.electrona.network.NetworkManager;
import net.reikeb.electrona.network.packets.CompressionPacket;
import net.reikeb.electrona.network.packets.PurificationPacket;
import org.jetbrains.annotations.NotNull;

public class Slots {

    public static class BatterySlot extends SlotItemHandler {

        public BatterySlot(IItemHandler itemHandler, int id, int x, int y) {
            super(itemHandler, id, x, y);
        }

        public boolean mayPlace(@NotNull ItemStack itemStack) {
            return true;
        }

        public int getMaxStackSize() {
            return 1;
        }
    }

    public static class BasicInputSlot extends SlotItemHandler {

        public BasicInputSlot(IItemHandler itemHandler, int id, int x, int y) {
            super(itemHandler, id, x, y);
        }

        public boolean mayPlace(@NotNull ItemStack itemStack) {
            return true;
        }
    }

    public static class BucketSlot extends SlotItemHandler {
        public BucketSlot(IItemHandler itemHandler, int id, int x, int y) {
            super(itemHandler, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return Items.BUCKET == itemStack.getItem();
        }

        public int getMaxStackSize() {
            return 1;
        }
    }

    public static class WaterBucketSlot extends SlotItemHandler {

        public WaterBucketSlot(IItemHandler itemHandler, int id, int x, int y) {
            super(itemHandler, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return Items.WATER_BUCKET == itemStack.getItem();
        }

        public int getMaxStackSize() {
            return 1;
        }
    }

    public static class CompressorOutputSlot extends SlotItemHandler {

        public CompressorOutputSlot(IItemHandler itemHandler, int id, int x, int y) {
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

    public static class PurificatorOutputSlot extends SlotItemHandler {

        public PurificatorOutputSlot(IItemHandler itemHandler, int id, int x, int y) {
            super(itemHandler, id, x, y);
        }

        public boolean mayPlace(@NotNull ItemStack itemStack) {
            return false;
        }

        public void onTake(@NotNull Player playerEntity, ItemStack stack) {
            if (stack.getItem() == ItemInit.PURIFIED_URANIUM.get()) {
                // Trigger Advancement
                NetworkManager.INSTANCE.sendToServer(new PurificationPacket());
            }
        }
    }

    public static class WirelessSlot extends SlotItemHandler {

        public WirelessSlot(IItemHandler itemHandler, int id, int x, int y) {
            super(itemHandler, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return itemStack.getItem() == ItemInit.WIRELESS_BOOSTER.get();
        }

        public int getMaxStackSize() {
            return 1;
        }
    }

    public static class TeleportSlot extends SlotItemHandler {

        public TeleportSlot(IItemHandler itemHandler, int id, int x, int y) {
            super(itemHandler, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return ((itemStack.getItem() == ItemInit.TELEPORT_SAVER.get())
                    || (itemStack.getItem() == ItemInit.PORTABLE_TELEPORTER.get()));
        }

        public int getMaxStackSize() {
            return 1;
        }
    }

    public static class EmeraldSlot extends SlotItemHandler {

        public EmeraldSlot(IItemHandler itemHandler, int id, int x, int y) {
            super(itemHandler, id, x, y);
        }

        public boolean mayPlace(ItemStack itemStack) {
            return (itemStack.getItem() == Items.EMERALD);
        }
    }

    public static class UraniumSlot extends SlotItemHandler {

        public NuclearGeneratorControllerBlockEntity nuclearGeneratorControllerBlockEntity;

        public UraniumSlot(IItemHandler itemHandler, NuclearGeneratorControllerBlockEntity nuclearGeneratorControllerBlockEntity, int id, int x, int y) {
            super(itemHandler, id, x, y);
            this.nuclearGeneratorControllerBlockEntity = nuclearGeneratorControllerBlockEntity;
        }

        public boolean mayPlace(@NotNull ItemStack itemStack) {
            return nuclearGeneratorControllerBlockEntity.areUbIn() == 0;
        }

        public boolean mayPickup(Player player) {
            return nuclearGeneratorControllerBlockEntity.areUbIn() == 0;
        }

        public int getMaxStackSize() {
            return 1;
        }
    }
}
