package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.containers.TeleporterContainer;
import net.reikeb.electrona.init.ItemInit;
import net.reikeb.maxilib.abs.AbstractEnergyBlockEntity;

import static net.reikeb.electrona.init.BlockEntityInit.TELEPORTER_BLOCK_ENTITY;

public class TeleporterBlockEntity extends AbstractEnergyBlockEntity {

    public static final BlockEntityTicker<TeleporterBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    private double teleportX;
    private double teleportY;
    private double teleportZ;
    private double itemTeleportX;
    private double itemTeleportY;
    private double itemTeleportZ;
    private boolean autoDeletion;
    private boolean isTeleportSaver;
    private boolean isTeleporter;

    public TeleporterBlockEntity(BlockPos pos, BlockState state) {
        super(TELEPORTER_BLOCK_ENTITY.get(), pos, state, "teleporter", Electrona.MODID, 1);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new TeleporterContainer(id, this.getBlockPos(), playerInventory, player);
    }

    public <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState state, T t) {
        this.setMaxStorage(2000);

        if (level == null) return;
        ItemStack stack = inventory.getStackInSlot(0);
        if ((stack.getItem() == ItemInit.TELEPORT_SAVER.get()) || (stack.getItem() == ItemInit.PORTABLE_TELEPORTER.get())) {
            this.itemTeleportX = stack.getOrCreateTag().getDouble("teleportX");
            this.itemTeleportY = stack.getOrCreateTag().getDouble("teleportY");
            this.itemTeleportZ = stack.getOrCreateTag().getDouble("teleportZ");
            this.isTeleportSaver = stack.getItem() == ItemInit.TELEPORT_SAVER.get();
            this.isTeleporter = stack.getItem() == ItemInit.PORTABLE_TELEPORTER.get();
        }

        this.setChanged();
        level.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(), 3);
    }

    public int getTeleportXTimesHundred() {
        return (int) (this.teleportX * 100);
    }

    public void setTeleportXTimesHundred(int teleportXTimesHundred) {
        this.teleportX = teleportXTimesHundred / 100.0;
    }

    public double getTeleportX() {
        return this.teleportX;
    }

    public void setTeleportX(double teleportX) {
        this.teleportX = teleportX;
    }

    public int getTeleportYTimesHundred() {
        return (int) (this.teleportY * 100);
    }

    public void setTeleportYTimesHundred(int teleportYTimesHundred) {
        this.teleportY = teleportYTimesHundred / 100.0;
    }

    public double getTeleportY() {
        return this.teleportY;
    }

    public void setTeleportY(double teleportY) {
        this.teleportY = teleportY;
    }

    public int getTeleportZTimesHundred() {
        return (int) (this.teleportZ * 100);
    }

    public void setTeleportZTimesHundred(int teleportZTimesHundred) {
        this.teleportZ = teleportZTimesHundred / 100.0;
    }

    public double getTeleportZ() {
        return this.teleportZ;
    }

    public void setTeleportZ(double teleportZ) {
        this.teleportZ = teleportZ;
    }

    public int isAutoDeletion() {
        return this.autoDeletion ? 1 : 0;
    }

    public void setAutoDeletion(int isAutoDeletion) {
        this.autoDeletion = (isAutoDeletion == 1);
    }

    public int getItemTeleportXTimesHundred() {
        return (int) (this.itemTeleportX * 100);
    }

    public void setItemTeleportXTimesHundred(int itemTeleportXTimesHundred) {
        this.itemTeleportX = itemTeleportXTimesHundred / 100.0;
    }

    public double getItemTeleportX() {
        return this.itemTeleportX;
    }

    public void setItemTeleportX(double itemTeleportX) {
        this.itemTeleportX = itemTeleportX;
    }

    public int getItemTeleportYTimesHundred() {
        return (int) (this.itemTeleportY * 100);
    }

    public void setItemTeleportYTimesHundred(int itemTeleportYTimesHundred) {
        this.itemTeleportY = itemTeleportYTimesHundred / 100.0;
    }

    public double getItemTeleportY() {
        return this.itemTeleportY;
    }

    public void setItemTeleportY(double itemTeleportY) {
        this.itemTeleportY = itemTeleportY;
    }

    public int getItemTeleportZTimesHundred() {
        return (int) (this.itemTeleportZ * 100);
    }

    public void setItemTeleportZTimesHundred(int itemTeleportZTimesHundred) {
        this.itemTeleportZ = itemTeleportZTimesHundred / 100.0;
    }

    public double getItemTeleportZ() {
        return this.itemTeleportZ;
    }

    public void setItemTeleportZ(double itemTeleportZ) {
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
        this.teleportX = compound.getDouble("teleportX");
        this.teleportY = compound.getDouble("teleportY");
        this.teleportZ = compound.getDouble("teleportZ");
        this.autoDeletion = compound.getBoolean("autoDeletion");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putDouble("teleportX", this.teleportX);
        compound.putDouble("teleportY", this.teleportY);
        compound.putDouble("teleportZ", this.teleportZ);
        compound.putBoolean("autoDeletion", this.autoDeletion);
    }
}
