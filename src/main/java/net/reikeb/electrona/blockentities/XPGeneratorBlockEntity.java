package net.reikeb.electrona.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.reikeb.electrona.Electrona;
import net.reikeb.electrona.blocks.XPGenerator;
import net.reikeb.electrona.containers.XPGeneratorContainer;
import net.reikeb.electrona.misc.vm.EnergyFunction;
import net.reikeb.maxilib.abs.AbstractBlockEntity;
import net.reikeb.maxilib.abs.AbstractEnergyBlockEntity;
import net.reikeb.maxilib.inventory.ItemHandler;

import static net.reikeb.electrona.init.BlockEntityInit.XP_GENERATOR_BLOCK_ENTITY;

public class XPGeneratorBlockEntity extends AbstractBlockEntity implements AbstractEnergyBlockEntity {

    public static final BlockEntityTicker<XPGeneratorBlockEntity> TICKER = (level, pos, state, be) -> be.tick(level, pos, state, be);
    public double electronicPower;
    private int maxStorage;
    private int wait;
    private int xpLevels;

    public XPGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(XP_GENERATOR_BLOCK_ENTITY.get(), pos, state, "xp_generator", Electrona.MODID, 1);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new XPGeneratorContainer(id, this.getBlockPos(), playerInventory, player);
    }

    public <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState state, T t) {
        this.setMaxStorage(10000);
        if (level == null) return;

        // Handle slot
        if ((this.electronicPower >= 0.8) && (this.inventory.getStackInSlot(0).getItem() == Items.EMERALD)) {
            wait += 1;
            EnergyFunction.drainEnergy(this, 0.8);
            if (wait >= 4800) {
                this.inventory.decrStackSize(0, 1);
                this.setXpLevels(this.xpLevels + 1);
                this.setWait(0);
            }
        } else {
            this.setWait(0);
        }
        level.setBlockAndUpdate(blockPos, this.getBlockState()
                .setValue(XPGenerator.ACTIVATED, (this.xpLevels > 0 || wait > 0)));

        this.setChanged();
        level.sendBlockUpdated(blockPos, this.getBlockState(), this.getBlockState(), 3);
    }

    public ItemHandler getItemInventory() {
        return this.inventory;
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

    public int getMaxStorage() {
        return this.maxStorage;
    }

    public void setMaxStorage(int maxStorage) {
        this.maxStorage = maxStorage;
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

    public boolean getLogic() {
        return false;
    }

    public void setLogic(boolean logic) {
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.electronicPower = compound.getDouble("ElectronicPower");
        this.maxStorage = compound.getInt("MaxStorage");
        this.xpLevels = compound.getInt("XPLevels");
        this.wait = compound.getInt("wait");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putDouble("ElectronicPower", this.electronicPower);
        compound.putInt("MaxStorage", this.maxStorage);
        compound.putInt("XPLevels", this.xpLevels);
        compound.putInt("wait", this.wait);
    }
}
