package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import net.reikeb.electrona.blocks.XPGenerator;
import net.reikeb.electrona.containers.XPGeneratorContainer;

import static net.reikeb.electrona.init.BlockEntityInit.XP_GENERATOR_BLOCK_ENTITY;

public class XPGeneratorBlockEntity extends AbstractBlockEntity {

    public static final BlockEntityTicker<XPGeneratorBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    public double electronicPower;
    private int maxStorage;
    private int wait;
    private int xpLevels;

    public XPGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(XP_GENERATOR_BLOCK_ENTITY.get(), pos, state, 1);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.electrona.xp_generator.name");
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("xp_generator");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new XPGeneratorContainer(id, this.getBlockPos(), playerInventory, player);
    }

    public <T extends BlockEntity> void tick(Level world, BlockPos blockPos, BlockState state, T t) {
        // We get the NBT Tags
        this.getTileData().putInt("MaxStorage", 10000);
        double electronicPower = this.getTileData().getDouble("ElectronicPower");
        int xpLevel = this.getTileData().getInt("XPLevels");
        int xp = this.getTileData().getInt("wait");

        if (world != null) { // Avoid NullPointerExceptions

            // Handle slot
            if ((electronicPower >= 0.8) && (this.inventory.getStackInSlot(0).getItem() == Items.EMERALD)) {
                xp += 1;
                this.getTileData().putInt("wait", xp);
                this.getTileData().putDouble("ElectronicPower", electronicPower - 0.8);
                if (xp >= 4800) {
                    this.inventory.decrStackSize(0, 1);
                    this.getTileData().putInt("XPLevels", xpLevel + 1);
                    this.getTileData().putInt("wait", 0);
                }
            } else {
                this.getTileData().putInt("wait", 0);
            }
            world.setBlockAndUpdate(blockPos, this.getBlockState()
                    .setValue(XPGenerator.ACTIVATED, (xpLevel > 0 || xp > 0)));

            this.setChanged();
            world.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public int getElectronicPowerTimesHundred() {
        return (int) (this.electronicPower * 100);
    }

    public void setElectronicPowerTimesHundred(int electronicPowerTimesHundred) {
        this.electronicPower = electronicPowerTimesHundred / 100.0;
    }

    public double getElectronicPower() {
        return this.electronicPower;
    }

    public void setElectronicPower(double electronicPower) {
        this.electronicPower = electronicPower;
    }

    public int getWait() {
        return this.wait;
    }

    public void setWait(int wait) {
        this.wait = wait;
    }

    public int getXpLevels() {
        return this.xpLevels;
    }

    public void setXpLevels(int xpLevels) {
        this.xpLevels = xpLevels;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        this.xpLevels = compound.getInt("XPLevels");
        this.wait = compound.getInt("wait");
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT((CompoundTag) compound.get("Inventory"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.putInt("XPLevels", this.xpLevels);
        compound.putInt("wait", this.wait);
        compound.put("Inventory", inventory.serializeNBT());
    }
}
