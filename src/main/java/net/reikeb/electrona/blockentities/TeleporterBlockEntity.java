package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.containers.TeleporterContainer;
import net.reikeb.electrona.init.ItemInit;

import static net.reikeb.electrona.init.BlockEntityInit.TELEPORTER_BLOCK_ENTITY;

public class TeleporterBlockEntity extends AbstractBlockEntity {

    public static final BlockEntityTicker<TeleporterBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    public double electronicPower;
    private int maxStorage;
    private double teleportX;
    private double teleportY;
    private double teleportZ;
    private int itemTeleportX;
    private int itemTeleportY;
    private int itemTeleportZ;
    private boolean autoDeletion;
    private boolean isTeleportSaver;
    private boolean isTeleporter;

    public TeleporterBlockEntity(BlockPos pos, BlockState state) {
        super(TELEPORTER_BLOCK_ENTITY.get(), pos, state, 1);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.electrona.teleporter.name");
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("teleporter");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new TeleporterContainer(id, this.getBlockPos(), playerInventory, player);
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
        // We get the NBT Tag
        this.getTileData().putInt("MaxStorage", 2000);

        if (world == null) return;
        ItemStack stack = inventory.getStackInSlot(0);
        if ((stack.getItem() == ItemInit.TELEPORT_SAVER.get()) || (stack.getItem() == ItemInit.PORTABLE_TELEPORTER.get())) {
            this.itemTeleportX = stack.getOrCreateTag().getInt("teleportX");
            this.itemTeleportY = stack.getOrCreateTag().getInt("teleportY");
            this.itemTeleportZ = stack.getOrCreateTag().getInt("teleportZ");
            this.isTeleportSaver = stack.getItem() == ItemInit.TELEPORT_SAVER.get();
            this.isTeleporter = stack.getItem() == ItemInit.PORTABLE_TELEPORTER.get();
        }

        this.setChanged();
        world.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(), 3);
    }

    public int getElectronicPower() {
        return (int) this.electronicPower;
    }

    public void setElectronicPower(int electronicPower) {
        this.electronicPower = electronicPower;
    }

    public int getTeleportX() {
        return (int) this.teleportX;
    }

    public void setTeleportX(int teleportX) {
        this.teleportX = teleportX;
    }

    public int getTeleportY() {
        return (int) this.teleportY;
    }

    public void setTeleportY(int teleportY) {
        this.teleportY = teleportY;
    }

    public int getTeleportZ() {
        return (int) this.teleportZ;
    }

    public void setTeleportZ(int teleportZ) {
        this.teleportZ = teleportZ;
    }

    public int isAutoDeletion() {
        return this.autoDeletion ? 1 : 0;
    }

    public void setAutoDeletion(int isAutoDeletion) {
        this.autoDeletion = (isAutoDeletion == 1);
    }

    public int getItemTeleportX() {
        return this.itemTeleportX;
    }

    public void setItemTeleportX(int itemTeleportX) {
        this.itemTeleportX = itemTeleportX;
    }

    public int getItemTeleportY() {
        return this.itemTeleportY;
    }

    public void setItemTeleportY(int itemTeleportY) {
        this.itemTeleportY = itemTeleportY;
    }

    public int getItemTeleportZ() {
        return this.itemTeleportZ;
    }

    public void setItemTeleportZ(int itemTeleportZ) {
        this.itemTeleportZ = itemTeleportZ;
    }

    public int isTeleportSaver() {
        return this.isTeleportSaver ? 1 : 0;
    }

    public void setTeleportSaver(int isTeleportSaver) {
        this.isTeleportSaver = (isTeleportSaver == 1);
    }

    public int isTeleporter() {
        return this.isTeleporter ? 1 : 0;
    }

    public void setTeleporter(int isTeleporter) {
        this.isTeleporter = (isTeleporter == 1);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        this.teleportX = compound.getDouble("teleportX");
        this.teleportY = compound.getDouble("teleportY");
        this.teleportZ = compound.getDouble("teleportZ");
        this.autoDeletion = compound.getBoolean("autoDeletion");
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundTag) compound.get("Inventory"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.putDouble("teleportX", this.teleportX);
        compound.putDouble("teleportY", this.teleportY);
        compound.putDouble("teleportZ", this.teleportZ);
        compound.putBoolean("autoDeletion", this.autoDeletion);
        compound.put("Inventory", inventory.serializeNBT());
    }
}
